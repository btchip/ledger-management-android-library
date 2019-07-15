package com.ledger.management.lib;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

import android.util.Log;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.HttpUrl;

import com.ledger.lib.transport.LedgerDevice;
import com.ledger.lib.LedgerException;
import com.ledger.lib.utils.Dump;

/**
 * \brief Abstract model of a remote management service for a Ledger device, calling the remote service
 * getting the commands to run, and running them on a device
 */
public abstract class APDUService {

	private static final int DEFAULT_TIMEOUT_MS = 300000;

	private static final int WEBSOCKET_CLOSE_NORMAL = 1000;
	private static final int WEBSOCKET_CLOSE_PROTOCOL_ERROR = 1002;
	private static final String LOG_STRING = "APDUService";  

	private static final String WS_QUERY = "query";
	private static final String WS_DATA = "data";
	private static final String WS_NONCE = "nonce";
	private static final String WS_RESPONSE = "response";
	private static final String QUERY_SUCCESS = "success";
	private static final String QUERY_ERROR = "error";	
	private static final String QUERY_EXCHANGE = "exchange";
	private static final String QUERY_BULK = "bulk";
	private static final String RESPONSE_UNSUPPORTED = "unsupported";
	private static final String RESPONSE_INTERNAL = "internal";
	private static final String RESPONSE_INVALID_STATUS = "invalid_status";
	private static final String RESPONSE_SUCCESS = "success";

	private static final byte[] SW_OK = { (byte)0x90, (byte)0x00 };

	private String url;
	private OkHttpClient client;
	private Map<String, String> queryParameters;
	private LedgerDevice device;
	private LinkedBlockingQueue<APDUServiceCallback.SocketEvent> blockingQueue;
	private APDUServiceCallback websocketCallback;
	private WebSocket webSocket;
	private int timeout;	  
	private boolean debug;

  /**
   * Constructor
   * @param url URL of the service endpoint
   * @param client instance of the HTTP client to use
   * @param queryParameters parameters to pass in the request query string
   * @param device communication interface of the Ledger device to run the script on
   * @param timeout communication timeout with the remote service in milliseconds
   */
	public APDUService(String url, OkHttpClient client, Map<String, String> queryParameters, LedgerDevice device, int timeout) {
		this.url = url;
		this.client = client;
		this.queryParameters = queryParameters;
		this.device = device;
		this.timeout = timeout;
		blockingQueue = new LinkedBlockingQueue<APDUServiceCallback.SocketEvent>();
		websocketCallback = new APDUServiceCallback(blockingQueue);
	}

  /**
   * Constructor
   * @param url URL of the service endpoint
   * @param client instance of the HTTP client to use
   * @param queryParameters parameters to pass in the request query string
   * @param device communication interface of the Ledger device to run the script on
   */
	public APDUService(String url, OkHttpClient client, Map<String, String> queryParameters, LedgerDevice device) {
		this(url, client, queryParameters, device, DEFAULT_TIMEOUT_MS);
	}

	/** 
	 * Set the debug configuration
	 * @param debug true to get additional debugging output
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	private APDUServiceCallback.SocketEvent waitEvent() {
		for (;;) {
			APDUServiceCallback.SocketEvent event = null;
			try {
				event = blockingQueue.poll(timeout, TimeUnit.MILLISECONDS);
			}
      catch(InterruptedException ex) {
      	webSocket.close(WEBSOCKET_CLOSE_PROTOCOL_ERROR, "Interrupted");
        throw new LedgerException(LedgerException.ExceptionReason.INTERNAL_ERROR, ex);
      }
      if (event == null) {
      	webSocket.close(WEBSOCKET_CLOSE_PROTOCOL_ERROR, "Timeout");
        throw new LedgerException(LedgerException.ExceptionReason.IO_ERROR, "Timeout");
      }
      if (debug) {
        Log.d(LOG_STRING, "Received " + event.toString());
      }
			switch(event.getEventType()) {
				case SOCKET_READY:
					break;
				case SOCKET_MESSAGE:
					return event;
				case SOCKET_ERROR:
					webSocket.close(WEBSOCKET_CLOSE_PROTOCOL_ERROR, "");
					throw new LedgerException(LedgerException.ExceptionReason.IO_ERROR, event.getException());
			}
		}
	}

	private JSONObject process(JSONObject data) throws JSONException {		
		JSONObject response = new JSONObject();		
		try {
			byte[] apduResponse = null;
			response.put(WS_NONCE, data.optInt(WS_NONCE, 0));
			String query = data.getString(WS_QUERY);
			if (query.equals(QUERY_EXCHANGE)) {
				byte[] apdu = Dump.hexToBin(data.getString(WS_DATA));
				apduResponse = device.exchange(apdu);
			}
			else
			if (query.equals(QUERY_BULK)) {
				JSONArray apduArray = data.getJSONArray(WS_DATA);
				for (int i=0; i<apduArray.length(); i++) {
					byte[] apdu = Dump.hexToBin(apduArray.getString(i));
					apduResponse = device.exchange(apdu);
					if ((apduResponse.length == 2) && !Arrays.equals(apduResponse, SW_OK)) {
						break;
					}
				}
			}
			else {
				response.put(WS_RESPONSE, RESPONSE_UNSUPPORTED);
			}
			if (apduResponse != null) {				
				if (apduResponse.length == 2) {					
					response.put(WS_RESPONSE, (Arrays.equals(apduResponse, SW_OK) ? RESPONSE_SUCCESS : RESPONSE_INVALID_STATUS));
					response.put(WS_DATA, Dump.dump(apduResponse));
				}
				else {
					response.put(WS_RESPONSE, RESPONSE_SUCCESS);
					response.put(WS_DATA, Dump.dump(apduResponse, 0, apduResponse.length - 2));						
				}
			}
		}
		catch(JSONException e1) {
			if (debug) {
				Log.d(LOG_STRING, "Parse error {}", e1);
			}
			response.put(WS_RESPONSE, RESPONSE_INTERNAL);
		}
		catch(LedgerException e2) {
			if (debug) {
				Log.d(LOG_STRING, "Communication error {}", e2);
			}
			response.put(WS_RESPONSE, RESPONSE_INTERNAL);
		}

		return response;
	}

	/**
	 * Launch the request to the remote service and execute the script
	 * @return script execution result
	 */
	public String start() {		
		String response = null;

		HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();
		if (queryParameters != null) {
       for(Map.Entry<String, String> param : queryParameters.entrySet()) {
           httpBuider.addQueryParameter(param.getKey(),param.getValue());
       }			
		}
		Request request = new Request.Builder().url(httpBuider.build()).build();
		webSocket = client.newWebSocket(request, websocketCallback);		
		for (;;) {
			APDUServiceCallback.SocketEvent event = waitEvent();
			JSONObject jsonData = null;
			try {
				jsonData = new JSONObject(event.getData());
				String query = jsonData.getString(WS_QUERY);
				if (query.equals(QUERY_SUCCESS)) {
					response = jsonData.optString(WS_DATA, "");
					webSocket.close(WEBSOCKET_CLOSE_NORMAL, "");
					break;
				}
				else
				if (query.equals(QUERY_ERROR)) {
					String errorReason = jsonData.optString(WS_DATA, "");
					webSocket.close(WEBSOCKET_CLOSE_PROTOCOL_ERROR, "");
					throw new LedgerException(LedgerException.ExceptionReason.INTERNAL_ERROR, errorReason);
				}
				JSONObject responseData = process(jsonData);
				if (!webSocket.send(responseData.toString())) {
					Log.d(LOG_STRING, "Failed to send");
					webSocket.close(WEBSOCKET_CLOSE_PROTOCOL_ERROR, "Couldn't send data");
					throw new LedgerException(LedgerException.ExceptionReason.IO_ERROR, "Couldn't send data");
				}
			}
			catch(JSONException e) {
				e.printStackTrace();
      	webSocket.close(WEBSOCKET_CLOSE_PROTOCOL_ERROR, "Malformed JSON data");
        throw new LedgerException(LedgerException.ExceptionReason.INTERNAL_ERROR, "Malformed JSON data");				
			}
		}		
		return response;
	}
}
