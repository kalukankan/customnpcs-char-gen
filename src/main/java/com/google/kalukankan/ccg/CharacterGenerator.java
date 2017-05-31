package com.google.kalukankan.ccg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CharacterGenerator {

	private static final String TEXTURE_DIR = "\\customnpcs\\assets\\customnpcs\\textures";
	private static final String JSON_DIR = "\\customnpcs\\clones";

	private String minecraftInstallDir;
	private String minecraftBaseJsons;

	public CharacterGenerator(String minecraftInstallDir, String minecraftBaseJsons) {
		this.minecraftInstallDir = minecraftInstallDir;
		this.minecraftBaseJsons = minecraftBaseJsons;
	}

	public void run() {
		List<String> skinNameList = getSkinNames();
		Map<String, String> jsonMap = getJson();
		for (String skinName : skinNameList) {
			CharacterParamGeneratorDto dto = new CharacterParamGeneratorDto();
			dto.setJsonMap(jsonMap);
			dto.setTexturePath(skinName);
			CharacterParamGenerator g = new CharacterParamGenerator();
			Character c = g.generat(dto);
			String characterJson = replaceJson(c);
			Path dst = Paths.get(c.getName() + ".json");
			if (!Files.exists(dst)) {
				try {
					Files.createFile(dst);
				} catch (IOException e1) {
					e1.printStackTrace();
					throw new RuntimeException("ファイル作成時エラー", e1);
				}
			}
			try (BufferedWriter bw = Files.newBufferedWriter(dst, StandardOpenOption.WRITE)) {
				bw.write(characterJson);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("jsonファイル書き込み時エラー", e);
			}
		}
	}

	private List<String> getSkinNames() {
		Path dir = Paths.get(minecraftInstallDir, TEXTURE_DIR);
		final List<Path> list = new ArrayList<>();
		try {
			Files.walkFileTree(dir, new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().endsWith(".png")) {
						list.add(file);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("スキンファイル読み込み時エラー", e);
		}

		List<String> skinNameList = new LinkedList<String>();
		for (Path path : list) {
			String name = path.toString();
			name = name.substring(name.indexOf(TEXTURE_DIR) + TEXTURE_DIR.length() - "textures".length());
			skinNameList.add(name);
		}
		return skinNameList;
	}

	private Map<String, String> getJson() {
		String[] jsonNames = minecraftBaseJsons.split(",");
		List<String> jsonNameList = Arrays.asList(jsonNames);

		Path dir = Paths.get(minecraftInstallDir, JSON_DIR);
		Map<String, String> map = new HashMap<>();
		try {
			Files.walkFileTree(dir, new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().endsWith(".json")) {
						String fileName = file.getFileName().toString();
						if (jsonNameList.stream().anyMatch(x -> fileName.equals(x))) {
							String content = Files.lines(file, StandardCharsets.UTF_8).collect(Collectors.joining(System.lineSeparator()));
							map.put(fileName, content);
						}
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Jsonファイル読み込み時エラー", e);
		}
		return map;
	}

	private String replaceJson(Character character) {
		String result = new String(character.getJson());
		JsonTextWorker worker = new JsonTextWorker(result);
		worker.set("    \"Name\"", "\"" + character.getName() + "\"");
		worker.set("    \"Texture\"", "\"" + character.getTexture() + "\"");
		return worker.toString();
	}
}
