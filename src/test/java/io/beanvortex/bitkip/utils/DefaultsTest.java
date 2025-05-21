package io.beanvortex.bitkip.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultsTest {

    @Test
    void testStaticQueueNames() {
        // Verify static queue names
        List<String> staticQueueNames = Defaults.staticQueueNames;
        
        assertNotNull(staticQueueNames, "Static queue names should not be null");
        assertTrue(staticQueueNames.contains(Defaults.ALL_DOWNLOADS_QUEUE), "Should contain ALL_DOWNLOADS_QUEUE");
        assertTrue(staticQueueNames.contains(Defaults.COMPRESSED_QUEUE), "Should contain COMPRESSED_QUEUE");
        assertTrue(staticQueueNames.contains(Defaults.PROGRAMS_QUEUE), "Should contain PROGRAMS_QUEUE");
        assertTrue(staticQueueNames.contains(Defaults.VIDEOS_QUEUE), "Should contain VIDEOS_QUEUE");
        assertTrue(staticQueueNames.contains(Defaults.DOCS_QUEUE), "Should contain DOCUMENTS_QUEUE");
        assertTrue(staticQueueNames.contains(Defaults.MUSIC_QUEUE), "Should contain MUSIC_QUEUE");
        assertTrue(staticQueueNames.contains(Defaults.OTHERS_QUEUE), "Should contain OTHERS_QUEUE");
        assertEquals(7, staticQueueNames.size(), "Should have exactly 7 static queue names");
    }

    @Test
    void testExtensions() {
        // Verify extensions map
        Map<String, List<String>> extensions = Defaults.extensions;
        
        assertNotNull(extensions, "Extensions map should not be null");
        assertTrue(extensions.containsKey(Defaults.COMPRESSED_QUEUE), "Should contain COMPRESSED_QUEUE key");
        assertTrue(extensions.containsKey(Defaults.PROGRAMS_QUEUE), "Should contain PROGRAMS_QUEUE key");
        assertTrue(extensions.containsKey(Defaults.VIDEOS_QUEUE), "Should contain VIDEOS_QUEUE key");
        assertTrue(extensions.containsKey(Defaults.DOCS_QUEUE), "Should contain DOCUMENTS_QUEUE key");
        assertTrue(extensions.containsKey(Defaults.MUSIC_QUEUE), "Should contain MUSIC_QUEUE key");
        assertTrue(extensions.containsKey(Defaults.OTHERS_QUEUE), "Should contain OTHERS_QUEUE key");
        
        // Check specific extensions
        assertTrue(extensions.get(Defaults.COMPRESSED_QUEUE).contains("zip"), "COMPRESSED_QUEUE should contain .zip");
        assertTrue(extensions.get(Defaults.PROGRAMS_QUEUE).contains("exe"), "PROGRAMS_QUEUE should contain .exe");
        assertTrue(extensions.get(Defaults.VIDEOS_QUEUE).contains("mp4"), "VIDEOS_QUEUE should contain .mp4");
        assertTrue(extensions.get(Defaults.DOCS_QUEUE).contains("pdf"), "DOCUMENTS_QUEUE should contain .pdf");
        assertTrue(extensions.get(Defaults.MUSIC_QUEUE).contains("mp3"), "MUSIC_QUEUE should contain .mp3");
        
        // OTHERS_QUEUE should have empty list
        assertTrue(extensions.get(Defaults.OTHERS_QUEUE).isEmpty(), "OTHERS_QUEUE should have empty list");
    }
}
