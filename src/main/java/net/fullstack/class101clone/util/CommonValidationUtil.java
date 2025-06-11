package net.fullstack.class101clone.util;

public class CommonValidationUtil {
	public static boolean checkSQLInjection(String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		String[] sqlInjectionPatterns = {
			"'", "\"", "--", ";", "/*", "*/", "xp_", "exec", "select", "insert", "update", "delete", "drop", "alter"
		};
		for (String pattern : sqlInjectionPatterns) {
			if (input.toLowerCase().contains(pattern.toLowerCase())) {
				return true; // SQL Injection pattern found
			}
		}
		return false; // No SQL Injection patterns found
	}

	public static boolean checkXSS(String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		String[] xssPatterns = {
			"<script>", "</script>", "<img", "onerror=", "javascript:", "alert(", "<iframe", "<object"
		};
		for (String pattern : xssPatterns) {
			if (input.toLowerCase().contains(pattern.toLowerCase())) {
				return true; // XSS pattern found
			}
		}
		return false; // No XSS patterns found
	}
}
