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

package com.avispa.ecm;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DocumentFormatRegistry;
import org.jodconverter.core.job.ConversionJobWithOptionalSourceFormatUnspecified;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.InputStream;

/**
 * @author Rafał Hiszpański
 */
@SpringBootApplication
public class EcmClientTestApplication {

    @Bean
    @ConditionalOnProperty(prefix = "jodconverter.local", name = "enabled", havingValue = "false")
    public DocumentConverter dummyDocumentConverter() {
        return new DocumentConverter() {
            @Override
            public @NonNull ConversionJobWithOptionalSourceFormatUnspecified convert(@NonNull File file) {
                return null;
            }

            @Override
            public @NonNull ConversionJobWithOptionalSourceFormatUnspecified convert(@NonNull InputStream inputStream) {
                return null;
            }

            @Override
            public @NonNull ConversionJobWithOptionalSourceFormatUnspecified convert(@NonNull InputStream inputStream, boolean b) {
                return null;
            }

            @Override
            public @NonNull DocumentFormatRegistry getFormatRegistry() {
                return null;
            }
        };
    }
}
