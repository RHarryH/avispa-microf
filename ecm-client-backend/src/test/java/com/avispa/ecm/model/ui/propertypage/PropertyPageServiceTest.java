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

package com.avispa.ecm.model.ui.propertypage;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.PropertyPageMapper;
import com.avispa.ecm.model.configuration.upsert.Upsert;
import com.avispa.ecm.util.TestDocument;
import com.avispa.ecm.util.TestDocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
@Slf4j
class PropertyPageServiceTest {
    @Mock
    private ContextService contextService;

    @Mock
    private PropertyPageMapper propertyPageMapper;

    @Mock
    private EcmConfigRepository<PropertyPage> propertyPageRepository;

    @InjectMocks
    private PropertyPageService propertyPageService;

    @Test
    void givenTypeAndDto_whenGetPropertyPage_thenContentIsReturned() {
        PropertyPage propertyPage = createPropertyPage();
        Upsert upsert = createUpsert(propertyPage);
        TestDocumentDto testDocumentDto = new TestDocumentDto();
        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(contextService.getConfiguration(TestDocument.class, Upsert.class))
                .thenReturn(Optional.of(upsert));
        when(propertyPageMapper.convertToContent(propertyPage, testDocumentDto, false))
                .thenReturn(propertyPageContent);

        var result = propertyPageService.getPropertyPage(TestDocument.class, testDocumentDto);

        assertEquals(propertyPageContent, result);
    }

    @Test
    void givenObject_whenGetPropertyPage_thenContentIsReturned() {
        PropertyPage propertyPage = createPropertyPage();
        TestDocument testDocument = new TestDocument();
        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(contextService.getConfiguration(testDocument, PropertyPage.class))
                .thenReturn(Optional.of(propertyPage));
        when(propertyPageMapper.convertToContent(propertyPage, testDocument, false))
                .thenReturn(propertyPageContent);

        var result = propertyPageService.getPropertyPage(testDocument, false);

        assertEquals(propertyPageContent, result);
    }

    @Test
    void givenName_whenGetPropertyPage_thenContentIsReturned() {
        PropertyPage propertyPage = createPropertyPage();
        TestDocument testDocument = new TestDocument();
        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(propertyPageRepository.findByObjectName("Name")).thenReturn(Optional.of(propertyPage));
        when(propertyPageMapper.convertToContent(propertyPage, testDocument, false))
                .thenReturn(propertyPageContent);

        var result = propertyPageService.getPropertyPage("Name", testDocument);

        assertEquals(propertyPageContent, result);
    }

    private Upsert createUpsert(PropertyPage propertyPage) {
        Upsert upsert = new Upsert();
        upsert.setPropertyPage(propertyPage);

        return upsert;
    }

    private PropertyPage createPropertyPage() {
        PropertyPage propertyPage = new PropertyPage();
        propertyPage.setId(UUID.randomUUID());

        return propertyPage;
    }
}