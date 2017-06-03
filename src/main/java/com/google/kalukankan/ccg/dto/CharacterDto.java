package com.google.kalukankan.ccg.dto;

import com.google.kalukankan.ccg.model.CustomJson;

public class CharacterDto {

	private String name;
	private String texture;
	private boolean isOldTexture;
	private CustomJson json;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTexture() {
		return texture;
	}
	public void setTexture(String texture) {
		this.texture = texture;
	}
	public boolean isOldTexture() {
		return isOldTexture;
	}
	public void setOldTexture(boolean isOldTexture) {
		this.isOldTexture = isOldTexture;
	}
	public CustomJson getJson() {
		return json;
	}
	public void setJson(CustomJson json) {
		this.json = json;
	}
}
