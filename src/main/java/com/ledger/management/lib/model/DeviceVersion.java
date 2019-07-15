package com.ledger.management.lib.model;

import java.util.List;
import java.util.Date;

import com.squareup.moshi.Json;

public class DeviceVersion {

	public long id;
	public String name;
	@Json(name="display_name") public String displayName;
	@Json(name="target_id") public long targetId;
	public String description;
	public long device;
	public List<Long> providers;
	@Json(name="mcu_versions") public List<Long> mcuVersions;
	@Json(name="se_firmware_final_versions") public List<Long> seFirmwareFinalVersions;
	@Json(name="osu_versions") public List<Long> osuVersions;
	@Json(name="application_versions") public List<Long> applicationVersions;
	@Json(name="date_creation") public Date dateCreation;
	@Json(name="date_last_modified") public Date dateLastModified;
}
