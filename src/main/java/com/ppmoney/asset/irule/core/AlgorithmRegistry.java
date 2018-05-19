package com.ppmoney.asset.irule.core;

import com.alibaba.fastjson.JSONObject;
import com.ppmoney.asset.irule.annotation.AlgConfg;
import com.ppmoney.asset.irule.define.Caller;
import com.ppmoney.asset.irule.exception.AlgorithmException;
import com.ppmoney.asset.irule.exception.AlgorithmIllegalArgumentException;
import com.ppmoney.asset.irule.exception.AlgorithmNotFoundException;
import com.ppmoney.asset.irule.exception.AlgorithmRegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by paul on 2018/5/8.
 */
public class AlgorithmRegistry implements Caller<Object> {
    private static final Log logger = LogFactory.getLog(AlgorithmRegistry.class);
    private static final String ALGORITHM_BASE_PACKAGE = "com.ppmoney.asset.***.algorithm";

    // Singleton Implementation.
    private static AlgorithmRegistry instance = null;
    public static final AlgorithmRegistry singleton() {
        if (instance ==  null) {
            synchronized (AlgorithmRegistry.class) {
                if (instance == null) {
                    try {
                        instance = new AlgorithmRegistry();
                    } catch (AlgorithmRegistryException e) {
                        throw new IllegalArgumentException("Error, we failed to upload all the algorithms", e);
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Cache all algrithms.
     */
    private Map<String, CallableAlgorithm> algorithms = new ConcurrentHashMap<String, CallableAlgorithm>();

    private AlgorithmRegistry() throws AlgorithmRegistryException {
        this.register();
    }

    private void register() throws AlgorithmRegistryException {
        try {
            logger.info("************************************************************************");
            logger.info("******************Algorithm Register Starting...************************");

            String resourcePath = ClassUtils.convertClassNameToResourcePath(ALGORITHM_BASE_PACKAGE);
            String packageSearchPath = "classpath*:" + resourcePath + "/**/*.class";

            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

            SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
            for (Resource resource : resources) {
                MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
                ClassMetadata metadata = reader.getClassMetadata();

                if (!metadata.isConcrete()) continue;
                if (!metadata.isIndependent()) continue;

                Class clazz = ClassUtils.forName(metadata.getClassName(), getClass().getClassLoader());
                register(clazz);
            }

            logger.info("******************Algorithm Register Finished***************************");
        } catch (Exception ex) {
            throw new AlgorithmRegistryException("Fail to register all Algorithms in system!", ex);
        }
    }

    private void register(Class clazz) throws IllegalAccessException, InstantiationException, AlgorithmRegistryException {
        Object instance = newInstance(clazz);
        if (instance == null) return ; // do nothing.

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            AlgConfg config = method.getAnnotation(AlgConfg.class);
            if (config == null) continue;

            String name = config.value();
            if (algorithms.containsKey(name)) {
                // It means developer may probably using a algorithm twice!
                throw new AlgorithmRegistryException("The algorithm named " + name + " is already exist!");
            }

            algorithms.put(name, new CallableAlgorithm(instance, method));
        }

    }

    private Object newInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            logger.warn("Fail to get instance of class: " + clazz);
            return null;
        }
    }


    @Override
    /**
     * The first element of params must be a string that is the name of the algorithm.
     * If the second params is JSONObject type, it means that this element is the AlgorithmConfig
     * The remaining params are all algorithm inputs.
     */
    public Object call(Object... params) throws AlgorithmException {
        if (params == null || params.length < 2) {
            // params length must be at least 2
            // The first element is the name of the algorithm
            // The algorithm input is at least conatins one parameter.
            throw new AlgorithmIllegalArgumentException("The input params is illegal.");
        }

        String name = getAlgorithmName(params);
        JSONObject config = getAlgorithmConfig(params);
        Object[] inputs = getAlgorithmInputs(params);

        return call(name, config, inputs);
    }

    private String getAlgorithmName(Object... params) throws AlgorithmIllegalArgumentException {
        if (!(params[0] instanceof String)) {
            throw new AlgorithmIllegalArgumentException("The first elment in params must be string, it is the name of the algorithm");
        }

        return (String)params[0];
    }

    private JSONObject getAlgorithmConfig(Object... params) {
        if (containsConfig(params))
            return (JSONObject)params[1];

        return null;
    }

    private Object[] getAlgorithmInputs(Object... params) {
        int start = 1;
        if (containsConfig(params))
            start++; // We should also escape the config element.

        Object[] inputs = new Object[params.length - start];
        System.arraycopy(params, start, inputs, 0, inputs.length);
        return inputs;
    }

    private boolean containsConfig(Object... params) {
        if (params.length < 2) return false;
        if (params[1] instanceof JSONObject) return true;

        return false;
    }

    public Object call(String name, JSONObject config, Object... params) throws AlgorithmException {
        CallableAlgorithm callableAlgorithm = algorithms.get(name);
        if (callableAlgorithm == null) {
            throw new AlgorithmNotFoundException();
        }

        return  callableAlgorithm.call(config, params);
        // return pack(algorithmResult);
    }

    /*
    private Object[] pack(Object object) {
        if (object instanceof Object[]) return (Object[])object;

        Object[] array = new Object[1];
        array[0] = object;

        return array;
    }
    */
}
