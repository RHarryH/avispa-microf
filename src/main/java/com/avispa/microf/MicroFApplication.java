package com.avispa.microf;

import com.avispa.cms.CmsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.avispa")
@EntityScan(basePackages = {"com.avispa.microf.model", "com.avispa.cms.model"}) // required to use CMS entities
@EnableJpaRepositories(basePackages = {"com.avispa.microf.model", "com.avispa.cms.model"}) // required to use CMS repositories
@PropertySource("classpath:application.properties")
@PropertySource("file:./microf.properties")
@Import(CmsConfiguration.class)
public class MicroFApplication {
	/*@Bean
	@Description("Spring Message Resolver")
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("application");
		return messageSource;
	}*/

	public static void main(String[] args) {
		SpringApplication.run(MicroFApplication.class, args);
	}
}
