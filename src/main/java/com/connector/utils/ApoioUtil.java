package com.connector.utils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

//Classe com métodos de apoio
public class ApoioUtil {

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			String str = ((String) obj).trim();
			if ("".equals(str)) {
				return true;
			}
		} else if (obj instanceof Object[]) {
			if (((Object[]) obj).length == 0) {
				return true;
			}
		} else if (obj instanceof Integer) {
			if (((Integer) obj).intValue() == 0) {
				return true;
			}
		} else if (obj instanceof Long) {
			if (((Long) obj).longValue() == 0) {
				return true;
			}
		} else if (obj instanceof Boolean) {
			if (!((Boolean) obj).booleanValue()) {
				return true;
			}
		} else if (obj instanceof Collection) {
			if (((Collection<?>) obj).isEmpty()) {
				return true;
			}
		} else if (obj instanceof Float) {
			if (((Float) obj).floatValue() == 0) {
				return true;
			}
		} else if (obj instanceof Optional) {
			Optional<?> o = (Optional<?>) obj;
			return o.isPresent()
					? isEmpty(o.get())
					: o.isEmpty();
		}
		return false;
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static String extractIdsFromUrls(List<String> urls) {
		if (isEmpty(urls)) {
			return "";
		}

		List<Integer> ulrsInt = urls.stream()
				.map(ApoioUtil::getIdFromUrl)
				.collect(Collectors.toList());

		return getContentAsJson(ulrsInt);
	}

	public static Integer getIdFromUrl(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		try {
			String[] parts = url.split("/");
			String idString = parts[parts.length - 1];

			return Integer.parseInt(idString);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public static <T> String getContentAsJson(List<T> content) {
		if (content == null || content.isEmpty()) {
			return "";
		}

		try (Jsonb jsonb = JsonbBuilder.create()) {
			return jsonb.toJson(content);
		} catch (Exception e) {
			return "";
		}
	}
}
