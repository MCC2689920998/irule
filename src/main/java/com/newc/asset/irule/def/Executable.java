package com.newc.asset.irule.def;

/**
 * The interface that indicate the ability of execute something and return the original model added some result.
 */
public interface Executable<I, R, O> {
    O execute(I input, R rule);
}
