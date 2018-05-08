package com.newc.asset.irule.cal;

import com.newc.asset.irule.def.Formula;
import com.newc.asset.irule.util.FormulaFactory;
import com.newc.asset.irule.def.Executable;
import com.newc.asset.irule.entity.Operator;
import com.newc.asset.irule.entity.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Created by paul on 2018/5/8.
 */
public class AlgorithmEngine implements Executable<Object[], Operator, Object> {
    private static final Log logger = LogFactory.getLog(AlgorithmEngine.class);

    // Singleton Implementation.
    private static AlgorithmEngine instance = null;
    public static final AlgorithmEngine defaultExecutor() {
        if (instance ==  null) {
            synchronized (AlgorithmEngine.class) {
                if (instance == null) { // Double check.
                    instance = new AlgorithmEngine();
                }
            }
        }

        return instance;
    }

    private FormulaFactory formulaFactory; // Manage all formulas.
    private AlgorithmEngine() {
        formulaFactory = FormulaFactory.defaultFactory();
    }


    @Override
    public Object execute(Object[] elements, Operator operator) {
        Assert.notNull(elements, "Factors should not be null.");
        Assert.notNull(operator, "Operators should not be null.");

        String algorithmName = operator.getName();
        Formula formula = formulaFactory.get(algorithmName);
        if (formula == null) {
            logger.error("The alg " + algorithmName + " does not exists!");
            return null;
        }

        return formula.calculate(operator.getConfig(), elements);
    }
}
