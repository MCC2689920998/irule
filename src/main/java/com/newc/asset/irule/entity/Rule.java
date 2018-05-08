package com.newc.asset.irule.entity;

import lombok.Data;

@Data
public class Rule {
    private Factor[] factors;
    private Operator operator;
    private Conclusion conclusion;
}

