package com.ledger.management.lib.model;

import com.squareup.moshi.Json;

public class GetLatestFirmwareParameters {

	public long provider;	
	@Json(name="device_version") public long deviceVersion;
	@Json(name="current_se_firmware_final_version") public long firmwareId;

	public GetLatestFirmwareParameters() {		
	}

	public GetLatestFirmwareParameters(long provider, DeviceVersion deviceVersion, FinalFirmware firmware) {
		this.provider = provider;
		this.deviceVersion = deviceVersion.id;
		this.firmwareId = firmware.id;
	}

}
