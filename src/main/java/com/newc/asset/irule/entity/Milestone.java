package com.newc.asset.irule.entity;

import com.alibaba.fastjson.JSONObject;
import com.newc.asset.iframe.entity.Identity;
import com.newc.asset.iframe.util.IdentityBuilder;
import com.newc.asset.irule.util.BranchBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.newc.asset.iframe.util.IdentityBuilder.MetaIdentity;

/**
 * Milestone means a special point that we really care a lot.
 * This point is where we start to deeply search the value we want.
 */
@Getter
@Setter
public class Milestone {
    private String path = "";
    private Identity names = MetaIdentity;
    private Branch branch = null;

    /**
     * Setter method used by Jackson.
     */
    public void setKs(List<String> ks) {
        if (ks == null)  ks = new ArrayList<String>();
        names = IdentityBuilder.build(ks.toArray());
    }

    public void setVs(JSONObject vs) {
        branch = BranchBuilder.build(vs);
    }
}
