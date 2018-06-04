package com.ppmoney.asset.irule.entity;

import java.util.Collection;
import java.util.List;

/**
 * Branch is the path from `Milestone` to the value we really care about.
 * 分支 是指从“转折点”到我们真正关心的值的路径。
 */
public interface Branch {
    List<Object> collect(Collection<Object> content);
    void update(Collection<Object> content, Object values);
}
