package com.avispa.microf.model.directory;

import java.util.ArrayList;
import java.util.List;

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.document.DocumentService;
import com.avispa.ecm.model.folder.Folder;
import com.avispa.ecm.model.folder.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("directory")
@RequiredArgsConstructor
public class DirectoryController {

    private final FolderService folderService;
    private final DocumentService documentService;
    private final DirectoryNodeMapper directoryNodeMapper;

    @GetMapping
    public List<DirectoryNode> directory() {
        List<DirectoryNode> directoryNodes = new ArrayList<>();

        processFolders(directoryNodes);
        processDocuments(directoryNodes);

        return directoryNodes;
    }

    private void processDocuments(List<DirectoryNode> directoryNodes) {
        List<Document> documents = documentService.getAllDocumentsInAnyFolder();
        for(Document document : documents) {
            directoryNodes.add(directoryNodeMapper.convert(document));
        }
    }

    private void processFolders(List<DirectoryNode> directoryNodes) {
        List<Folder> folders = folderService.getAllFolders();
        for(Folder folder : folders) {
            directoryNodes.add(directoryNodeMapper.convert(folder));
        }
    }
}