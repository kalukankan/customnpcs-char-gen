package com.google.kalukankan.ccg.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.kalukankan.ccg.dto.CharacterDto;

public class CharacterParamGenerator {

	private static final String SUFFIX_OLD_TEXTURE_KBN = "_32";
	private static final Random random = new Random();

	private Map<String, CustomJson> jsonMap;

	public CharacterParamGenerator(Map<String, CustomJson> jsonMap) {
		this.jsonMap = jsonMap;
	}

	public CharacterDto generat(String skinName) {
		Path texture = Paths.get(skinName);
		List<String> jsonKeyList = new ArrayList<>();
		jsonMap.keySet().stream().forEach(x -> jsonKeyList.add(x));

		String fileName = texture.getFileName().toString();
		String name = fileName.substring(0, fileName.lastIndexOf("."));
		boolean isOldTexture = name.endsWith(SUFFIX_OLD_TEXTURE_KBN);
		if (isOldTexture) name = name.substring(0, name.length() - 1 - SUFFIX_OLD_TEXTURE_KBN.length());

		CharacterDto character = new CharacterDto();
		character.setName(name);
		character.setOldTexture(isOldTexture);
		character.setTexture("customnpcs:" + skinName.replaceAll("\\\\", "/"));
		character.setJson(jsonMap.get(jsonKeyList.get(random.nextInt(jsonKeyList.size()))).clone());

		setJson(character);
		return character;
	}

	private void setJson(CharacterDto character) {
		CustomJson json = character.getJson();
		json.set("Name", "\"" + character.getName() + "\"");
		json.set("Texture", "\"" + character.getTexture() + "\"");
		if (character.isOldTexture())
			json.add(
				new String[]{"NpcModelData", "LegParts"},
				new String[]{"NpcModelData", "EntityClass"},
				"\"" + "noppes.npcs.entity.EntityNPC64x32" + "\"");
		json.set(new String[]{"NpcModelData", "Eyes", "Type"}, "-1b");
	}
}
