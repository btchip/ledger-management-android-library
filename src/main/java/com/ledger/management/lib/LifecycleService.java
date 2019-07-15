package com.ledger.management.lib;

import java.util.Map;
import java.util.HashMap;

import okhttp3.OkHttpClient;

import com.ledger.lib.transport.LedgerDevice;

/**
 * \brief Renote service managing the lifecycle of applications and firmware on a Ledger device
 */
public class LifecycleService extends APDUService {

	private static final String DEFAULT_URL = "https://api.ledgerwallet.com/update/install";
	private static final String QUERY_TARGETID = "targetId";
	private static final String QUERY_PERSO = "perso";
	private static final String QUERY_FIRMWARE = "firmware";
	private static final String QUERY_FIRMWARE_KEY = "firmwareKey";

	private LifecycleService(String url, OkHttpClient client, Map<String, String> queryParameters, LedgerDevice device, int timeout) {
		super(url, client, queryParameters, device, timeout);
	}

	private LifecycleService(String url, OkHttpClient client, Map<String, String> queryParameters, LedgerDevice device) {
		super(url, client, queryParameters, device);
	}

	private static Map<String, String> getParameters(long targetId, String perso, String firmware, String firmwareKey) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(QUERY_TARGETID, Long.toString(targetId));
		parameters.put(QUERY_PERSO, perso);
		parameters.put(QUERY_FIRMWARE, firmware);
		parameters.put(QUERY_FIRMWARE_KEY, firmwareKey);
		return parameters;
	}

	/**
	 * Get an instance of a Lifecycle service
	 * @param client instance of the HTTP client to use
   * @param device communication interface of the Ledger device to run the script on
   * @param targetId target ID of the device   
   * @param perso personalization reference of the device 
   * @param firmware reference of the application or firmware to provision
   * @param firmwareKey reference of the application key or firmware key to provision
   * @return instance of the Lifecycle service
   */
	public static LifecycleService getLifecycleService(OkHttpClient client, LedgerDevice device, long targetId, String perso, String firmware, String firmwareKey) {
		return new LifecycleService(DEFAULT_URL, client, getParameters(targetId, perso, firmware, firmwareKey), device);
	}

	/**
	 * Get an instance of a Lifecycle service
	 * @param url URL of the service endpoint	
	 * @param client instance of the HTTP client to use
   * @param device communication interface of the Ledger device to run the script on
   * @param targetId target ID of the device   
   * @param perso personalization reference of the device 
   * @param firmware reference of the application or firmware to provision
   * @param firmwareKey reference of the application key or firmware key to provision
   * @return instance of the Lifecycle service
   */
	public static LifecycleService getLifecycleService(String url, OkHttpClient client, LedgerDevice device, long targetId, String perso, String firmware, String firmwareKey) {
		return new LifecycleService(url, client, getParameters(targetId, perso, firmware, firmwareKey), device);
	}

}
