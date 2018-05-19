package com.ppmoney.asset.irule.exception;

/**
 * Created by paul on 2018/5/12.
 */
public class RuleExecutionException extends RuleException {
    public RuleExecutionException(String desc, Throwable th) {
        super(desc, th);
    }

    public RuleExecutionException(String desc) {
        super(desc);
    }
}
