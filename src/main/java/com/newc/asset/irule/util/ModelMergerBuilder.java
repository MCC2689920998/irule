package com.newc.asset.irule.util;

import com.newc.asset.irule.cal.ModelMerger;
import com.newc.asset.irule.entity.Conclusion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by paul on 2018/5/8.
 */
public class ModelMergerBuilder {
    private static final Log logger = LogFactory.getLog(ModelMergerBuilder.class);

    private static ModelMergerBuilder instance = null;
    public static ModelMergerBuilder defaultBuilder() {
        if (instance == null) {
            synchronized (ModelMergerBuilder.class) {
                if (instance == null) { // Default check.
                    instance = new ModelMergerBuilder();
                }
            }
        }

        return instance;
    }

    public ModelMerger build(Conclusion conclusion) {
        return new ModelMerger(conclusion);
    }
}
