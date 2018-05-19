package com.ppmoney.asset.irule.core;

import com.alibaba.fastjson.JSONObject;
import com.ppmoney.asset.iframe.util.ConversionService;
import com.ppmoney.asset.irule.annotation.AlgConfg;
import com.ppmoney.asset.irule.define.Caller;
import com.ppmoney.asset.irule.exception.AlgorithmExecutionException;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 2018/5/12.
 */
public final class CallableAlgorithm implements Caller<Object> {
    private String name; // The algorithm name;
    private Object instance; // The instance of the algorithm;
    private AlgConfg algConfg; // The annotation information
    private Method method; // The method that will be invoked.

    public CallableAlgorithm(Object instance, Method method) {
        Assert.notNull(instance, "instance should not be null.");
        Assert.notNull(method, "method should not be null.");

        this.instance = instance;
        this.method = method;

        // now we use method to get name and AlgConfig annotation
        this.algConfg = method.getDeclaredAnnotation(AlgConfg.class);
        Assert.notNull(this.algConfg, "Fail to get the annotation on the method.");

        this.name = this.algConfg.value();
    }

    @Override
    public Object call(Object... params) throws Exception {
        Assert.notEmpty(params, "params should at least have one element.");
        return call(createConfig(params), createInputs(params));
    }

    private JSONObject createConfig(Object... params) {
        if (containsConfig(params)) return (JSONObject)params[0];

        return null;
    }

    private Object[] createInputs(Object... params) {
        if (!containsConfig(params)) return params;

        Object[] inputs = new Object[params.length - 1];
        System.arraycopy(params, 1, inputs, 0, inputs.length);

        return inputs;
    }

    private boolean containsConfig(Object... params) {
        return params[0] instanceof JSONObject;
    }

    /**
     * This method is the really function that will using reflection to call the method that is annotated with
     * `AlgConfig`, the return Object is actually the origin method's return.
     * @param config
     * @param params
     * @return
     * @throws AlgorithmExecutionException
     */
    public Object call(JSONObject config, Object... params) throws AlgorithmExecutionException {
        try {
            Object[] args = createArgs(config, params);
            return method.invoke(instance, args);
        } catch (Exception ex) {
            throw new AlgorithmExecutionException("Execute algorithm error.", ex);
        }
    }

    private Object[] createArgs(JSONObject config, Object... params) {
        List<Object> args = new ArrayList<Object>();

        args.addAll(convertConfig(config));
        args.addAll(convertParams(params));

        return args.toArray();
    }

    private List<Object> convertConfig(JSONObject config) {
        List<Object> result = new ArrayList<Object>();

        AlgConfg algConfg = method.getDeclaredAnnotation(AlgConfg.class);
        Assert.notNull(algConfg, "Annotation is not exist!");

        Class[] types = method.getParameterTypes();

        String[] configs = algConfg.config();
        for (int i=0; i<configs.length; i++) {
            Object value = config.get(configs[i]);
            Object matched = ConversionService.convert(value, types[i]);
            result.add(matched);
        }

        return result;
    }

    private List<Object> convertParams(Object[] params) {
        Class[] types = method.getParameterTypes();
        int start = algConfg.config().length;

        Assert.isTrue(params.length >= types.length - start, "Input params is not enough!");

        List<Object> result = new ArrayList<Object>();
        for (int i=start; i<types.length - 1; i++) {
            result.add(ConversionService.convert(params[i-start], types[i]));
        }

        result.add(convertAlterableType(params, types.length-1-start));

        return result;
    }

    private Object convertAlterableType(Object[] params, int begin) {
        Class[] types = method.getParameterTypes();
        Class type = types[types.length - 1];

        Object[] inputs = new Object[params.length - begin];
        System.arraycopy(params, begin, inputs, 0, inputs.length);

        return ConversionService.convert(inputs, type);
    }
}