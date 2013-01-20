/**
 * 
 */
package org.restq.server.utils;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

/**
 * @author ganeshs
 *
 */
public final class GsonUtil {

	public static final Gson createGson() {
		return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	}
	
	public static final Gson createGson(Type type, JsonDeserializer<Map> deserializer) {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(type, deserializer).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	}
}
