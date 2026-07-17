package com.smartroad.admin.util;

public final class HtmlUtil {

    private HtmlUtil() {
        // Prevent instantiation
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}