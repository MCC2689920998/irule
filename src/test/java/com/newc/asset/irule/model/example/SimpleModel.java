package com.newc.asset.irule.model.example;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import lombok.Data;

import java.util.HashMap;

/**
 * Created by paul on 2018/4/30.
 */
@Data
public class SimpleModel {
    private String key;
}
