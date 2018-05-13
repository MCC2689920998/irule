package com.newc.asset.irule.exception;

/**
 * Created by paul on 2018/5/12.
 */
public class RuleException extends Exception {
    public RuleException(String desc, Throwable th) {
        super(desc, th);
    }

    public RuleException(String desc) {
        super(desc);
    }
}
