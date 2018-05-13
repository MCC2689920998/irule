package com.newc.asset.irule.exception;

import com.newc.asset.irule.entity.Algorithm;

/**
 * Created by paul on 2018/5/12.
 */
public class AlgorithmException extends Exception {
    public AlgorithmException() {
        super();
    }

    public AlgorithmException(String desc) {
        super(desc);
    }

    public AlgorithmException(String desc, Throwable th) {
        super(desc, th);
    }
}
