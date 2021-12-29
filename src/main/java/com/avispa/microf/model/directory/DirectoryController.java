package com.avispa.microf.model.directory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.document.DocumentService;
import com.avispa.ecm.model.folder.Folder;
import com.avispa.ecm.model.folder.FolderService;
import com.avispa.microf.model.zip.ZipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("directory")
@RequiredArgsConstructor
@Slf4j
public class DirectoryController {

    private final FolderService folderService;
    private final DocumentService documentService;
    private final ZipService zipService;
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

    @GetMapping(params = "export", produces = "application/zip")
    public ResponseEntity<Resource> exportStructure() {
        ByteArrayResource resource = new ByteArrayResource(zipService.getZippedStructure());

        return ResponseEntity.ok()
                .header("Content-disposition", "attachment; filename=structure-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + ".zip")
                .contentLength(resource.contentLength())
                .body(resource);
    }
}