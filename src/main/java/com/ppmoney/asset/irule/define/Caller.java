package com.ppmoney.asset.irule.define;

/**
 * This abstraction represents anything that can be called with `params`, and the procedure name is `name`
 * 这个抽象表示任何可以用“params”调用的东西，而过程名是“name”
 * The return type `R` is a GenericType.
 *他返回类型“R”是一个泛型。
 * Created by paul on 2018/5/12.
 */
public interface Caller<R> {
    R call(Object... params) throws Exception;
}
