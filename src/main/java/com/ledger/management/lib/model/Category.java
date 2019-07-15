package com.ledger.management.lib.model;

import java.util.List;
import java.util.Date;

import com.squareup.moshi.Json;

public class Category {

	public long id;
	public String name;
	public String description;
	public List<Long> providers;
	public List<Long> applications;
	@Json(name="date_creation") public Date dateCreation;
	@Json(name="date_last_modified") public Date dateLastModified;
}
