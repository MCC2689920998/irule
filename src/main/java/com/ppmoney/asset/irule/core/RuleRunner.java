package com.ppmoney.asset.irule.core;

import com.alibaba.fastjson.JSONObject;
import com.ppmoney.asset.iframe.cache.GlobalCache;
import com.ppmoney.asset.iframe.entity.Identity;
import com.ppmoney.asset.iframe.iter.Container;
import com.ppmoney.asset.irule.define.Runner;
import com.ppmoney.asset.irule.entity.Algorithm;
import com.ppmoney.asset.irule.entity.Branch;
import com.ppmoney.asset.irule.entity.Rule;
import com.ppmoney.asset.irule.entity.Milestone;
import com.ppmoney.asset.irule.exception.RuleExecutionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.util.*;

import static com.ppmoney.asset.iframe.cache.GlobalCache.IdKey;

/**
 * Created by paul on 2018/5/10.
 */
public class RuleRunner implements Runner {
    private static Log logger = LogFactory.getLog(RuleRunner.class);

    // The global property that will be used by all instance.
    private static GlobalCache cache = GlobalCache.getInstance();
    private static AlgorithmRegistry algorithmRegistry = AlgorithmRegistry.singleton();

    private JSONObject model = null;
    private Rule rule = null;
    private Identity cacheKey = null;
    private Container container = null;
    private ModelVisitor traversal = null;

    public RuleRunner(JSONObject model, Rule rule) {
        Assert.notNull(model, "model should not be null.");
        Assert.notNull(rule, "rule should not be null.");

        this.model = model;
        this.rule = rule;

        this.cacheKey = (Identity)model.get(IdKey);
        if (this.cacheKey == null) {
            this.cacheKey = cache.put(new Container(model));
            model.put(IdKey, this.cacheKey);
        }
        this.container = cache.get(this.cacheKey, Container.class);

        init();
    }

    private void init() {
        this.traversal = new ModelVisitor(this.container, rule.getValues());

        List<Milestone> milestones = rule.getSources();
        for (int i=0; i<milestones.size(); i++) {
            String path = milestones.get(i).getPath();
            Identity names = milestones.get(i).getNames();
            Branch branch = milestones.get(i).getBranch();

            this.traversal.update(path, names, branch);
        }

        this.traversal.setUp();
    }

    public void run() throws RuleExecutionException {
        try {
            while (traversal.hasNext()) {
                // Create the inputs for algorithm
                Object[] inputs = traversal.next();

                // Invoke the algorithm
                Algorithm algorithm = rule.getAlg();
                Object result = algorithmRegistry.call(algorithm.getName(), algorithm.getConfig(), inputs);

                // Update the model
                update(result);
            }
        } catch (Exception ex) {
            throw new RuleExecutionException("Error occurs when execute rule.", ex);
        }
    }

    private void update(Object record) throws RuleExecutionException {
        Milestone milestone = rule.getTarget(); // The place to store the result.
        Identity values = traversal.curval();

        Identity kvs = milestone.getNames().zip(values);
        Set<Object> content = container.search(milestone.getPath(), kvs, true);

        if (content == null) {
            throw new RuleExecutionException("Could not load the storage configuration for algorithm result.");
        }

        Branch branch = milestone.getBranch();
        branch.update(content, record);
    }
}
