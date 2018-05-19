package com.ppmoney.asset.irule.util;

import com.ppmoney.asset.iframe.util.Reflection;
import com.ppmoney.asset.irule.entity.Branch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by paul on 2018/5/11.
 */
public class BranchBuilder {
    private static final Log logger = LogFactory.getLog(BranchBuilder.class);
    private static final Branch END = Impl.END;

    public static final Branch build(Object content) {
        if (content == null) return END;
        if (!(content instanceof Map)) {
            logger.warn("Can not understand the input content, " + content + ", we think it is the end of the branch.");
            return END;
        }

        Map<String, Object> maps = (Map<String, Object>)content;
        if (maps.isEmpty()) return END; // We think the empty map is also the end of the branch.
        return new Impl(maps);
    }

    private static final class Impl implements Branch {
        private static final Log logger = LogFactory.getLog(Impl.class);
        private static final Branch END = new Impl();

        private Map<String, Branch> forks = new HashMap<String, Branch>();
        private Impl() {
        }

        private Impl(Map<String, Object> roadmaps) {
            Assert.notNull(roadmaps, "content should not be null.");
            Assert.notEmpty(roadmaps, "content should not be empty.");

            for (String name : roadmaps.keySet()) {
                Branch fork = BranchBuilder.build(roadmaps.get(name));
                forks.put(name, fork);
            }
        }

        @Override
        public List<Object> collect(Collection<Object> content) {
            List<Object> result = new ArrayList<Object>();
            if (content == null) {
                logger.warn("We traced with the guide, but find there is no road any more!!!!");
                return result;
            }

            for (Object road : content) {
                result.addAll(collect(road));
            }

            return result;
        }

        private List<Object> collect(Object road) {
            List<Object> result = new ArrayList<Object>();
            if (road == null) {
                logger.warn("We traced with the guide, but find there is no road any more!!!!");
                return result;
            }

            if (this == END) {
                // This is the end of the world, we need to return back.
                result.add(road);
                return result;
            }

            for (String roadName : forks.keySet()) {
                Branch subBranch = forks.get(roadName);
                List<Object> subRoads = Reflection.getAll(road, roadName);
                result.addAll(subBranch.collect(subRoads));
            }

            return result;
        }

        @Override
        public void update(Collection<Object> content, Object value) {
            for (Object road : content)
                update(road, value);
        }

        public void update(Object road, Object value) {
            Assert.notNull(road, "road should not be null.");
            // In fact, we can never reach END when we using update method.
            Assert.isTrue(this != END, "Inner error occurs, please check.");

            // When we use update, that means we are going to store the algorithm result
            // As the result can only contains one element, so the forks can only have one item.
            Assert.isTrue(forks.size() == 1, "");
            String roadName = forks.keySet().iterator().next();
            Branch child = forks.get(roadName);

            if (child == END) { // This is the end of the path.
                Reflection.set(road, roadName, value);
            } else {
                ((Impl)child).update(Reflection.search(road, roadName), value);
            }
        }
    }
}
