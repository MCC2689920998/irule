package com.newc.asset.irule.engine.impl;

import com.newc.asset.irule.engine.Engine;
import com.newc.asset.irule.model.Model;
import com.newc.asset.irule.rule.Rule;
import com.newc.asset.irule.rule.RuleSet;
import org.springframework.util.Assert;

/**
 * Created by paul on 2018/4/28.
 */
public class DefaultEngine implements Engine {
    @Override
    public Model execute(Model model, RuleSet ruleSet) {
        Assert.notNull(model, "Model should not be null.");
        Assert.notNull(ruleSet, "Rule set should not be null.");
        return ruleSet.run(model);
    }
}
