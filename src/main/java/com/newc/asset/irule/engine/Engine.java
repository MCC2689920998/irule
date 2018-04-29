package com.newc.asset.irule.engine;

import com.newc.asset.irule.model.Model;
import com.newc.asset.irule.rule.Rule;
import com.newc.asset.irule.rule.RuleSet;

/**
 * Created by paul on 2018/4/28.
 */
public interface Engine {
    Model execute(Model model, RuleSet ruleSet);
}
