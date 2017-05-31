package com.google.kalukankan.ccg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Application {

	public static void main(String[] args) {
		String minecraftInstallDir = null;
		String minecraftBaseJsons = null;

		try {

			try (InputStreamReader reader = new InputStreamReader(Application.class.getResourceAsStream("/application.properties"), StandardCharsets.UTF_8)) {
				Properties prop = new Properties();

				prop.load(reader);
				minecraftInstallDir = prop.getProperty("minecraftInstallDir");
				minecraftBaseJsons = prop.getProperty("minecraftBaseJsons");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(ReturnCode.PropertiesFileNotFound.ordinal());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(ReturnCode.PropertiesFileReadError.ordinal());
			}

			new CharacterGenerator(minecraftInstallDir, minecraftBaseJsons).run();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ReturnCode.OtherError.ordinal());
		}
	}

	enum ReturnCode {
		Succeed,
		PropertiesFileNotFound,
		PropertiesFileReadError,
		PropertiesFileCloseError,
		OtherError
	}

}
