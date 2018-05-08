package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.model.Example;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by paul on 2018/4/30.
 */
public class DeserializationContextTests {
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();

        Example example = new Example("Paul Wong");
        JsonNode jsonNode = objectMapper.valueToTree(example);
        JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);

        deserializationContext = objectMapper.createDeserializationContext(jsonParser, objectMapper._deserializationConfig);
    }

    @Test
    public void checkHowToMappingValueDeserializer() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Example.class);
        JsonDeserializer<Object> jsonDeserializer = deserializationContext.findRootValueDeserializer(javaType);
        assertThat(jsonDeserializer, is(notNullValue()));
    }
}
