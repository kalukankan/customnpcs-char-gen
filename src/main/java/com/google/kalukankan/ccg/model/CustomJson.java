package com.google.kalukankan.ccg.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.kalukankan.ccg.exception.CcgException;

/**
 * CustomNPCs用Josnデータモデル<br>
 * CustomNPCsのjsonは数値の後ろにアルファベットが付与されているため、通常のJosnライブラリで扱えない。<br>
 * とりあえずライブラリが見つかるまではこれを使用しておきます。
 */
public class CustomJson implements Cloneable {

	protected final Logger log = LoggerFactory.getLogger(CustomJson.class);
	private List<String> content = new LinkedList<>();

	public CustomJson(String content) {
		Arrays.stream(content.split(System.lineSeparator())).forEach(x -> this.content.add(x));
	}

	public String get(String key) {
		return get(new String[]{key});
	}

	public String get(String[] keys) {
		String[] cnvKeys = encloseDoubleQuote(keys);
		int index = indexOf(cnvKeys);
		return (index != -1) ? content.get(index) : null;
	}

	public void set(String key, String value) {
		set(new String[]{key}, value);
	}

	public void set(String[] keys, String value) {
		String[] cnvKeys = encloseDoubleQuote(keys);
		int index = indexOf(cnvKeys);
		if (index != -1) content.set(index, repeat("    ", keys.length) + cnvKeys[keys.length - 1] + ": " + value + ",");
	}

	public void add(String[] locationKeys, String[] keys, String value) {
		String[] cnvLocationKeys = encloseDoubleQuote(locationKeys);
		String[] cnvKeys = encloseDoubleQuote(keys);
		int index = indexOf(cnvLocationKeys);
		if (index != -1) content.add(index, repeat("    ", keys.length) + cnvKeys[keys.length - 1] + ": " + value + ",");
	}

	public int indexOf(final String[] keys) {
		String[] searchKeys = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			searchKeys[i] = repeat("    ", i + 1) + keys[i];
		}
		int keyIndex = 0;
		for (int i = 0; i < content.size(); i++) {
			String line = content.get(i);
			log.debug(String.format("i=%d searchKeys[%d]=%s line=%s", i, keyIndex, searchKeys[keyIndex], line));

			// キーと一致しない場合、検索続行
			if (!line.startsWith(searchKeys[keyIndex])) continue;

			// キーが一致した場合
			if (keyIndex == searchKeys.length - 1) {
				return i;
			}
			keyIndex++;
		}

		log.debug(String.format("keys not match. keys=%s", keys.toString()));
		return -1;
	}

	@Override
	public String toString() {
		String result = content.stream().collect(Collectors.joining("\n"));
		return result;
	}

	private String repeat(String str, int count) {
		String result = "";
		for (int i = 0; i < count; i++) {
			result += str;
		}
		return result;
	}

	private String[] encloseDoubleQuote(String[] strs) {
		String[] result = strs.clone();
		for (int i = 0; i < result.length; i++) {
			result[i] = "\"" + result[i] + "\"";
		}
		return result;
	}

	@Override
	public CustomJson clone() {
		List<String> contentTemp = new LinkedList<>();
		content.stream().forEach(x -> contentTemp.add(new String(x)));
		CustomJson instance;
		try {
			instance = (CustomJson)super.clone();
			instance.content = contentTemp;
		} catch (CloneNotSupportedException e) {
			throw new CcgException("クローン処理に失敗しました。");
		}
		return instance;
	}
}
