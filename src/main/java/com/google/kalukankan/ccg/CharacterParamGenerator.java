package com.google.kalukankan.ccg;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CharacterParamGenerator {

	private static final Random random = new Random();

	public Character generat(CharacterParamGeneratorDto dto) {
		Path texture = Paths.get(dto.getTexturePath());
		List<String> jsonKeyList = new ArrayList<>();
		dto.getJsonMap().keySet().stream().forEach(x -> jsonKeyList.add(x));

		String fileName = texture.getFileName().toString();
		String name = fileName.substring(0, fileName.indexOf("."));

		Character character = new Character();
		character.setName(name);
		character.setTexture("customnpcs:" + dto.getTexturePath().replaceAll("\\\\", "/"));
		character.setJson(dto.getJsonMap().get(jsonKeyList.get(random.nextInt(jsonKeyList.size()))));
		return character;
	}
}
