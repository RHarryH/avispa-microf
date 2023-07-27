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

package com.avispa.ecm.model.ui.menu;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.ui.menu.dto.MenuDto;
import com.avispa.ecm.model.ui.menu.dto.MenuItemDto;
import com.avispa.ecm.model.ui.menu.mapper.MenuDtoMapper;
import com.avispa.ecm.model.ui.menu.mapper.MenuItemDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private EcmConfigRepository<Menu> menuRepository;

    private final MenuDtoMapper menuDtoMapper = Mappers.getMapper(MenuDtoMapper.class);
    private final MenuItemDtoMapper menuItemDtoMapper = Mappers.getMapper(MenuItemDtoMapper.class);

    private MenuService menuService;

    @BeforeEach
    void init() {
        menuService = new MenuService(menuRepository, menuDtoMapper);
    }

    @Test
    void givenMenuInRepo_whenGet_thenReturned() {
        Menu menu = getMenu();

        ReflectionTestUtils.setField(menuService, "menuConfigName", "Menu");
        ReflectionTestUtils.setField(menuDtoMapper, "menuItemDtoMapper", menuItemDtoMapper);

        when(menuRepository.findByObjectName("Menu")).thenReturn(Optional.of(menu));

        MenuDto actualDto = menuService.getConfiguration();
        MenuDto expectedDto = getExpectedMenuDto();

        assertEquals(expectedDto, actualDto);
    }

    private static Menu getMenu() {
        Menu menu = new Menu();
        menu.setObjectName("Menu");

        MenuItem menuItem = new MenuItem();
        menuItem.setLabel("Label");
        menuItem.setAction("Action");

        menu.setItems(List.of(menuItem));
        return menu;
    }

    private static MenuDto getExpectedMenuDto() {
        return MenuDto.builder()
                .item(MenuItemDto.builder()
                        .label("Label")
                        .action("Action")
                        .build())
                .build();
    }

    @Test
    void givenMenuNotInRepo_whenGet_thenNullReturned() {
        ReflectionTestUtils.setField(menuService, "menuConfigName", "Menu");

        when(menuRepository.findByObjectName("Menu")).thenReturn(Optional.empty());

        MenuDto actualDto = menuService.getConfiguration();

        assertNull(actualDto);
    }

    @Test
    void givenNoConfigurationName_whenGet_thenNullReturned() {
        ReflectionTestUtils.setField(menuService, "menuConfigName", "");

        MenuDto actualDto = menuService.getConfiguration();

        assertNull(actualDto);
    }
}