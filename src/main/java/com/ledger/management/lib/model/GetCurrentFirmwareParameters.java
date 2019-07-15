package com.ledger.management.lib.model;

import com.squareup.moshi.Json;

public class GetCurrentFirmwareParameters {

	public long provider;	
	@Json(name="version_name") public String versionName;
	@Json(name="device_version") public long deviceVersion;

	public GetCurrentFirmwareParameters() {		
	}

	public GetCurrentFirmwareParameters(long provider, String versionName, DeviceVersion deviceVersion) {
		this.provider = provider;
		this.versionName = versionName;
		this.deviceVersion = deviceVersion.id;		
	}

}
