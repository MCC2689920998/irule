package com.newc.asset.irule.def;

import com.newc.asset.iframe.entity.Model;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Created by paul on 2018/5/1.
 */
public abstract class Formula {
    private Object instance;
    private Method method;

    public Formula(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public Object calculate(Model config, Object[] elements) {
        return ReflectionUtils.invokeMethod(method, instance, config, elements);
    }
}
