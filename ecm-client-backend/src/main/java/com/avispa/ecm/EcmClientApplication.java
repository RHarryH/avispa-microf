/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.ecm;

import com.avispa.ecm.model.configuration.load.ConfigurationRegistry;
import com.avispa.ecm.model.configuration.load.ConfigurationType;
import com.avispa.ecm.model.load.dto.ApplicationDto;
import com.avispa.ecm.model.load.dto.LayoutDto;
import com.avispa.ecm.model.load.dto.LinkDocumentDto;
import com.avispa.ecm.model.load.dto.ListWidgetDto;
import com.avispa.ecm.model.load.dto.MenuDto;
import com.avispa.ecm.model.ui.application.Application;
import com.avispa.ecm.model.ui.layout.Layout;
import com.avispa.ecm.model.ui.menu.Menu;
import com.avispa.ecm.model.ui.modal.link.LinkDocument;
import com.avispa.ecm.model.ui.widget.list.ListWidget;
import com.avispa.ecm.util.Version;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Locale;

import static com.avispa.ecm.model.configuration.load.ConfigurationRegistry.ECM_CONTEXT;

/**
 * @author Rafał Hiszpański
 */
@SpringBootApplication(scanBasePackages = {"com.avispa.ecm", "${avispa.ecm.additional-component-packages:}"})
@EntityScan(basePackages = {"com.avispa.ecm.model", "${avispa.ecm.additional-entities-packages:}"})
@PropertySource(value = "classpath:/ecm.properties")
@PropertySource(value = "classpath:/ecm-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:/ecm-client.properties")
@PropertySource(value = "classpath:/ecm-client-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class EcmClientApplication {
    @Bean
    @Primary
    public ConfigurationRegistry ecmAppConfigurationRegistry() {
        var registry = new ConfigurationRegistry();
        registry.register(ConfigurationType.of("ecm_application", Application.class, ApplicationDto.class, false));
        registry.register(ConfigurationType.of("ecm_list_widget", ListWidget.class, ListWidgetDto.class, false));
        registry.register(ConfigurationType.of("ecm_menu", Menu.class, MenuDto.class, false));
        registry.register(ConfigurationType.of("ecm_layout", Layout.class, LayoutDto.class, true));
        registry.register(ConfigurationType.of("ecm_link_document", LinkDocument.class, LinkDocumentDto.class, false), ECM_CONTEXT);
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

    @Bean
    public Version ecmClientVersion(@Value("${avispa.ecm.client.name}") String applicationName, @Value("${avispa.ecm.client.version}") String number) {
        return new Version(applicationName, number);
    }

    @Bean
    public OpenAPI ecmApi(Version ecmClientVersion) {
        return new OpenAPI()
                .info(new Info().title("Avispa ECM Backend")
                        .description("API for communication with Avispa ECM. It includes customization endpoints in dedicated groups.")
                        .version(ecmClientVersion.number())
                        .license(new License().name("AGPL v3").url("https://www.gnu.org/licenses/agpl-3.0.txt")));
    }

    @Bean
    public GroupedOpenApi ecmAndEcmBackendApi() {
        String[] packagesToScan = {"com.avispa.ecm"};
        return GroupedOpenApi.builder().group("Avispa ECM and Avispa ECM Backend").packagesToScan(packagesToScan)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(EcmClientApplication.class, args);
    }
}
