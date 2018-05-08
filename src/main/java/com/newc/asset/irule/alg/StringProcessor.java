package com.newc.asset.irule.alg;

import com.newc.asset.iframe.entity.Model;
import com.newc.asset.irule.anno.Algorithm;
import org.springframework.util.Assert;

/**
 * Created by paul on 2018/5/1.
 */
public class StringProcessor {
    @Algorithm("substring")
    public String substring(Model parameter, Object... input) {
        Assert.notNull(parameter, "parameter should not be null.");
        Assert.notNull(input, "input should not be empty.");
        Assert.isTrue(input.length == 1, "substring can only handle one input string.");
        Assert.isInstanceOf(String.class, input[0], "input should be a string.");

        return _substring((String)input[0], parameter.getInteger("begin"), parameter.getInteger("length"));
    }

    private final String _substring(String input, int begin, int length) {
        return input.substring(begin, begin + length);
    }
}
