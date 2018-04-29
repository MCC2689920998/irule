package com.newc.asset.irule.rule;

import com.newc.asset.irule.engine.RuleRunner;
import lombok.Data;

import java.util.List;

/**
 * Created by paul on 2018/4/28.
 */
@Data
public class Rule implements RuleRunner {
    private List<Factor> factors;
    private Algorithm algorithm;
    private Decision decision;
}

class Factor {

}
class Algorithm {}
class Decision {}

