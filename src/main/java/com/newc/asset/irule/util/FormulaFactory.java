package com.newc.asset.irule.util;

import com.newc.asset.irule.anno.Algorithm;
import com.newc.asset.irule.def.Formula;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 算子管理中心，负责注册、维护和执行算子
 * Created by paul on 2018/5/1.
 */
public class FormulaFactory {
    private static final Log logger = LogFactory.getLog(FormulaFactory.class);

    // The default base package that will contains all the alg which should be register to FormulaFactory.
    private static final String ALGORITHM_BASE_PACKAGE = "com.newc.asset.irule.alg";

    /**
     * Singleton Mode.
     * Create and return the only instance of FormulaFactory
     */
    private static FormulaFactory instance = null;
    public static FormulaFactory defaultFactory() {
        if (instance == null) {
            synchronized (FormulaFactory.class) {
                if (instance == null) {
                    FormulaFactory builder = new FormulaFactory();
                    instance = builder;
                }
            }
        }

        return instance;
    }

    // The inner cache store all alg implementation.
    private Map<String, Formula> formulaCache = new ConcurrentHashMap<String, Formula>();

    private FormulaFactory() {
        try {
            register();
        } catch (IOException e) {
            logger.error("Can not initialize all the configured Algorithm, please check the configuration.", e);
        }
    }

    private void register() throws IOException {
        logger.info("************************************************************************");
        logger.info("******************Register All Algorithm Start...***********************");
        String resourcePath = ClassUtils.convertClassNameToResourcePath(ALGORITHM_BASE_PACKAGE);
        String packageSearchPath = "classpath*:" + resourcePath + "/**/*.class";

        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

        AlgorithmMethodBuilder builder = AlgorithmMethodBuilder.defaultInstance();
        builder.clear(); // Clean cache in order to create a pure environment.

        SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
        for (Resource resource : resources) {
            MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
            register(reader.getClassMetadata());
        }

        formulaCache.putAll(builder.getAll());
        builder.clear(); // Clean cache to release memory.
    }

    private void register(ClassMetadata classMetadata) {
        if (!classMetadata.isConcrete()) return ;

        Class<?> clazz = forName(classMetadata.getClassName(), getClass().getClassLoader());
        if (clazz == null) return ;

        AlgorithmMethodBuilder builder = AlgorithmMethodBuilder.defaultInstance();
        builder.cacheAlgorithms(clazz);
    }

    private Class<?> forName(String className, ClassLoader classLoader) {
        try {
            return ClassUtils.forName(className, classLoader);
        } catch (ClassNotFoundException e) {
            logger.error("Could not find the class named: " + className, e);
            return null;
        }
    }

    public Formula get(String name) {
        return formulaCache.get(name);
    }

    /**
     * The Util Class that helps to create a singleton Algorithm method.
     */
    private static final class AlgorithmMethodBuilder {
        private static final Log logger = LogFactory.getLog(AlgorithmMethodBuilder.class);

        private static AlgorithmMethodBuilder instance = null;
        public static AlgorithmMethodBuilder defaultInstance() {
            if (instance == null) {
                synchronized (AlgorithmMethodBuilder.class) {
                    if (instance == null) { // Double check.
                        instance = new AlgorithmMethodBuilder();
                    }
                }
            }

            return  instance;
        }

        // The cache that stores all the Algorithm Class instance.
        Map<String, Formula> formulaCache = new ConcurrentHashMap<String, Formula>();

        // Default private constructor
        private AlgorithmMethodBuilder() {

        }

        public void clear() {
            formulaCache.clear();
        }


        public void cacheAlgorithms(Class<?> clazz) {
            Object instance = createInstance(clazz);
            if (instance == null) return ;

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods)
                cacheAlgorithm(instance, method);
        }

        private Object createInstance(Class<?> clazz) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                logger.error("Fail to create instance of class " + clazz.getName(), e);
                return null;
            }
        }

        private void cacheAlgorithm(Object instance, Method method) {
            Algorithm algorithm = AnnotationUtils.findAnnotation(method, Algorithm.class);
            if (algorithm == null) return ;

            String name = algorithm.value();
            if (formulaCache.get(name) != null) {
                // Already exsit in cache, print warn logs
                logger.warn("The alg " + name + " is already exsit, please check the alg configuration.");
            }

            formulaCache.put(name, new FormulaImpl(instance, method));
        }

        public Map<String, Formula> getAll() {
            return formulaCache;
        }
    }

    /**
     * Inner class that implements Formula Abstraction
     */
    private static final class FormulaImpl extends Formula {
        public FormulaImpl(Object instance, Method method) {
            super(instance, method);
        }
    }
}
