package com.avispa.microf;

import com.avispa.cms.util.CmsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.avispa")
@PropertySource("classpath:application.properties")
@PropertySource("file:./microf.properties")
public class MicroFApplication extends CmsConfiguration {
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
