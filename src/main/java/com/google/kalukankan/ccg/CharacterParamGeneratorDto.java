package com.google.kalukankan.ccg;

import java.util.Map;

public class CharacterParamGeneratorDto {

	private String texturePath;
	private Map<String, String> jsonMap;

	public String getTexturePath() {
		return texturePath;
	}

	public void setTexturePath(String texturePath) {
		this.texturePath = texturePath;
	}

	public Map<String, String> getJsonMap() {
		return jsonMap;
	}

	public void setJsonMap(Map<String, String> jsonMap) {
		this.jsonMap = jsonMap;
	}
}
