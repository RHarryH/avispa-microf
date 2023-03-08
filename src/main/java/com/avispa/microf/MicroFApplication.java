package com.avispa.microf;

import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import com.avispa.microf.model.invoice.service.counter.impl.ContinuousCounterStrategy;
import com.avispa.microf.model.invoice.service.counter.impl.MonthCounterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Locale;

@SpringBootApplication(scanBasePackages = "com.avispa")
@EntityScan(basePackages = {"com.avispa.microf.model", "com.avispa.ecm.model"}) // required to use CMS entities
@EnableJpaRepositories(basePackages = {"com.avispa.microf.model", "com.avispa.ecm.model"}) // required to use CMS repositories
@PropertySource("classpath:application.properties")
@PropertySource("file:./microf.properties")
public class MicroFApplication {
	@Value("${microf.invoice.counter-strategy}")
	private String counterStrategyName;

	// Optionally can be realized with Spring interface Condition and @Conditional annotation
	@Bean
	public CounterStrategy counterStrategy(@Autowired InvoiceRepository invoiceRepository) {
		if(counterStrategyName.equals("continuousCounterStrategy")) {
			return new ContinuousCounterStrategy(invoiceRepository);
		} else if(counterStrategyName.equals("monthCounterStrategy")) {
			return new MonthCounterStrategy(invoiceRepository);
		} else {
			throw new IllegalStateException(String.format("Unknown invoice counter strategy %s", counterStrategyName));
		}
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("pl", "PL"));
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Bean
	public DefaultDataBinderFactory getDataBinderFactory(@Autowired RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
		return new DefaultDataBinderFactory(requestMappingHandlerAdapter.getWebBindingInitializer());
	}

	public static void main(String[] args) {
		SpringApplication.run(MicroFApplication.class, args);
	}
}
