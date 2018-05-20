package com.ppmoney.asset.irule.entity;

import com.ppmoney.asset.iframe.cache.GlobalCache;
import com.ppmoney.asset.iframe.entity.Identity;
import com.ppmoney.asset.iframe.util.IdentityBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static com.ppmoney.asset.iframe.util.IdentityBuilder.MetaIdentity;

/**
 * Rule means we will use it to find the correct path to the target we want to go.
 * Created by paul on 2018/5/9.
 */
@Setter
@Getter
public class Rule {
    private static final Log logger = LogFactory.getLog(Rule.class);
    private static final GlobalCache cache = GlobalCache.getInstance();

    private Identity values = MetaIdentity;

    private List<Milestone> sources = new ArrayList<Milestone>();
    private Algorithm alg = null; // Jackson will set this property with default Setter method.
    private Milestone target = null; // Jackson will set this property with default Setter method.

    /**
     * Set method used by jackson.
     */
    public void setSel(List<Object> sel) {
        if (sel == null) sel = new ArrayList<Object>();
        values = IdentityBuilder.build(sel.toArray());
    }

    public void setNodes(List<Milestone> nodes) {
        Assert.notNull(nodes, "nodes should not be null.");
        this.sources = nodes;
    }
}
