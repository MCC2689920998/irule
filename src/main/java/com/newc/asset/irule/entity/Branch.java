package com.newc.asset.irule.entity;

import java.util.Collection;
import java.util.List;

/**
 * Branch is the path from `Milestone` to the value we really care about.
 */
public interface Branch {
    List<Object> collect(Collection<Object> content);
    void update(Collection<Object> content, Object values);
}
