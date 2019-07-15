package com.ledger.management.lib.model;

import java.util.List;

import com.squareup.moshi.Json;

public class FinalFirmware extends BaseFirmware {

	public String version;
	@Json(name="se_firmware") public Long seFirmware;
	@Json(name="osu_versions") public List<OsuFirmware> osuVersions;
	@Json(name="mcu_versions") public List<Long> mcuVersions;
	@Json(name="application_versions") public List<Long> applicationVersions;
}
