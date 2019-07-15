package com.ledger.management.lib;

import java.util.Map;
import java.util.HashMap;

import okhttp3.OkHttpClient;

import com.ledger.lib.transport.LedgerDevice;

/**
 * \brief Renote service handling the installation of a new non secure MCU firmware on a Ledger device
 */
public class McuService extends APDUService {

	private static final String DEFAULT_URL = "https://api.ledgerwallet.com/update/mcu";
	private static final String QUERY_VERSION = "version";

	private McuService(String url, OkHttpClient client, Map<String, String> queryParameters, LedgerDevice device, int timeout) {
		super(url, client, queryParameters, device, timeout);
	}

	private McuService(String url, OkHttpClient client, Map<String, String> queryParameters, LedgerDevice device) {
		super(url, client, queryParameters, device);
	}

	private static Map<String, String> getParameters(String version) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(QUERY_VERSION, version);
		return parameters;
	}

	/**
	 * Get an instance of a MCU service
	 * @param client instance of the HTTP client to use
   * @param device communication interface of the Ledger device to run the script on
   * @param version version of the MCU to install
   * @return instance of the MCU service
   */
	public static McuService getMcuService(OkHttpClient client, LedgerDevice device, String version) {
		return new McuService(DEFAULT_URL, client, getParameters(version), device);
	}

	/**
	 * Get an instance of a MCU service
	 * @param url URL of the service endpoint		 
	 * @param client instance of the HTTP client to use
   * @param device communication interface of the Ledger device to run the script on
   * @param version version of the MCU to install
   * @return instance of the MCU service
   */
	public static McuService getMcuService(String url, OkHttpClient client, LedgerDevice device, String version) {
		return new McuService(url, client, getParameters(version), device);
	}

}
