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

package com.avispa.ecm.model.ui.menu.mapper;

import com.avispa.ecm.EcmConfiguration;
import com.avispa.ecm.model.ui.menu.Menu;
import com.avispa.ecm.model.ui.menu.MenuItem;
import com.avispa.ecm.model.ui.menu.dto.MenuDto;
import com.avispa.ecm.model.ui.menu.dto.MenuItemDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
@JsonTest
@ContextConfiguration(classes = {MenuDtoMapperImpl.class, MenuItemDtoMapperImpl.class,
        EcmConfiguration.class}) // required to load custom object mapper config
@Slf4j
class MenuDtoMapperTest {
    @Autowired
    private MenuDtoMapper menuDtoMapper;

    @Test
    void givenMenu_whenConvert_thenCorrectDtoCreated() {
        Menu menu = new Menu();
        menu.setObjectName("Menu");

        MenuItem menuItem1 = new MenuItem();
        menuItem1.setLabel("Invoice");

        MenuItem menuSubItem1 = new MenuItem();
        menuSubItem1.setLabel("Invoice action");
        menuSubItem1.setAction("invoice-action");

        menuItem1.setItems(List.of(menuSubItem1));

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setLabel("Some action");
        menuItem2.setAction("action");

        menu.setItems(List.of(menuItem1, menuItem2));

        MenuDto actualDto = menuDtoMapper.convert(menu);
        MenuDto expectedDto = getExpectedLayoutDto();

        assertEquals(expectedDto, actualDto);
    }

    private static MenuDto getExpectedLayoutDto() {
        return MenuDto.builder()
                .item(MenuItemDto.builder()
                        .label("Invoice")
                        .item(MenuItemDto.builder()
                                .label("Invoice action")
                                .action("invoice-action")
                                .build())
                        .build())
                .item(MenuItemDto.builder()
                        .label("Some action")
                        .action("action")
                        .build())
                .build();
    }
}