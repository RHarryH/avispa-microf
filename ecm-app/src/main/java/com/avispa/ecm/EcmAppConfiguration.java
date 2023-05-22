package com.avispa.ecm;

import com.avispa.ecm.model.ui.configuration.dto.ListWidgetConfigDto;
import com.avispa.ecm.model.configuration.load.ConfigurationRegistry;
import com.avispa.ecm.model.configuration.load.ConfigurationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Locale;

@Configuration
@PropertySource("classpath:ecm-app-application.properties")
public class EcmAppConfiguration {
	@Bean
	@Primary
	public ConfigurationRegistry ecmAppConfigurationRegistry() {
		var registry = new ConfigurationRegistry();
		registry.registerNewConfigurationType(ConfigurationType.of("ecm_list_widget", ListWidgetConfigDto.class, false));
		return registry;
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
}
