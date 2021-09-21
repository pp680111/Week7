package com.zst.week7.q10.configuration;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Configuration
public class DataSourceConfiguration {
    @Bean
    public DataSource shardingSphereDataSource() throws IOException, SQLException {
        File yamlFile = new ClassPathResource("application-shardingsphere-jdbc.yml").getFile();
        return YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
    }
}
