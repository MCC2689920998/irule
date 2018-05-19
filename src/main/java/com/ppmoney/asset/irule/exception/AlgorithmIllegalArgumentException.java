package com.ppmoney.asset.irule.exception;

/**
 * Created by paul on 2018/5/12.
 */
public class AlgorithmIllegalArgumentException extends AlgorithmException {
    public AlgorithmIllegalArgumentException() {
        super();
    }

    public AlgorithmIllegalArgumentException(String desc) {
        super(desc);
    }

    public AlgorithmIllegalArgumentException(String desc, Throwable th) {
        super(desc, th);
    }
}
