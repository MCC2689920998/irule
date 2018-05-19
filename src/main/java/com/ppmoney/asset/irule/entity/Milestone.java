package com.ppmoney.asset.irule.entity;

import com.alibaba.fastjson.JSONObject;
import com.ppmoney.asset.iframe.entity.Identity;
import com.ppmoney.asset.iframe.util.IdentityBuilder;
import com.ppmoney.asset.irule.util.BranchBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static com.ppmoney.asset.iframe.util.IdentityBuilder.MetaIdentity;

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
