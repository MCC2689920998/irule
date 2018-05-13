package com.newc.asset.irule.exception;

/**
 * Created by paul on 2018/5/12.
 */
public class AlgorithmExecutionException extends AlgorithmException {
    public AlgorithmExecutionException() {
        super();
    }

    public AlgorithmExecutionException(String desc) {
        super(desc);
    }

    public AlgorithmExecutionException(String desc, Throwable th) {
        super(desc, th);
    }
}
