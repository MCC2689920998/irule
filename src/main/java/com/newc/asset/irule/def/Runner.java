package com.newc.asset.irule.def;

import com.newc.asset.irule.exception.AlgorithmException;

/**
 * Just a trait that indicate this can run one time.
 */
public interface Runner {
    void run() throws Exception;
}
