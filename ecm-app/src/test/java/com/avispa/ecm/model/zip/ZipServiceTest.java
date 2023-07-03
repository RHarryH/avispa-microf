package com.avispa.ecm.model.zip;

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.folder.Folder;
import com.avispa.ecm.model.folder.FolderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.avispa.ecm.util.FolderCreationUtils.getDocument;
import static com.avispa.ecm.util.FolderCreationUtils.getFolder;
import static com.avispa.ecm.util.FolderCreationUtils.getRoot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class ZipServiceTest {
    @Mock
    private FolderService folderService;

    @InjectMocks
    private ZipService zipService;

    @Test
    void givenRootFolder_whenZipping_thenZipIsEmpty() {
        Folder root = getRoot();

        when(folderService.getRootFolders()).thenReturn(List.of(root));
        when(folderService.getAllFoldersAndLinkedObjects(root, true)).thenReturn(List.of());

        byte[] zipFile = zipService.getZippedStructure();

        List<String> entries = getZipEntriesPaths(zipFile);

        assertEquals(List.of(), entries);
    }

    @Test
    void givenRootFolderWithContent_whenZipping_thenFileIsCorrect() {
        Folder root = getRoot();

        Document document = getDocument(root);

        when(folderService.getRootFolders()).thenReturn(List.of(root));
        when(folderService.getAllFoldersAndLinkedObjects(root, true)).thenReturn(List.of(document));

        byte[] zipFile = zipService.getZippedStructure();

        List<String> entries = getZipEntriesPaths(zipFile);

        assertEquals(List.of("Root folder\\content.txt"), entries);
    }

    @Test
    void givenTwoFoldersOneWithContentAndOneWithout_whenZipping_thenOnlyContent() {
        Folder root = getRoot();
        Folder folder_1 = getFolder(root, "Folder 1");
        Folder folder_2 = getFolder(root, "Folder 2");

        Document document = getDocument(folder_2);

        when(folderService.getRootFolders()).thenReturn(List.of(root));
        when(folderService.getAllFoldersAndLinkedObjects(root, true)).thenReturn(List.of(folder_1, folder_2, document));

        byte[] zipFile = zipService.getZippedStructure();

        List<String> entries = getZipEntriesPaths(zipFile);

        assertEquals(List.of("Root folder\\Folder 2\\content.txt"), entries);
    }

    /**
     * Unzip created file and collect all entries paths
     * @param zipFile
     * @return
     */
    private static List<String> getZipEntriesPaths(byte[] zipFile) {
        List<String> entries = new ArrayList<>();
        try(ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipFile))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                entries.add(zipEntry.getName());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return entries;
    }

    @Test
    void givenNonEmptyZipFile_whenCheckZip_thenIsZip() {
        var zipCandidate = getZipFileCandidate("zip/non-empty-zip.zip");
        assertTrue(ZipService.isZipFile(zipCandidate));
    }

    @Test
    void givenEmptyZipFile_whenCheckZip_thenIsNotZip() {
        var zipCandidate = getZipFileCandidate("zip/empty-zip.zip");
        assertFalse(ZipService.isZipFile(zipCandidate));
    }

    @Test
    void givenRegularFile_whenCheckZip_thenIsNotZip() {
        var zipCandidate = getZipFileCandidate("zip/file.txt");
        assertFalse(ZipService.isZipFile(zipCandidate));
    }

    @Test
    void givenNull_whenCheckZip_thenThrowException() {
        assertThrows(IllegalStateException.class, () -> ZipService.isZipFile((File) null));
    }

    @Test
    void givenNonEmptyZipStream_whenCheckZip_thenIsZip() throws IOException {
        var zipCandidate = getZipInputStreamCandidate("zip/non-empty-zip.zip");
        assertTrue(ZipService.isZipFile(zipCandidate));
    }

    @Test
    void givenEmptyZipStream_whenCheckZip_thenIsNotZip() throws IOException {
        var zipCandidate = getZipInputStreamCandidate("zip/empty-zip.zip");
        assertFalse(ZipService.isZipFile(zipCandidate));
    }

    @Test
    void givenRegularFileStream_whenCheckZip_thenIsNotZip() throws IOException {
        var zipCandidate = getZipInputStreamCandidate("zip/file.txt");
        assertFalse(ZipService.isZipFile(zipCandidate));
    }

    @Test
    void givenNullStream_whenCheckZip_thenThrowException() {
        assertThrows(IllegalStateException.class, () -> ZipService.isZipFile((InputStream) null));
    }

    private static File getZipFileCandidate(String name) {
        var resource = ZipServiceTest.class.getClassLoader().getResource(name);
        if(resource != null) {
            return new File(resource.getPath());
        }

        return null;
    }

    private static InputStream getZipInputStreamCandidate(String name) throws IOException {
        var resource = ZipServiceTest.class.getClassLoader().getResource(name);
        if(resource != null) {
            return resource.openStream();
        }

        return null;
    }
}