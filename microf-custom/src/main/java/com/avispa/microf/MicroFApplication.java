/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
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

package com.avispa.microf;

import com.avispa.ecm.util.Version;
import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import com.avispa.microf.model.invoice.service.counter.impl.ContinuousCounterStrategy;
import com.avispa.microf.model.invoice.service.counter.impl.MonthCounterStrategy;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Rafał Hiszpański
 */
@SpringBootApplication(scanBasePackages = {"com.avispa.ecm", "com.avispa.microf"})
@EntityScan(basePackages = {"com.avispa.microf.model", "com.avispa.ecm.model"}) // required to use ECM entities
@PropertySource(value = "file:config/microf.properties", ignoreResourceNotFound = true)
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
    public Version microfVersion(@Value("${spring.application.name}") String applicationName, @Value("${microf.version}") String number) {
        return new Version(applicationName, number);
    }

    @Bean
    public GroupedOpenApi microfApi() {
        String[] paths = {"com.avispa.microf.model"};
        return GroupedOpenApi.builder().group("Avispa μF").packagesToScan(paths)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(MicroFApplication.class, args);
    }
}
