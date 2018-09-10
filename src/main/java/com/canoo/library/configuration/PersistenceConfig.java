package com.canoo.library.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages={"com.canoo.library.persistence.repository"})
public class PersistenceConfig {

}
