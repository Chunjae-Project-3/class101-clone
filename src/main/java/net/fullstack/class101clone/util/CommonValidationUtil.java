package net.fullstack.class101clone.util;

public class CommonValidationUtil {

	public static boolean checkXSS(String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		String[] xssPatterns = {
				"<script>", "</script>",
				"&lt;script&gt;", "&lt;/script&gt;",
				"<img", "onerror=", "javascript:",
				"alert(", "<iframe", "<object"
		};
		for (String pattern : xssPatterns) {
			if (input.toLowerCase().contains(pattern.toLowerCase())) {
				return true; // XSS pattern found
			}
		}
		return false; // No XSS patterns found
	}
}
