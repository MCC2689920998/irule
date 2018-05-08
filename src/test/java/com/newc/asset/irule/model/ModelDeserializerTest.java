package com.newc.asset.irule.model;

import com.newc.asset.iframe.entity.Model;
import com.newc.asset.irule.model.example.SimpleModel;
import com.newc.asset.itest.annotation.TestData;
import com.newc.asset.itest.data.TestDataLoader;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by paul on 2018/4/29.
 */
@TestData("classpath:data/model.json")
public class ModelDeserializerTest {
    @Test
    @TestData(node = "simple_model")
    public void deserializeSimpleKeyValue() throws IOException {
        Model model = TestDataLoader.loadTestData(Model.class);
        assertThat(model, is(notNullValue()));
    }

    @Test
    @TestData(node = "simple_model")
    public void deserializeSimpleKeyValueAsPojoObject() throws IOException {
        SimpleModel model = TestDataLoader.loadTestData(SimpleModel.class);
        assertThat(model, is(notNullValue()));
    }
}