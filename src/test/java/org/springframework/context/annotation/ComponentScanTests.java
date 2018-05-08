package org.springframework.context.annotation;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by paul on 2018/5/1.
 */
public class ComponentScanTests {
    @Test
    public void checkComponentScanFeature() throws IOException {
        String packageSearchPath = "classpath*:" + "com/newc/asset/irule/alg/**/*.class";
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        assertThat(resources, is(notNullValue()));

        Resource resource = resources[0];

        SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
        MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);

        assertThat(reader, is(notNullValue()));
    }
}
