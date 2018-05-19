package com.ppmoney.asset.irule.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by paul on 2018/5/11.
 */
@Setter
@Getter
public class Algorithm {
    private String name;
    private JSONObject config;
}
