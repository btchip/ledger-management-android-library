package com.ledger.management.lib.model;

import java.util.List;
import java.util.Date;

import com.squareup.moshi.Json;

public class McuVersion {

	public long id;
	public long mcu;
	public String name;
	public String description;
	public List<Long> providers;
	@Json(name="from_bootloader_version") public String fromBootloaderVersion;
	@Json(name="device_versions") public List<Long> deviceVersions;
	@Json(name="se_firmware_final_versions") public List<Long> seFirmwareFinalVersions;
	@Json(name="date_creation") public Date dateCreation;
	@Json(name="date_last_modified") public Date dateLastModified;
}
