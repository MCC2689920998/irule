package com.newc.asset.irule.engine;

import com.newc.asset.irule.model.Model;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

/**
 * Created by paul on 2018/4/28.
 */
public interface RuleRunner {
    Log logger = LogFactory.getLog(RuleRunner.class);

    default Model run(Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Be careful, rule is running with default implementation");
        }
        return model;
    }
}
