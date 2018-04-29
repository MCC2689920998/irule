package com.newc.asset.irule.engine;

import com.newc.asset.irule.engine.impl.DefaultEngine;
import com.newc.asset.irule.model.Model;
import com.newc.asset.irule.rule.Rule;
import com.newc.asset.irule.rule.RuleSet;
import com.newc.asset.itest.data.TestDataLoader;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by paul on 2018/4/28.
 */
public class EngineTest {
    private Engine engine = new DefaultEngine();

    @Test
    public void execute() throws IOException {
        // 获取测试数据
        Model model = TestDataLoader.loadTestData(Model.class, "model");
        // 获取配置数据
        RuleSet ruleSet = TestDataLoader.loadTestData(RuleSet.class, "ruleSet");
        Model result = engine.execute(model, ruleSet);
        assertThat(result, is(notNullValue()));
    }
}