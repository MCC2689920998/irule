package com.newc.asset.irule.alg;

import com.newc.asset.irule.anno.AlgConfg;
import org.springframework.util.Assert;

/**
 * Created by paul on 2018/5/11.
 */
public class NumericHandler {
    @AlgConfg("sum")
    public Double sum(Double... param) {
        Assert.notNull(param, "param should not be null.");
        double total = 0;
        for (int i = 0; i < param.length; i++) {
            total += param[i];
        }
        return total;
    }
}
