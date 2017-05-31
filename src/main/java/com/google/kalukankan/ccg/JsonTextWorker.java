package com.google.kalukankan.ccg;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonTextWorker {
	private List<String> content = new LinkedList<>();

	public JsonTextWorker(String content) {
		this.content = Arrays.asList(content.split(System.lineSeparator()));
	}

	public String get(String key) {
		for (String line : content) {
			if (line.startsWith(key)) return line.substring(key.length() + 1).trim();
		}
		return null;
	}

	public void set(String key, String value) {
		for (int i = 0; i < content.size(); i++) {
			if (!content.get(i).startsWith(key)) continue;
			content.set(i, key + ": " + value + ",");
			return;
		}
	}

	@Override
	public String toString() {
		String result = content.stream().collect(Collectors.joining(System.lineSeparator()));
		return result.replaceAll("\r\n", "\n");
	}
}
