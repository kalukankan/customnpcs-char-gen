package com.google.kalukankan.ccg.service;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.kalukankan.ccg.Application;
import com.google.kalukankan.ccg.dto.CharacterDto;
import com.google.kalukankan.ccg.exception.CcgException;
import com.google.kalukankan.ccg.model.CharacterParamGenerator;
import com.google.kalukankan.ccg.model.CustomJson;

public class CharacterGeneratorService {

	private static final String TEXTURE_DIR = "\\customnpcs\\assets\\customnpcs\\textures";
	private static final String JSON_DIR = "\\customnpcs\\clones";
	private static final String EXPORT_JSON_DIR = ".\\export";

	protected final Logger log = LoggerFactory.getLogger(Application.class);

	private String minecraftInstallDir;
	private String minecraftBaseJsons;

	public CharacterGeneratorService(String minecraftInstallDir, String minecraftBaseJsons) {
		this.minecraftInstallDir = minecraftInstallDir;
		this.minecraftBaseJsons = minecraftBaseJsons;
	}

	public void run() {

		log.info(String.format("処理開始"));

		log.info(String.format("スキン名取得開始"));
		List<String> skinNameList = getSkinNames();
		log.info(String.format("スキン名取得完了 count=%d", skinNameList.size()));
		if (skinNameList.size() == 0) {
			log.warn(String.format("スキンがありません。処理を終了します。"));
			return;
		}

		log.info(String.format("キャラクターJsonデータ取得開始"));
		Map<String, CustomJson> jsonMap = getJson();
		log.info(String.format("キャラクターJsonデータ取得完了 count=%d", jsonMap.size()));
		if (jsonMap.size() == 0) {
			log.warn(String.format("キャラクターJsonデータがありません。処理を終了します。"));
			return;
		}

		int total = skinNameList.size();
		int index = 1;
		CharacterParamGenerator gen = new CharacterParamGenerator(jsonMap);
		for (String skinName : skinNameList) {
			log.info(String.format("キャラクターデータ作成(%d/%d) スキン名=%s", index, total, skinName));
			CharacterDto c = gen.generat(skinName);

			log.info(String.format("キャラクターJson作成(%d/%d)", index, total));
			createExportDir();
			writeCharacterJson(c);
			index++;
		}

		log.info(String.format("処理終了"));
	}

	private List<String> getSkinNames() {
		Path dir = Paths.get(minecraftInstallDir, TEXTURE_DIR);
		if (Files.notExists(dir)) {
			throw new CcgException(String.format("%s フォルダが存在しません。apprication.propertiesのminecraftInstallDirに正しいパスが設定されているか確認してください。"
					, dir.toAbsolutePath().toString()));
		}

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
			throw new CcgException("スキンファイル読み込み時にエラーが発生しました。正しいスキンファイルが配置されているか確認してください。", e);
		}

		List<String> skinNameList = new LinkedList<String>();
		for (Path path : list) {
			String name = path.toString();
			name = name.substring(name.indexOf(TEXTURE_DIR) + TEXTURE_DIR.length() - "textures".length());
			skinNameList.add(name);
		}

		return skinNameList;
	}

	private Map<String, CustomJson> getJson() {

		Path dir = Paths.get(minecraftInstallDir, JSON_DIR);
		if (Files.notExists(dir)) {
			throw new CcgException(String.format("%s フォルダが存在しません。apprication.propertiesのminecraftInstallDirに正しいパスが設定されているか確認してください。"
					, dir.toAbsolutePath().toString()));
		}

		List<String> jsonNameList = Arrays.asList(minecraftBaseJsons.split(","));
		Map<String, CustomJson> map = new HashMap<>();
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
							map.put(fileName, new CustomJson(content));
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
			throw new CcgException("キャラクターjsonファイル読み込み時にエラーが発生しました。キャラクターjsonファイルが配置されているか確認してください。", e);
		}
		return map;
	}

	private void createExportDir() {
		Path dstDir = Paths.get(EXPORT_JSON_DIR);
		if (Files.notExists(dstDir)) {
			try {
				Files.createDirectories(dstDir);
			} catch (IOException e) {
				throw new CcgException(String.format(
						"キャラクターJsonを出力するフォルダが作成できません。一度 %s フォルダを削除してもう一度実行してください。"
						, Paths.get(EXPORT_JSON_DIR).toAbsolutePath().toString()), e);
			}
		}
	}

	private void writeCharacterJson(CharacterDto c) {

		Path dst = Paths.get(EXPORT_JSON_DIR + "\\" + c.getName() + ".json");
		log.debug(String.format("file=%s", dst.toString()));

		if (Files.notExists(dst)) {
			try {
				Files.createFile(dst);
			} catch (IOException e1) {
				throw new CcgException(String.format(
						"キャラクターJsonファイルの作成時にエラーが発生しました。一度 %s を削除して再度実行してください。"
						, Paths.get(EXPORT_JSON_DIR).toAbsolutePath().toString()), e1);
			}
		}
		try (BufferedWriter bw = Files.newBufferedWriter(dst, StandardOpenOption.WRITE)) {
			bw.write(c.getJson().toString());
		} catch (IOException e) {
			throw new CcgException(String.format(
				"キャラクターJsonファイルの書き込み中にエラーが発生しました。一度 %s を削除して再度実行してください。"
				, dst.toAbsolutePath().toString()), e);
		}
	}
}
