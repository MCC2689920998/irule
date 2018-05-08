package com.newc.asset.irule.util;

import com.newc.asset.iframe.entity.Model;
import com.newc.asset.irule.entity.Factor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Created by paul on 2018/4/30.
 */
public final class FactorBuilder {
    private static final Log logger = LogFactory.getLog(FactorBuilder.class);

    // Singleton Implementation
    private static FactorBuilder instance = null;
    public static final FactorBuilder defaultInstance() {
        if (instance == null) {
            synchronized (FactorBuilder.class) {
                if (instance == null) {
                    instance = new FactorBuilder();
                }
            }
        }

        return instance;
    }

    private FactorBuilder() {

    }

    public Object build(Model model, Factor definition) {
        Assert.notNull(model, "model should not be null.");
        Assert.notNull(definition, "factor def should not be null.");

        String path = definition.getPath();

        Object value = model.get(path);

        return value;
    }
}
