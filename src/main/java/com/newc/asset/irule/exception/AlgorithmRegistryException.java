package com.newc.asset.irule.exception;

/**
 * Created by paul on 2018/5/8.
 */
public class AlgorithmRegistryException extends AlgorithmException {
    public AlgorithmRegistryException() {
        super();
    }

    public AlgorithmRegistryException(String desc) {
        super(desc);
    }

    public AlgorithmRegistryException(String desc, Throwable th) {
        super(desc, th);
    }
}
