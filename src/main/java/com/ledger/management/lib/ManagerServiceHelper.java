package com.ledger.management.lib;

import java.util.Date;

import com.squareup.moshi.Rfc3339DateJsonAdapter;
import com.squareup.moshi.Moshi;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * \brief Helper class returning an automatically built ManagerService instance
 */
public class ManagerServiceHelper {

	private static final String LEDGER_MANAGER_URL = "https://manager.api.live.ledger.com/";

	/**
	 * Get a ManagerService instance using the given url
	 * @param url URL mapping the Manager service
	 * @return ManagerService instance
	 */
	public static ManagerService getDefaultManagerService(String url) {
		Moshi moshi = new Moshi.Builder()
			.add(Date.class, new Rfc3339DateJsonAdapter().nullSafe())
			.build();
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(LEDGER_MANAGER_URL)
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.build();
		return retrofit.create(ManagerService.class);
	}

	/**
	 * Get a ManagerService instance using the default url
	 * @return ManagerService instance
	 */
	public static ManagerService getDefaultManagerService() {
		return getDefaultManagerService(LEDGER_MANAGER_URL);
	}

}
