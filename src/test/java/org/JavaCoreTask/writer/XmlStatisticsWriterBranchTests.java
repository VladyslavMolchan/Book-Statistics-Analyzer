package org.JavaCoreTask.writer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class XmlStatisticsWriterBranchTests {

    @Test
    void testEscapeXml_nullInput() {
        assertEquals("", XmlStatisticsWriter.escapeXml(null));
    }

    @Test
    void testEscapeXml_emptyString() {
        assertEquals("", XmlStatisticsWriter.escapeXml(""));
    }

    @Test
    void testEscapeXml_specialChars() {
        String input = "<&>'\"";
        String expected = "&lt;&amp;&gt;&apos;&quot;";
        assertEquals(expected, XmlStatisticsWriter.escapeXml(input));
    }

    @Test
    void testEscapeXml_normalString() {
        String input = "abc123";
        assertEquals("abc123", XmlStatisticsWriter.escapeXml(input));
    }

    @Test
    void testEscapeXml_mixedString() {
        String input = "a&b<c>d\"e'f";
        String expected = "a&amp;b&lt;c&gt;d&quot;e&apos;f";
        assertEquals(expected, XmlStatisticsWriter.escapeXml(input));
    }

    @Test
    void testWrite_emptyStats() throws IOException {
        Map<String, Long> stats = Map.of();
        String attribute = "empty";
        XmlStatisticsWriter.write(attribute, stats);

        Path path = Paths.get("statistics_by_" + attribute + ".xml");
        assertTrue(Files.exists(path));

        String content = Files.readString(path);
        assertTrue(content.contains("<statistics>"));
        assertTrue(content.contains("</statistics>"));

        Files.deleteIfExists(path);
    }

    @Test
    void testWrite_singleEntry() throws IOException {
        Map<String, Long> stats = Map.of("GenreA", 5L);
        String attribute = "single";
        XmlStatisticsWriter.write(attribute, stats);

        Path path = Paths.get("statistics_by_" + attribute + ".xml");
        String content = Files.readString(path);
        assertTrue(content.contains("<value>GenreA</value>"));
        assertTrue(content.contains("<count>5</count>"));

        Files.deleteIfExists(path);
    }

    @Test
    void testWrite_multipleEntries() throws IOException {
        Map<String, Long> stats = Map.of("A", 1L, "B", 2L, "C", 3L);
        String attribute = "multi";
        XmlStatisticsWriter.write(attribute, stats);

        Path path = Paths.get("statistics_by_" + attribute + ".xml");
        String content = Files.readString(path);
        assertTrue(content.contains("<value>A</value>"));
        assertTrue(content.contains("<value>B</value>"));
        assertTrue(content.contains("<value>C</value>"));

        Files.deleteIfExists(path);
    }

    @Test
    void testWrite_keyWithEmptyString() throws IOException {
        Map<String, Long> stats = Map.of("", 7L);
        String attribute = "emptyKey";
        XmlStatisticsWriter.write(attribute, stats);

        Path path = Paths.get("statistics_by_" + attribute + ".xml");
        String content = Files.readString(path);
        assertTrue(content.contains("<value></value>"));
        assertTrue(content.contains("<count>7</count>"));

        Files.deleteIfExists(path);
    }

    @Test
    void testWrite_keyWithSpecialChars() throws IOException {
        Map<String, Long> stats = Map.of("<&>'\"", 9L);
        String attribute = "specialKey";
        XmlStatisticsWriter.write(attribute, stats);

        Path path = Paths.get("statistics_by_" + attribute + ".xml");
        String content = Files.readString(path);
        assertTrue(content.contains("<value>&lt;&amp;&gt;&apos;&quot;</value>"));
        assertTrue(content.contains("<count>9</count>"));

        Files.deleteIfExists(path);
    }

    @Test
    void testWrite_attributeWithSpaces() throws IOException {
        Map<String, Long> stats = Map.of("A", 1L);
        String attribute = "attr with spaces";
        XmlStatisticsWriter.write(attribute, stats);

        Path path = Paths.get("statistics_by_" + attribute + ".xml");
        assertTrue(Files.exists(path));

        Files.deleteIfExists(path);
    }

    @Test
    void testWrite_nullStats_throws() {
        assertThrows(NullPointerException.class, () -> XmlStatisticsWriter.write("attr", null));
    }

    @Test
    void testWrite_nullAttribute_throws() {
        Map<String, Long> stats = Map.of("A", 1L);
        assertThrows(NullPointerException.class, () -> XmlStatisticsWriter.write(null, stats));
    }

    @Test
    void testWrite_statsWithSpacesInKey() throws IOException {
        Map<String, Long> stats = Map.of("some key", 11L);
        String attribute = "spaceKey";
        XmlStatisticsWriter.write(attribute, stats);

        Path path = Paths.get("statistics_by_" + attribute + ".xml");
        String content = Files.readString(path);
        assertTrue(content.contains("<value>some key</value>"));
        assertTrue(content.contains("<count>11</count>"));

        Files.deleteIfExists(path);
    }
}
