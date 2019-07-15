package com.ledger.management.lib.model;

import java.util.List;

import com.squareup.moshi.Json;

public class OsuFirmware extends BaseFirmware {

	@Json(name="next_se_firmware_final_version") public Long nextSeFirmwareFirmwareVersion;
	@Json(name="previous_se_firmware_final_version") public List<Long> previousSeFirmwareFinalVersion;
}
