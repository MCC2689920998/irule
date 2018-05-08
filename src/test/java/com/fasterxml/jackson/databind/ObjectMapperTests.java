package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.model.Example;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * The test suite that illustrate how ObjectMapper works
 * Created by paul on 2018/4/30.
 */
public class ObjectMapperTests {
    private ObjectMapper objectMapper;
    private JsonNode testNode;
    private JsonParser parser;
    private DeserializationConfig deserializationConfig;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        testNode = createJsonNode();
        parser = objectMapper.treeAsTokens(testNode);
        deserializationConfig = objectMapper.getDeserializationConfig();

        deserializationContext = objectMapper.createDeserializationContext(parser, deserializationConfig);
    }

    /**
     * Construct test data.
     */
    private JsonNode createJsonNode() {
        Example model = new Example();
        model.setName("value");

        return objectMapper.valueToTree(model);
    }

    /**
     * Test process steps.
     */
    // Run the treeToValue method that construct a POJO object using JsonNode
    @Test
    public void deserializeJsonNodeWithTreeToValue() throws JsonProcessingException {
        Example result = objectMapper.treeToValue(testNode, Example.class);
        assertThat(result.getName(), is("value"));
    }

    @Test
    public void checkDefaultDeserializationConfig() {
        assertThat(deserializationConfig, is(notNullValue()));
    }

    // Create deserialize context
    @Test
    public void checkCreateDeserializeContext() {
        assertThat(deserializationContext, is(notNullValue()));
    }
}
