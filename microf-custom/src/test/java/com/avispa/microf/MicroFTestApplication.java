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
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import com.avispa.microf.model.invoice.service.counter.impl.ContinuousCounterStrategy;
import com.avispa.microf.model.invoice.service.counter.impl.MonthCounterStrategy;
import jakarta.persistence.EntityManager;
import org.jodconverter.core.DocumentConverter;
import org.mockito.Mockito;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Rafał Hiszpański
 */
@SpringBootApplication(scanBasePackages = {"com.avispa.ecm", "${avispa.ecm.additional-component-packages:}"})
@EntityScan(basePackages = {"com.avispa.ecm.model", "${avispa.ecm.additional-entities-packages:}"})
@PropertySource(value = "classpath:/ecm.properties")
public class MicroFTestApplication {
    @Bean
    public DocumentConverter dummyDocumentConverter() {
        return Mockito.mock(DocumentConverter.class);
    }
}
