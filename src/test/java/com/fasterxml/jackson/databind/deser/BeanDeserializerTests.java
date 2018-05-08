package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.model.Example;
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
public class BeanDeserializerTests {
    private JsonParser jsonParser;
    private DeserializationConfig deserializationConfig;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<Object> deserializer;

    @Before
    public void setUp() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Example example = new Example("Paul Wong");
        JsonNode jsonNode = objectMapper.valueToTree(example);
        jsonParser = objectMapper.treeAsTokens(jsonNode);

        TypeFactory factory = TypeFactory.defaultInstance();
        JavaType javaType = factory.constructType(Example.class);

        deserializationConfig = objectMapper.getDeserializationConfig();
        deserializationContext = ((DefaultDeserializationContext)objectMapper.getDeserializationContext()).createInstance(deserializationConfig, jsonParser, null);
        deserializer = deserializationContext.findRootValueDeserializer(javaType);
    }


    @Test
    public void checkBeanDeserialize() throws IOException {
        deserializationConfig.initialize(jsonParser); // since 2.5
        if (jsonParser.getCurrentToken() == null)
            jsonParser.nextToken();

        Object result = deserializer.deserialize(jsonParser, deserializationContext);
        assertThat(result, is(notNullValue()));
    }
}
