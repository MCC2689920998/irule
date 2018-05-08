package com.fasterxml.jackson.databind;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.model.MapExample;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by paul on 2018/4/30.
 */
public class JSONObjectMapperTests {
    private ObjectMapper objectMapper;
    private JSONObject testData;
    private JsonNode jsonNode;
    private JsonParser jsonParser;
    private DeserializationConfig cfg;
    private DeserializationContext ctx;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();

        testData = new JSONObject();
        testData.put("name", "Paul Wong");

        jsonNode = objectMapper.valueToTree(testData);
        jsonParser = objectMapper.treeAsTokens(jsonNode);

        cfg = objectMapper.getDeserializationConfig();
        ctx = objectMapper.createDeserializationContext(null, cfg);
    }


    @Test
    public void deserializeJSONObject() throws JsonProcessingException {
        MapExample example = objectMapper.treeToValue(jsonNode, MapExample.class);
        assertThat(example, is(notNullValue()));
    }

    @Test
    public void deserializeJSONObjectWithReadValue() throws IOException {
        Object result = objectMapper.readValue(jsonParser, MapExample.class);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void deserializeJSONObjectWith_ReadValue() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(MapExample.class);

        Object result = objectMapper._readValue(objectMapper.getDeserializationConfig(), jsonParser, javaType);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void deserializeJSONObjectWithMapDeserializer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(MapExample.class);

        objectMapper._initForReading(jsonParser, javaType);
        JsonDeserializer<Object> deser = ctx.findRootValueDeserializer(javaType);
        Object result = deser.deserialize(jsonParser, ctx);
        assertThat(result, is(notNullValue()));
    }
}
