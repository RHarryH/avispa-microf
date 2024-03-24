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

package com.avispa.ecm.model.load;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.load.dto.MenuDto;
import com.avispa.ecm.model.load.dto.MenuItemDto;
import com.avispa.ecm.model.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@SpringBootTest
class MenuLoaderTest {

    @MockBean(name = "menuRepository")
    private EcmConfigRepository<Menu> repository;

    @Autowired
    private MenuLoader loader;

    @Captor
    private ArgumentCaptor<Menu> entityCaptor;

    @Test
    void givenMenuDtoAndContent_whenLoad_thenProperMethodsInvoked() {
        final String configName = "Default menu config";
        MenuDto dto = getMenuDto(configName);

        when(repository.findByObjectName(configName)).thenReturn(Optional.empty());

        loader.load(dto, true);

        verify(repository).save(entityCaptor.capture());

        Menu menu = entityCaptor.getValue();
        assertEquals(configName, menu.getObjectName());
        var menuItems = menu.getItems();
        assertEquals(2, menuItems.size());

        var menuItem1 = menuItems.get(0);
        assertEquals("Invoice", menuItem1.getLabel());
        assertFalse(menuItem1.getItems().isEmpty());
        assertEquals("Invoice action", menuItem1.getItems().get(0).getLabel());

        var menuItem2 = menuItems.get(1);
        assertEquals("Some action", menuItem2.getLabel());
        assertEquals("action", menuItem2.getAction());
    }

    private static MenuDto getMenuDto(String configName) {
        MenuDto dto = new MenuDto();
        dto.setName(configName);

        MenuItemDto menuItemDto1 = new MenuItemDto();
        menuItemDto1.setLabel("Invoice");

        MenuItemDto menuSubItemDto1 = new MenuItemDto();
        menuSubItemDto1.setLabel("Invoice action");
        menuSubItemDto1.setAction("invoice-action");

        menuItemDto1.setItems(List.of(menuSubItemDto1));

        MenuItemDto menuItemDto2 = new MenuItemDto();
        menuItemDto2.setLabel("Some action");
        menuItemDto2.setAction("action");

        dto.setItems(List.of(menuItemDto1, menuItemDto2));
        return dto;
    }
}