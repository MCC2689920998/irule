package com.newc.asset.irule.util;

import com.newc.asset.irule.def.Formula;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by paul on 2018/5/1.
 */
public class FormulaFactoryTest {
    @Test
    public void shouldRegisterAllAlgorithms() throws IOException {
        FormulaFactory factory = FormulaFactory.defaultFactory();
        Formula formula = factory.get("substring");
        assertThat(formula, is(notNullValue()));
    }
}