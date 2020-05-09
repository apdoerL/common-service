package org.apdoer.common.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * JSON工具类，用于JSON与对象之间的相互转换。
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper mapper;

    /**
     * 将对象转换为JSON字符串。
     */
    public static String toJson(Object o) {
        init();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("object to json failed:", e);
            throw new RuntimeException("object to json failed");
        }

    }

    public static <T> T toObject(String json, Class<T> type) {
        init();
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log.error("son to Object failed:", e);
            throw new RuntimeException("json to Object failed");
        }
    }

    /**
     * 将 Map 转换为 javaBean
     *
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T mapToObj(Map map, Class<T> clazz) {
        init();
        return mapper.convertValue(map, clazz);
    }

    /**
     * 初始化mapper，不需要synchronized，多次初始化不影响正确性。
     */
    private static void init() {
        if (mapper == null) {
            ObjectMapper m = new ObjectMapper();
            m.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//			m.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//			m.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//			m.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
//			m.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
//			m.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
//			m.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
            m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            m.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
//			m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper = m;
        }
    }
}
