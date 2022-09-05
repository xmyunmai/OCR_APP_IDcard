package com.yunmai.android.bcr.update;

import java.util.List;

public class VersionEntity {

	private String versionCode;
	private String versionName;
	private String mandatory;
	private List<String> chineseSimplifiedContent;
	private List<String> englishContent;
	private List<String> chineseTraditionalContent;
	private String downloadUrl;
	private String size;
	private String date;

	public VersionEntity() {
		super();
	}

	public VersionEntity(String versionCode, String versionName,
			String mandatory, List<String> chineseSimplifiedContent,
			List<String> englishContent,
			List<String> chineseTraditionalContent, String downloadUrl,
			String size, String date) {
		super();
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.mandatory = mandatory;
		this.chineseSimplifiedContent = chineseSimplifiedContent;
		this.englishContent = englishContent;
		this.chineseTraditionalContent = chineseTraditionalContent;
		this.downloadUrl = downloadUrl;
		this.size = size;
		this.date = date;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public List<String> getChineseSimplifiedContent() {
		return chineseSimplifiedContent;
	}

	public void setChineseSimplifiedContent(
			List<String> chineseSimplifiedContent) {
		this.chineseSimplifiedContent = chineseSimplifiedContent;
	}

	public List<String> getEnglishContent() {
		return englishContent;
	}

	public void setEnglishContent(List<String> englishContent) {
		this.englishContent = englishContent;
	}

	public List<String> getChineseTraditionalContent() {
		return chineseTraditionalContent;
	}

	public void setChineseTraditionalContent(
			List<String> chineseTraditionalContent) {
		this.chineseTraditionalContent = chineseTraditionalContent;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
