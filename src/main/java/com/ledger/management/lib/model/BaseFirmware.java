package com.ledger.management.lib.model;

import java.util.List;
import java.util.Date;

import com.squareup.moshi.Json;

public class BaseFirmware {

	public long id;
	public String name;
	public String description;
	@Json(name="display_name") public String displayName;
	public String notes;
	public String perso;
	public String firmware;
	@Json(name="firmware_key") public String firmwareKey;
	public String hash;
	@Json(name="date_creation") public Date dateCreation;
	@Json(name="date_last_modified") public Date dateLastModified;
	@Json(name="device_versions") public List<Long> deviceVersions;
	public List<Long> providers;
}
