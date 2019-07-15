package com.ledger.management.lib.model;

import com.squareup.moshi.Json;

public class GetDeviceVersionParameters {

	public long provider;	
	@Json(name="target_id") public long targetId;

}
