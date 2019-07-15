package com.ledger.management.lib.model;

import java.util.List;

import com.squareup.moshi.Json;

public class ApplicationVersionList {

	@Json(name="application_versions") public List<ApplicationVersion> applicationVersionList;
}
