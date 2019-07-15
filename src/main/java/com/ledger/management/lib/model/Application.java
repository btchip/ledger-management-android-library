package com.ledger.management.lib.model;

import java.util.List;
import java.util.Date;

import com.squareup.moshi.Json;

public class Application {

	public long id;
	public String name;
	public String description;
	@Json(name="application_versions") public List<ApplicationVersion> applicationVersions;
	public List<Long> providers;
	public long category;
	public long publisher;
	@Json(name="date_creation") public Date dateCreation;
	@Json(name="date_last_modified") public Date dateLastModified;
}
