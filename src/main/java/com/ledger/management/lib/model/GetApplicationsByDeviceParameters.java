package com.ledger.management.lib.model;

import com.squareup.moshi.Json;

public class GetApplicationsByDeviceParameters {

	public long provider;	
	@Json(name="device_version") public long deviceVersion;
	@Json(name="current_se_firmware_final_version") public long firmwareId;

	public GetApplicationsByDeviceParameters() {		
	}

	public GetApplicationsByDeviceParameters(long provider, DeviceVersion deviceVersion, FinalFirmware firmware) {
		this.provider = provider;
		this.deviceVersion = deviceVersion.id;
		this.firmwareId = firmware.id;
	}

}
