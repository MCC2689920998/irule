package com.ppmoney.asset.irule.define;

/**
 * Just a trait that indicate this can run one time.
 * 只有一个特征表明它可以运行一次
 */
public interface Runner {
    void run() throws Exception;
}
