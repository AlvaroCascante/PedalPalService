package com.quetoquenana.pedalpal.util;

/**
 * Central place for small JSON payloads used in controller tests.
 * We intentionally handcraft PATCH payloads to avoid accidentally serializing missing fields as explicit nulls.
 */
public final class TestJsonBodies {

    private TestJsonBodies() {}

    public static String patchBikeName(String name) {
        // Keep it minimal: only the field being patched.
        return "{\"name\":\"" + escapeJson(name) + "\"}";
    }

    public static String createBikeMinimal(String name, String type) {
        return "{\"name\":\"" + escapeJson(name) + "\",\"type\":\"" + escapeJson(type) + "\"}";
    }

    public static String patchBikeStatus(String status) {
        return "{\"status\":\"" + escapeJson(status) + "\"}";
    }

    private static String escapeJson(String value) {
        if (value == null) return "";
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
