package com.ledger.management.lib.model;

import java.util.List;

import com.squareup.moshi.Json;

public class LatestFirmware {

	public String result;
	@Json(name="se_firmware_osu_version") public OsuFirmware osuFirmware;
	public FinalFirmware nextFirmware;

	public boolean successful() {
		return (osuFirmware != null) && (osuFirmware.name != null);
	}
}
