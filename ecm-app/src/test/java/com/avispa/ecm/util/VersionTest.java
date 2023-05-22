package com.avispa.ecm.util;

import com.avispa.ecm.util.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class VersionTest {
    private final Version snapshotVersion = new Version("1.0.3-SNAPSHOT");
    private final Version releaseVersion = new Version("1.0.3");

    @Test
    void snapshotVersion() {
        assertEquals("1.0.3-SNAPSHOT", snapshotVersion.getNumber());
        assertEquals("1.0.3", snapshotVersion.getReleaseNumber());
    }

    @Test
    void releaseVersion() {
        assertEquals("1.0.3", releaseVersion.getNumber());
        assertEquals("1.0.3", releaseVersion.getReleaseNumber());
    }
}