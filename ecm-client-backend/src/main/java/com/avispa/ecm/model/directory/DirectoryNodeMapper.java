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

package com.avispa.ecm.model.directory;

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.folder.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DirectoryNodeMapper {
    @Mapping(source = "folder.id", target= "parent", defaultExpression = "java(\"#\")", qualifiedByName = "uuidToString")
    @Mapping(source = "id", target= "id", qualifiedByName = "uuidToString")
    @Mapping(source = "objectName", target= "text")
    @Mapping(target="type", expression = "java(null != folder.getFolder() ? \"folder\" : \"root\")")
    DirectoryNode convert(Folder folder);

    @Mapping(source = "folder.id", target= "parent", defaultExpression = "java(\"#\")", qualifiedByName = "uuidToString")
    @Mapping(source = "id", target= "id", qualifiedByName = "uuidToString")
    @Mapping(source = "objectName", target= "text")
    @Mapping(source = ".", target="type", qualifiedByName = "getType")
    DirectoryNode convert(Document document);

    @Named("uuidToString")
    default String uuidToString(UUID id) {
        return id.toString();
    }

    @Named("getType")
    default String getType(Document document) {
        var primaryContent = document.getPrimaryContent();
        return null != primaryContent && null != primaryContent.getFormat() ? primaryContent.getFormat().getExtension() : "default";
    }
}