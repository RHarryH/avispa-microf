package com.avispa.microf.model.directory;

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.folder.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DirectoryNodeMapper {
    @Mapping(source = "folder.ancestor.id", target= "parent", defaultValue = "#", qualifiedByName = "uuidToString")
    @Mapping(source = "id", target= "id", qualifiedByName = "uuidToString")
    @Mapping(source = "objectName", target= "text")
    //@Mapping(target="type", expression = "java(null != folder.getAncestor() ? \"folder\" : \"root\")")
    @Mapping(target="icon", expression = "java(null != folder.getAncestor() ? \"bi bi-folder-fill\" : \"bi bi-archive-fill\")")
    DirectoryNode convert(Folder folder);

    @Mapping(source = "document.folder.id", target= "parent", defaultValue = "#", qualifiedByName = "uuidToString")
    @Mapping(source = "id", target= "id", qualifiedByName = "uuidToString")
    @Mapping(source = "objectName", target= "text")
    @Mapping(source = "document", target="icon", qualifiedByName = "getIcon")
    DirectoryNode convert(Document document);

    @Named("uuidToString")
    default String uuidToString(UUID id) {
        return id.toString();
    }

    @Named("getIcon")
    default String getIcon(Document document) {
        return document.getPrimaryContent().getFormat().getIcon();
    }
}