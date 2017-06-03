package com.google.kalukankan.ccg;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.kalukankan.ccg.exception.CcgException;
import com.google.kalukankan.ccg.service.CharacterGeneratorService;

public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		try {
			String minecraftInstallDir = null;
			String minecraftBaseJsons = null;

			try (InputStreamReader reader = new InputStreamReader(Application.class.getResourceAsStream("/application.properties"), StandardCharsets.UTF_8)) {
				Properties prop = new Properties();
				prop.load(reader);
				minecraftInstallDir = prop.getProperty("minecraftInstallDir");
				minecraftBaseJsons = prop.getProperty("minecraftBaseJsons");
			}

			// Validation
			Path minecraftInstallDirPath;
			try {
				minecraftInstallDirPath = Paths.get(minecraftInstallDir);
			} catch(InvalidPathException e) {
				throw new CcgException(String.format("application.properties の minecraftInstallDirPath に正しいパスを指定してください。 現在の値=%s", minecraftInstallDir));
			}
			if (!Files.isDirectory(minecraftInstallDirPath)) {
				throw new CcgException(String.format("application.properties の minecraftInstallDirPath に指定したパスはフォルダではありません。正しいパスを指定してください。 現在の値=%s", minecraftInstallDir));
			}
			if (Files.notExists(minecraftInstallDirPath)) {
				throw new CcgException(String.format("application.properties の minecraftInstallDirPath に指定したフォルダがありません。正しいパスを指定してください。 フォルダパス=%s", minecraftInstallDirPath.toAbsolutePath().toString()));
			}
			if (minecraftBaseJsons == null || minecraftBaseJsons.length() == 0) {
				throw new CcgException(String.format("application.properties の minecraftBaseJsons に値を設定してください。 現在の値=%s", minecraftBaseJsons));
			}
			String[] jsons = minecraftBaseJsons.split(",");
			long count = Arrays.stream(jsons).filter(new Predicate<String>() {
				@Override
			    public boolean test(String t) {
			        return t.endsWith(".json");
			    }
			}).count();
			if (count != jsons.length) {
				throw new CcgException(String.format("application.properties の minecraftBaseJsons に.jsonではないファイル名が指定されています。.jsonファイルをカンマ区切りで指定してください。 現在の値=%s", minecraftBaseJsons));
			}

			new CharacterGeneratorService(minecraftInstallDir, minecraftBaseJsons).run();

		} catch (CcgException e) {
			log.error("エラーが発生したため、処理を終了します。エラー内容を確認して問題を修正してください。", e);
			System.exit(ReturnCode.Error.ordinal());
		} catch (Exception e) {
			log.error("予期しないエラーが発生しました。申し訳ございませんがエラー内容を開発者にご連絡ください。", e);
			System.exit(ReturnCode.Error.ordinal());
		}
	}

	enum ReturnCode {
		/** 正常終了 */
		Succeed,
		/** 異常終了 */
		Error
	}

}
