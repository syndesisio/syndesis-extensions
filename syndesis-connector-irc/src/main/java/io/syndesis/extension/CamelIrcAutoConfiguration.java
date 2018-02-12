package io.syndesis.extension;

import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(CamelAutoConfiguration.class)
@ConditionalOnClass(CamelAutoConfiguration.class)
public class CamelIrcAutoConfiguration {
    Logger LOG = LoggerFactory.getLogger(CamelIrcAutoConfiguration.class);
    // specifying a name here is VERY important, since SpringBoot uses a single namespace where it generates the Bean name from the annotated method name !!!
    // So either change the value in the annotation of the name of the annotated method itself
    @Bean(name = "myCamIrcCustomConfigurer")
    public CamelContextConfiguration integrationContextConfiguration() {
        LOG.info("CamelIrcAutoConfiguration custom behavior loaded correctly");
        return new CamelIrcContextConfiguration();
    }
}
