package com.newc.asset.irule.def;

import java.util.List;

/**
 * Created by paul on 2018/5/10.
 */
public interface Traversal<T> {
    boolean hasNext();
    T next();
}
