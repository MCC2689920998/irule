package com.ppmoney.asset.irule.define;

/**
 * This abstraction represents anything that can be called with `params`, and the procedure name is `name`
 * The return type `R` is a GenericType.
 * Created by paul on 2018/5/12.
 */
public interface Caller<R> {
    R call(Object... params) throws Exception;
}
