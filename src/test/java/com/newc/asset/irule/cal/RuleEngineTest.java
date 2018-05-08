package com.newc.asset.irule.cal;

import com.alibaba.fastjson.JSONObject;
import com.newc.asset.iframe.entity.Model;
import com.newc.asset.irule.entity.Rule;
import com.newc.asset.itest.annotation.TestData;
import com.newc.asset.itest.data.TestDataLoader;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by paul on 2018/4/30.
 */
@TestData("classpath:data/rule_runner.json")
public class RuleEngineTest {
    private RuleEngine engine = RuleEngine.defaultExecutor();

    @Test
    @TestData(node = "birthday")
    public void findBirthdayFromPersonId() throws IOException {
        Model model = TestDataLoader.loadTestData(Model.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule");

        model = engine.execute(model, rule);
        String birthday = model.getString("birthday");
        assertThat(birthday, is("20000101"));
    }

    @Test
    @TestData(node = "model_contains_entity")
    public void findBirthdayFromModelContainsEntity() throws IOException {
        // Gien
        Model model = TestDataLoader.loadTestData(Model.class, "model");
        Rule rule = TestDataLoader.loadTestData(Rule.class, "rule");

        // When
        model = engine.execute(model, rule);

        // Then
        assertThat(model, is(notNullValue()));

        JSONObject person = model.getJSONObject("person");
        assertThat(person, is(notNullValue()));

        String birthday = person.getString("birthday");
        assertThat(birthday, is("20000101"));
    }
}