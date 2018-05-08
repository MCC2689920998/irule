package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.model.Example;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by paul on 2018/4/30.
 */
public class TypeFactoryTests {
    private TypeFactory typeFactory = TypeFactory.defaultInstance();

    @Test
    public void constructTypeWithJavaClass() {
        JavaType type = typeFactory.constructType(Example.class);
        assertThat(type, is(notNullValue()));
    }
}
