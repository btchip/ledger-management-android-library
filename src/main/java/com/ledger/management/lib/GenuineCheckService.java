package com.ledger.management.lib;

import java.util.Map;
import java.util.HashMap;

import okhttp3.OkHttpClient;

import com.ledger.lib.transport.LedgerDevice;

/**
 * \brief Renote service running a genuine check on a Ledger device
 */
public class GenuineCheckService extends APDUService {

	private static final String DEFAULT_URL = "https://api.ledgerwallet.com/update/genuine";
	private static final String QUERY_TARGETID = "targetId";
	private static final String QUERY_PERSO = "perso";

	private GenuineCheckService(String url, OkHttpClient client, Map<String, String> queryParameters, LedgerDevice device, int timeout) {
		super(url, client, queryParameters, device, timeout);
	}

	private GenuineCheckService(String url, OkHttpClient client, Map<String, String> queryParameters, LedgerDevice device) {
		super(url, client, queryParameters, device);
	}

	private static Map<String, String> getParameters(long targetId, String perso) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(QUERY_TARGETID, Long.toString(targetId));
		parameters.put(QUERY_PERSO, perso);
		return parameters;
	}

	/**
	 * Get an instance of a Genuine Check service
	 * @param client instance of the HTTP client to use
   * @param device communication interface of the Ledger device to run the script on
   * @param targetId target ID of the device   
   * @param perso personalization reference of the device 
   */
	public static GenuineCheckService getGenuineCheckService(OkHttpClient client, LedgerDevice device, long targetId, String perso) {
		return new GenuineCheckService(DEFAULT_URL, client, getParameters(targetId, perso), device);
	}

	/**
	 * Get an instance of a Genuine Check service
	 * @param url URL of the service endpoint
	 * @param client instance of the HTTP client to use
   * @param device communication interface of the Ledger device to run the script on
   * @param targetId target ID of the device   
   * @param perso personalization reference of the device 
   */
	public static GenuineCheckService getGenuineCheckService(String url, OkHttpClient client, LedgerDevice device, long targetId, String perso) {
		return new GenuineCheckService(url, client, getParameters(targetId, perso), device);
	}

}
