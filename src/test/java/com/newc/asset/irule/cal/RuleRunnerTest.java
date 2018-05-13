package com.newc.asset.irule.cal;


import com.alibaba.fastjson.JSONObject;
import com.newc.asset.iframe.cache.GlobalCache;
import com.newc.asset.iframe.entity.Identity;
import com.newc.asset.iframe.util.Reflection;
import com.newc.asset.irule.def.Traversal;
import com.newc.asset.irule.entity.Rule;
import com.newc.asset.irule.exception.RuleExecutionException;
import com.newc.asset.itest.annotation.TestData;
import com.newc.asset.itest.data.TestDataLoader;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by paul on 2018/5/11.
 */
@TestData("classpath:data/test-data-set.json")
public class RuleRunnerTest {
    @Test @TestData(node = "object-example")
    public void shouldTraversalOnce() throws IOException {
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-sel");

        RuleRunner extractor = new RuleRunner(model, rule);
        Traversal<Object[]> traversal = getTraversal(extractor);

        assertThat(traversal.hasNext(), is(true));
        Object[] params = traversal.next();
        assertThat(params, is(notNullValue()));
        assertThat(params.length, is(24));

        // Now there is nothing remained.
        assertThat(traversal.hasNext(), is(false));
    }

    @Test @TestData(node = "array-example")
    public void dissectSelectSpecificArray() throws IOException {
        // Given
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-base");

        // When
        RuleRunner extractor = new RuleRunner(model, rule);
        Traversal<Object[]> traversal = getTraversal(extractor);

        // Then
        assertThat(traversal.hasNext(), is(true));
        Object[] params = traversal.next();
        assertThat(params, is(notNullValue()));
        assertThat(params.length, is(24));

        // Now there is nothing remained.
        assertThat(traversal.hasNext(), is(false));
    }

    @Test @TestData(node = "object-example")
    public void dissectNoSelectSpecific() throws IOException {
        // Given
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-sel-null");

        // When
        RuleRunner extractor = new RuleRunner(model, rule);
        Traversal<Object[]> traversal = getTraversal(extractor);

        assertThat(traversal.hasNext(), is(false));
    }

    @Test @TestData(node = "object-example")
    public void dissectSelectAll() throws IOException {
        // Given
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-reduce");

        // When
        RuleRunner extractor = new RuleRunner(model, rule);
        Traversal<Object[]> traversal = getTraversal(extractor);

        // Then
        assertThat(traversal.hasNext(), is(true));
        Object[] params = traversal.next();
        assertThat(params, is(notNullValue()));
        assertThat(params.length, is(48));

        // Now there is nothing remained.
        assertThat(traversal.hasNext(), is(false));
    }

    @Test @TestData(node = "object-example")
    public void dissectTraversal() throws IOException {
        // Given
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-traversal");

        // When
        RuleRunner extractor = new RuleRunner(model, rule);
        Traversal<Object[]> traversal = getTraversal(extractor);

        // Then
        assertThat(traversal.hasNext(), is(true));
        Object[] params = traversal.next();
        assertThat(params, is(notNullValue()));
        assertThat(params.length, is(24));

        // Now there is nothing remained.
        assertThat(traversal.hasNext(), is(true));
        params = traversal.next();
        assertThat(params, is(notNullValue()));
        assertThat(params.length, is(24));

        assertThat(traversal.hasNext(), is(false));
    }

    private Traversal<Object[]> getTraversal(RuleRunner extractor) {
        Field field = ReflectionUtils.findField(extractor.getClass(), "traversal");
        field.setAccessible(true);
        return (Traversal<Object[]>)ReflectionUtils.getField(field, extractor);
    }

    @Test @TestData(node = "object-example")
    public void sumSelectedPersonSalary() throws IOException, RuleExecutionException {
        // Given
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-sel-sum");
        RuleRunner runner = new RuleRunner(model, rule);

        // When
        runner.run(); // execute;

        // Then
        assertThat(model, is(notNullValue()));
        Object object = Reflection.get(model, "person.total");
        assertThat(object, is(notNullValue()));
        assertThat(object, is(new Double(24000)));

        GlobalCache.getInstance().remove((Identity)model.remove(GlobalCache.IdKey));
    }

    @Test @TestData(node = "object-example")
    public void sumAllSalary() throws IOException, RuleExecutionException {
        // Given
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-all-sum");
        RuleRunner runner = new RuleRunner(model, rule);

        // When
        runner.run(); // execute;

        // Then
        assertThat(model, is(notNullValue()));

        Object object = Reflection.get(model, "total");
        assertThat(object, is(notNullValue()));
        assertThat(object, is(new Double(72000)));

        GlobalCache.getInstance().remove((Identity)model.remove(GlobalCache.IdKey));
    }

    @Test @TestData(node = "object-example")
    public void sumSalaryOfEachPerson() throws IOException, RuleExecutionException {
        // Given
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-traversal-sum");
        RuleRunner runner = new RuleRunner(model, rule);

        // When
        runner.run(); // execute;

        // Then
        assertThat(model, is(notNullValue()));

        List<Object> result = Reflection.getAll(model, "person.total");
        assertThat(result, is(hasItem(new Double(24000))));
        assertThat(result, is(hasItem(new Double(48000))));

        GlobalCache.getInstance().remove((Identity)model.remove(GlobalCache.IdKey));
    }

    @Test @TestData(node = "object-example")
    public void sumSalaryAndStoreInDifferentNode() throws IOException, RuleExecutionException {
        // Given
        JSONObject model = TestDataLoader.loadTestData(JSONObject.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule-diferent-nodes-sum");
        RuleRunner runner = new RuleRunner(model, rule);

        // When
        runner.run(); // execute;

        // Then
        assertThat(model, is(notNullValue()));

        List<Object> result = Reflection.getAll(model, "financial.total");
        assertThat(result, is(hasItem(new Double(24000))));
        assertThat(result, is(hasItem(new Double(48000))));

        GlobalCache.getInstance().remove((Identity)model.remove(GlobalCache.IdKey));
    }
}