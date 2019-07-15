package com.ledger.management.lib.model;

import java.util.List;
import java.util.Date;

import com.squareup.moshi.Json;

public class ApplicationVersion {

	public long id;
	public String name;
	public String version;
	public long app;	
	public String description;
	@Json(name="display_name") public String displayName;
	public String icon;
	public long picture;
	public String notes;
	public String perso;
	public String hash;
	public String firmware;
	@Json(name="firmware_key") public String firmwareKey;
	public String delete;
	@Json(name="delete_key") public String deleteKey;
	@Json(name="device_versions") public List<Long> deviceVersions;
	@Json(name="se_firmware_final_versions") public List<Long> seFirmwareFinalVersions;
	public List<Long> providers;
	@Json(name="date_creation") public Date dateCreation;
	@Json(name="date_last_modified") public Date dateLastModified;
}
