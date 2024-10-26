package io.galacsh.starter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfiguration
@EnableConfigurationProperties(SampleProperties.class)
public class SampleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Sample sample(SampleProperties props) {
        return new Sample(props.getId(), props.getName());
    }
}
