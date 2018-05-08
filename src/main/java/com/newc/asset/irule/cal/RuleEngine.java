package com.newc.asset.irule.cal;

import com.newc.asset.iframe.entity.Model;
import com.newc.asset.irule.def.Executable;
import com.newc.asset.irule.entity.*;
import com.newc.asset.irule.util.FactorBuilder;
import com.newc.asset.irule.util.ModelMergerBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Created by paul on 2018/4/30.
 */
public final class RuleEngine implements Executable<Model, Rule, Model> {
    private static final Log logger = LogFactory.getLog(RuleEngine.class);

    private static RuleEngine instance = null;
    public static final RuleEngine defaultExecutor() {
        if (instance == null) {
            synchronized (RuleEngine.class) {
                if (instance == null) { // Double check.
                    instance = new RuleEngine();
                }
            }
        }

        return instance;
    }

    private AlgorithmEngine algorithmEngine = null; // Using to calculate operation.
    private ModelMergerBuilder mergerBuilder = null; // USing to merger conclusion into model.

    /***
     * Private Construction.
     */
    private RuleEngine() {
        algorithmEngine = AlgorithmEngine.defaultExecutor();
        mergerBuilder = ModelMergerBuilder.defaultBuilder();
    }

    @Override
    public Model execute(Model model, Rule definition) {
        Assert.notNull(definition, "Rule should not be null.");
        Factor[] factor = definition.getFactors();

        Object[] elements = generatorFactors(model, factor);
        Operator operator = definition.getOperator();

        Object operation = algorithmEngine.execute(elements, operator);
        Conclusion conclusion = definition.getConclusion();

        ModelMerger merger = mergerBuilder.build(conclusion);

        return merger.execute(model, operation);
    }

    private Object[] generatorFactors(Model model, Factor[] definitions) {
        Assert.notNull(model, "model should not be null.");
        Assert.notNull(definitions, "indicator should not be null.");

        Object[] elements = new Element[definitions.length];
        for (int i=0; i<definitions.length; i++)
            elements[i] = generatorFactor(model, definitions[i]);

        return elements;
    }

    private Object generatorFactor(Model model, Factor definition) {
        FactorBuilder builder = FactorBuilder.defaultInstance();
        return builder.build(model, definition);
    }
}
