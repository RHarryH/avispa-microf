package com.avispa.microf.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rafał Hiszpański
 */
class VersionTest {
    private Version snapshotVersion = new Version("1.0.3-SNAPSHOT");
    private Version releaseVersion = new Version("1.0.3");

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