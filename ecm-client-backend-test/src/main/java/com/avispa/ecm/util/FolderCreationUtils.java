package com.avispa.ecm.util;

import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.folder.Folder;
import com.avispa.ecm.model.folder.FolderService;
import com.avispa.ecm.model.format.Format;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FolderCreationUtils {
    public static Folder getRoot() {
        Folder root = new Folder();
        root.setId(UUID.randomUUID());
        root.setObjectName("Root folder");
        root.setPath(FolderService.FILE_SEPARATOR + "Root folder");
        return root;
    }

    public static Folder getFolder(Folder ancestor) {
        return getFolder(ancestor, "Folder");
    }

    public static Folder getFolder(Folder ancestor, String folderName) {
        Folder folder = new Folder();
        folder.setId(UUID.randomUUID());
        folder.setObjectName(folderName);
        folder.setFolder(ancestor);
        folder.setPath(ancestor.getPath() + FolderService.FILE_SEPARATOR + folderName);
        return folder;
    }

    public static Document getDocument() {
        return getDocument(null);
    }

    public static Document getDocument(Folder folder) {
        Document document = new Document();
        document.setId(UUID.randomUUID());
        document.setObjectName("Document");
        document.setFolder(folder);

        Format format = new Format();
        format.setObjectName("pdf");

        Content content = new Content();
        content.setFormat(format);
        content.setObjectName("content.txt");

        document.setContents(Set.of(content));
        return document;
    }
}
