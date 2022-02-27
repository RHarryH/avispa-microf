package com.avispa.microf.model.directory;

import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.folder.Folder;
import com.avispa.ecm.model.format.Format;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Rafał Hiszpański
 */
class DirectoryNodeMapperIntegrationTest {
    private final DirectoryNodeMapper mapper = Mappers.getMapper(DirectoryNodeMapper.class);

    @Test
    void rootFolderOnly() {
        Folder root = getRoot();

        DirectoryNode directoryNode = mapper.convert(root);

        assertEquals("#", directoryNode.getParent());
        assertEquals("Root folder", directoryNode.getText());
        assertEquals(root.getId().toString(), directoryNode.getId());
        assertEquals("root", directoryNode.getType());
    }

    @Test
    void documentOnly() {
        Document document = getDocument();

        DirectoryNode directoryNode = mapper.convert(document);

        assertEquals("#", directoryNode.getParent());
        assertEquals("Document", directoryNode.getText());
        assertEquals(document.getId().toString(), directoryNode.getId());
        assertEquals("pdf", directoryNode.getType());
    }

    @Test
    void nestedFolder() {
        Folder root = getRoot();
        Folder nestedFolder = getFolder(root);

        DirectoryNode directoryNode = mapper.convert(nestedFolder);

        assertNotNull(directoryNode.getParent());

        assertEquals(root.getId().toString(), directoryNode.getParent());
        assertEquals("Folder", directoryNode.getText());
        assertEquals(nestedFolder.getId().toString(), directoryNode.getId());
        assertEquals("folder", directoryNode.getType());
    }

    @Test
    void nestedDocument() {
        Folder root = getRoot();
        Document nestedDocument = getDocument(root);

        DirectoryNode directoryNode = mapper.convert(nestedDocument);

        assertNotNull(directoryNode.getParent());

        assertEquals(root.getId().toString(), directoryNode.getParent());
        assertEquals("Document", directoryNode.getText());
        assertEquals(nestedDocument.getId().toString(), directoryNode.getId());
        assertEquals("pdf", directoryNode.getType());
    }

    private Folder getRoot() {
        Folder root = new Folder();
        root.setId(UUID.randomUUID());
        root.setObjectName("Root folder");
        return root;
    }

    private Folder getFolder(Folder ancestor) {
        Folder folder = new Folder();
        folder.setId(UUID.randomUUID());
        folder.setObjectName("Folder");
        folder.setFolder(ancestor);
        return folder;
    }

    private Document getDocument() {
        return getDocument(null);
    }

    private Document getDocument(Folder folder) {
        Document document = new Document();
        document.setId(UUID.randomUUID());
        document.setObjectName("Document");
        document.setFolder(folder);

        Format format = new Format();
        format.setObjectName("pdf");

        Content content = new Content();
        content.setFormat(format);

        document.setContents(Set.of(content));
        return document;
    }

}