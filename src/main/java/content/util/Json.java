package content.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * Thin JSON utility backed by Gson.
 */
public final class Json {

    private static final Gson GSON = new GsonBuilder().create();

    private Json() {}

    /** Parse a JSON string into a Map suitable for template expansion. */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parse(String json) {
        return GSON.fromJson(json, Map.class);
    }

    /** Serialize an object to JSON. */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }
}
