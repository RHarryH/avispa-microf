package com.avispa.microf.model.directory;

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.document.DocumentService;
import com.avispa.ecm.model.folder.Folder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class DirectoryNodeMapper {

    @Autowired
    private DocumentService documentService;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "folder.ancestor.id", target= "parent", defaultValue = "#", qualifiedByName = "uuidToString")
    @Mapping(source = "id", target= "id", qualifiedByName = "uuidToString")
    @Mapping(source = "objectName", target= "text")
    //@Mapping(target="type", expression = "java(null != folder.getAncestor() ? \"folder\" : \"root\")")
    @Mapping(target="icon", expression = "java(null != folder.getAncestor() ? \"bi bi-folder-fill\" : \"bi bi-archive-fill\")")
    public abstract DirectoryNode convert(Folder folder);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "document.folder.id", target= "parent", defaultValue = "#", qualifiedByName = "uuidToString")
    @Mapping(source = "id", target= "id", qualifiedByName = "uuidToString")
    @Mapping(source = "objectName", target= "text")
    @Mapping(source = "document", target="icon", qualifiedByName = "getIcon")
    public abstract DirectoryNode convert(Document document);

    /*@AfterMapping
    protected void afterAgreementDtoTOAgreementMapping(Document document, @MappingTarget DirectoryNode directoryNode) {
        if(document.hasPdfRendition()) {
            directoryNode.setType(PDF);
        } else if(!document.getContents().isEmpty()){
            document.getContents().stream().findAny().ifPresent(c -> directoryNode.setType(c.getObjectName().toLowerCase(Locale.ROOT)));
        }
    }*/

    @Named("uuidToString")
    protected String uuidToString(UUID id) {
        return id.toString();
    }

    @Named("getIcon")
    protected String getIcon(Document document) {
        return document.getPrimaryContent().getFormat().getIcon();
    }
}