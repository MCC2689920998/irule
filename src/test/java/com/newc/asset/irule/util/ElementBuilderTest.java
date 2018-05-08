package com.newc.asset.irule.util;

import com.newc.asset.iframe.entity.Model;
import com.newc.asset.irule.entity.Factor;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by paul on 2018/5/1.
 */
public class ElementBuilderTest {
    @Test
    public void buildFactor() {
        Model model = new Model();
        model.put("id", "310115200001011713");

        Factor definition = new Factor();
        definition.setPath("id");

        FactorBuilder builder = FactorBuilder.defaultInstance();
        Object element = builder.build(model, definition);

        assertThat(element, is("310115200001011713"));
    }
}