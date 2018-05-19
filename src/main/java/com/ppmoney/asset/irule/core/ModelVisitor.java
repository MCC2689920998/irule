package com.ppmoney.asset.irule.core;

import com.ppmoney.asset.iframe.entity.Identity;
import com.ppmoney.asset.iframe.entity.JoinPoint;
import com.ppmoney.asset.iframe.entity.ZipSide;
import com.ppmoney.asset.iframe.iter.Container;
import com.ppmoney.asset.irule.define.Traversal;
import com.ppmoney.asset.irule.entity.Branch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.ppmoney.asset.iframe.util.IdentityBuilder.MetaIdentity;
import static com.ppmoney.asset.iframe.util.IdentityBuilder.VoidIdentity;

/**
 * Created by paul on 2018/5/11.
 */
public class ModelVisitor implements Traversal<Object[]> {
    private static Log logger = LogFactory.getLog(ModelVisitor.class);

    private Container container = null;
    private Identity values = null;

    // This virtual point is just indicate that we have a lot of point with path equals `JoinPoint.path` and with names equals `JoinPoint.kvs.unzip(left)`
    // In order to match the definition of JoinPoint, we let names zip with itself, this means that `JoinPoint.path.unzip(right)` have no meanings at all.
    private Set<JoinPoint> virtualPoints = null;

    //We use this to save the leaf node key that we want to extract it's value and return back.
    private Map<JoinPoint, Branch> branches = null;
    // This is the core object that used to traversal, every Identity indicate that we have a set JointPoint that should visit.
    // The value is with type of Set<JoinPoint>, which is actually the parameter for `container.search(path, kvs)`
    private Map<Identity, Set> core = null;
    private Iterator<Identity> _iterator = null; // The target iterator

    // This property is always changing when invoke `next`,
    // When `next` is invoked, we will try to get another values that has not been visited right now
    // then we return all the params and save the values with `curval` property.
    private Identity curval = null;

    public ModelVisitor(Container container, Identity values) {
        Assert.notNull(container, "container should not be null.");
        Assert.notNull(values, "values should not be null.");

        this.container = container;
        this.values = values;

        this.branches = new ConcurrentHashMap<JoinPoint, Branch>();
        this.virtualPoints = new HashSet<JoinPoint>();
    }

    public void update(String path, Identity names, Branch branch) {
        Assert.notNull(path, "path should not be null.");
        Assert.notNull(names, "names should not be null.");
        Assert.notNull(branch, "branch should not be null.");

        // values is MetaIdenitity if and only if all the names are MetaIdentity
        if (values == MetaIdentity) {
            if (names != MetaIdentity) {
                values = VoidIdentity; // This means we do not care about what values is.
            }
        }

        JoinPoint virtualPoint = new JoinPoint(path, names.zip(names));
        if (virtualPoints.contains(virtualPoint)) {
            // This means the operation has been doen before.
            if (logger.isDebugEnabled()) {
                logger.debug("The update with same path and names has been invoked more than one times! " + virtualPoint);
            }
            return ;
        }

        virtualPoints.add(virtualPoint);
        branches.put(virtualPoint, branch);

        // Update the container structure.
        container.update(path, names);
    }

    public void setUp() {
        this.core = new ConcurrentHashMap<Identity, Set>();

        for (JoinPoint virtualPoint : virtualPoints) {
            String path = virtualPoint.getPath();
            Set<Identity> identities = container.getIdentities(path);
            if (identities == null || identities.size() == 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("We have not found any identities for path: " + path);
                }
                continue;
            }

            _generateCore(path, identities);
        }

        // The last, we generate the iterator that will be used.
        this._iterator = this.core.keySet().iterator();
    }

    private void _generateCore(String path, Set<Identity> identities) {
        for (Identity kvs : identities) {
            if (kvs == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("We have got an null kvs in identities...  ");
                }

                continue;
            }
            _generateCore(path, kvs);
        }
    }

    private void _generateCore(String path, Identity kvs) {
        Identity values = kvs.unzip(ZipSide.Right);

        // This means the values is not match
        if (!valuesIsEqual(values)) return ;

        Set<JoinPoint> joinPoints = _search(values);

        joinPoints.add(new JoinPoint(path, kvs));
    }

    private boolean valuesIsEqual(Identity values) {
        if (this.values == values) return true; // The same thing.

        if (this.values == VoidIdentity && values == MetaIdentity) return false;
        if (this.values == MetaIdentity && values == VoidIdentity) return false;

        return this.values.equals(values);
    }

    private Set<JoinPoint> _search(Identity values) {
        Set<JoinPoint> joinPoints = core.get(values);
        if (joinPoints != null) return joinPoints;

        core.put(values, new HashSet<JoinPoint>());
        return core.get(values);
    }


    @Override
    public boolean hasNext() {
        Assert.notNull(_iterator, "The _iterator should not be null, please check if setUp has been invoked before.");
        return _iterator.hasNext();
    }

    @Override
    public Object[] next() {
        curval = _iterator.next();
        Assert.notNull(values, "values should not be null.");

        Set<JoinPoint> joinPoints = core.get(curval);
        Assert.notNull(joinPoints, "JoinPoint set should not be null.");

        List<Object> collector = new ArrayList<Object>();

        for (JoinPoint joinPoint : joinPoints) {
            String path = joinPoint.getPath();
            Identity kvs = joinPoint.getKvs();

            // TODO will be remove later
            // We assert that curval is equal to kvs.unzip(right)
            Assert.isTrue(curval.equals(kvs.unzip(ZipSide.Right)), "Please check why values are different!");

            Set<Object> content = container.search(path, kvs);
            Assert.notNull(content , "content should not be null.");

            Identity names = kvs.unzip(ZipSide.Left);

            Branch branch = _getBranch(joinPoint);
            Assert.notNull(branch, "branch should not be null.");
            List<Object> partial = branch.collect(content);
            collector.addAll(partial);
        }

        return collector.toArray();
    }

    private Branch _getBranch(JoinPoint joinPoint) {
        Assert.notNull(joinPoint, "joinPoint should not be null.");

        // Let's first create a virtualPoint
        String path = joinPoint.getPath();
        Identity kvs = joinPoint.getKvs();

        Identity names = kvs.unzip(ZipSide.Left);
        JoinPoint virtualPoint = new JoinPoint(path, names.zip(names));

        return branches.get(virtualPoint);
    }

    /**
     * We should let invoke understand which values now is using.
     */
    public Identity curval() {
        return this.curval; // curval not values...
    }
}
