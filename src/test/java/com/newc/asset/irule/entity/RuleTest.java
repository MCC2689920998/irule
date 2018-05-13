package com.newc.asset.irule.entity;

import com.newc.asset.itest.annotation.TestData;
import com.newc.asset.itest.data.TestDataLoader;
import org.junit.Test;

import java.io.IOException;

import static com.newc.asset.iframe.util.IdentityBuilder.MetaIdentity;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by paul on 2018/5/9.
 */
@TestData("classpath:data/test-data-set.json")
public class RuleTest {
    @Test @TestData(node = "array-example")
    public void deserializeRule() throws IOException {
        // When
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-base");
        // Then
        assertThat(rule, is(notNullValue()));
    }

    @Test @TestData(node = "array-example")
    public void deserializeNoSelRule() throws IOException {
        // When
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-no-sel");
        // Then
        assertThat(rule, is(notNullValue()));
        assertThat(rule.getValues(), is(MetaIdentity));
    }

    @Test @TestData(node = "array-example")
    public void deserializeTraversalRule() throws IOException {
        // When
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-traversal-type");
        // Then
        assertThat(rule, is(notNullValue()));
    }

    @Test @TestData(node = "object-example")
    public void deserializeSelSumRule() throws IOException {
        // When
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-sel-sum");

        // Then
        assertThat(rule, is(notNullValue()));
    }
}