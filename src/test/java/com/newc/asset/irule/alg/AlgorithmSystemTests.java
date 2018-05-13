package com.newc.asset.irule.alg;

import com.alibaba.fastjson.JSONObject;
import com.newc.asset.irule.anno.AlgConfg;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by paul on 2018/5/12.
 */
@SpringBootApplication
public class AlgorithmSystemTests {
    static class AlgorithmSet {
        @AlgConfg("sum")
        public Double sum(Double... values) {
            double result = 0;
            for (int i=0; i<values.length; i++)
                result += values[i];
            return result;
        }

        @AlgConfg("sumTwo")
        public Double sumTwo(Double value1, Double value2) {
            return value1 + value2;
        }

        @AlgConfg(value="subStr", config={"begin", "length"})
        public String subStr(int begin, int length, String orginal) {
            return orginal.substring(begin, begin + length);
        }
    }

    private final DefaultConversionService conversionService = new DefaultConversionService();

    @Test
    public void checkSumValue() {
        Double x1 = 1.0;
        Double x2 = 2.0;

        AlgorithmSet set = new AlgorithmSet();

        Double v = set.sumTwo(x1, x2);
        System.out.println(v);

        v = set.sum(x1, x2);
        System.out.println(v);

        v = set.sum(x1, x2, 3.0);
        System.out.println(v);
    }

    @Test
    public void invokeAlgorithmWithoutConfig() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Object instance = newInstance();

        Method method = getAlgorithmMethod(instance, "sum");
        Object[] params = new Object[10];
        params[0] = 1;
        params[1] = 2;
        params[2] = "3";
        params[3] = 4;
        params[4] = 5;
        params[5] = "6";
        params[6] = 7;
        params[7] = 8;
        params[8] = 9;
        params[9] = 10;

        List<Object> objects = convertParams(method, params, 0);
        Object result = invoke(instance, method, objects.toArray());
        assertThat(result, is(new Double(55)));

        method = getAlgorithmMethod(instance, "sumTwo");
        objects = convertParams(method, params, 0);

        result = invoke(instance, method, objects.toArray());
        assertThat(result, is(new Double(3.0)));
    }

    @Test
    public void invokeAlgorithmWithConfig() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        JSONObject config = new JSONObject();
        config.put("begin", 1);
        config.put("length", 3);

        Object[] params = new Object[1];
        params[0] = "Hello World!";

        Object instance = newInstance();
        Method method = getAlgorithmMethod(instance, "subStr");

        List<Object> cfgList = convertConfig(method, config);
        List<Object> paramList = convertParams(method, params, cfgList.size());

        List<Object> invokeParams = new ArrayList<Object>();
        invokeParams.addAll(cfgList);
        invokeParams.addAll(paramList);

        Object result = invoke(instance, method, invokeParams.toArray());
        assertThat(result, is(notNullValue()));
        assertThat(result, is("ell"));
    }

    private Object newInstance() throws IllegalAccessException, InstantiationException {
        return AlgorithmSet.class.newInstance();
    }

    private Method getAlgorithmMethod(Object instance, String algName) {
        Class clazz = instance.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            AlgConfg config = method.getAnnotation(AlgConfg.class);
            if (config == null) continue;

            if (algName.equals(config.value())) return method;
        }

        return null;
    }

    private List<Object> convertConfig(Method method, JSONObject config) {
        List<Object> result = new ArrayList<Object>();

        AlgConfg algConfg = method.getDeclaredAnnotation(AlgConfg.class);
        Assert.notNull(algConfg, "Annotation is not exist!");

        Class[] types = method.getParameterTypes();

        String[] configs = algConfg.config();
        for (int i=0; i<configs.length; i++) {
            Object value = config.get(configs[i]);
            Object matched = conversionService.convert(value, types[i]);
            result.add(matched);
        }

        return result;
    }

    private List<Object> convertParams(Method method, Object[] params, int start) {
        Class[] types = method.getParameterTypes();
        Assert.isTrue(params.length >= types.length - start, "Input params is not enough!");

        List<Object> result = new ArrayList<Object>();
        for (int i=start; i<types.length - 1; i++) {
            result.add(conversionService.convert(params[i-start], types[i]));
        }

        result.add(convertAlterableType(types[types.length-1], params, types.length-1-start));

        return result;
    }

    private Object convertAlterableType(Class type, Object[] params, int begin) {
        Object[] objs = new Object[params.length - begin];
        System.arraycopy(params, begin, objs, 0, objs.length);

        return conversionService.convert(objs, type);
    }

    private Object invoke(Object instance, Method method, Object[] params) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, params);
    }
}
