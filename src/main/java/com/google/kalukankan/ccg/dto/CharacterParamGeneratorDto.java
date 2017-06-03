package com.google.kalukankan.ccg.dto;

import java.util.Map;

import com.google.kalukankan.ccg.model.CustomJson;

public class CharacterParamGeneratorDto {

	private String texturePath;
	private Map<String, CustomJson> jsonMap;

	public String getTexturePath() {
		return texturePath;
	}

	public void setTexturePath(String texturePath) {
		this.texturePath = texturePath;
	}

	public Map<String, CustomJson> getJsonMap() {
		return jsonMap;
	}

	public void setJsonMap(Map<String, CustomJson> jsonMap) {
		this.jsonMap = jsonMap;
	}
}
