package com.derek.ltapoc.model;

import java.util.Date;

public class ChargingScheme {
	private String mSchemeName;
	private String mSchemeDescription;
	private String mCreatedBy;
	private Date mCreatedDate;

	public String getSchemeName() {
		return mSchemeName;
	}

	public void setSchemeName(String schemeName) {
		mSchemeName = schemeName;
	}

	public String getSchemeDescription() {
		return mSchemeDescription;
	}

	public void setSchemeDescription(String schemeDescription) {
		mSchemeDescription = schemeDescription;
	}

	public String getCreatedBy() {
		return mCreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		mCreatedBy = createdBy;
	}

	public Date getCreatedDate() {
		return mCreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		mCreatedDate = createdDate;
	}
}
