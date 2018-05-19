package com.ppmoney.asset.irule.exception;

/**
 * Created by paul on 2018/5/12.
 */
public class AlgorithmNotFoundException extends AlgorithmException{
    public AlgorithmNotFoundException() {
        super();
    }

    public AlgorithmNotFoundException(String desc) {
        super(desc);
    }

    public AlgorithmNotFoundException(String desc, Throwable th) {
        super(desc, th);
    }
}
