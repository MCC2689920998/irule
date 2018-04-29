package com.newc.asset.irule.rule;

import com.newc.asset.irule.engine.RuleRunner;
import com.newc.asset.irule.model.Model;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 2018/4/28.
 */
@Data
public class RuleSet extends ArrayList<Rule> implements RuleRunner {
    @Override
    public Model run(Model model) {
        Assert.isTrue(this.size() > 0, "Rule set is empty.");

        this.forEach(it -> it.run(model));
        return model;
    }
}
