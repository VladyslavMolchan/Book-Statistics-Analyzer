package org.JavaCoreTask.writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class XmlStatisticsWriterTest {

    @BeforeEach
    void setup() {

    }

    @Test
    void testWriteAndReadFile() throws Exception {
        Map<String, Long> stats = Map.of("Fiction", 3L, "Drama", 2L);
        XmlStatisticsWriter.write("genre", stats);

        Path writtenFile = Paths.get("statistics_by_genre.xml");
        assertTrue(Files.exists(writtenFile));

        String content = Files.readString(writtenFile);
        assertTrue(content.contains("<statistics>"));
        assertTrue(content.contains("<count>3</count>"));

        Files.deleteIfExists(writtenFile);
    }

    @Test
    void testEscapeXml() {
        String escaped = XmlStatisticsWriter.escapeXml("a<b> & 'quote'");
        assertEquals("a&lt;b&gt; &amp; &apos;quote&apos;", escaped);
    }

    @Test
    void testEscapeXml_null() {
        String escaped = XmlStatisticsWriter.escapeXml(null);
        assertEquals("", escaped);
    }

    @Test
    void testWriteEmptyStats() throws Exception {
        Map<String, Long> emptyStats = Map.of();
        XmlStatisticsWriter.write("empty", emptyStats);

        Path writtenFile = Paths.get("statistics_by_empty.xml");
        assertTrue(Files.exists(writtenFile));

        String content = Files.readString(writtenFile);
        assertTrue(content.contains("<statistics>"));
        assertTrue(content.contains("</statistics>"));

        Files.deleteIfExists(writtenFile);
    }

    @Test
    void testWriteWithSpecialChars() throws Exception {
        Map<String, Long> stats = Map.of("a<b>&'\"", 1L);
        XmlStatisticsWriter.write("special", stats);

        Path writtenFile = Paths.get("statistics_by_special.xml");
        String content = Files.readString(writtenFile);
        assertTrue(content.contains("<value>a&lt;b&gt;&amp;&apos;&quot;</value>"));
        assertTrue(content.contains("<count>1</count>"));

        Files.deleteIfExists(writtenFile);
    }


    @Test
    void testWriteWithNullStatsThrowsNullPointer() {
        assertThrows(NullPointerException.class,
                () -> XmlStatisticsWriter.write("genre", null));
    }
}
