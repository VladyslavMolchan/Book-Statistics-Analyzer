package org.JavaCoreTask.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

class BookParserTest {

    @Test
    void testParseFileAndCollectStats_validBooks_author() throws IOException {
        Path path = Files.createTempFile("books", ".json");
        String json = """
                [
                    {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":["Fiction"]},
                    {"title":"Book B","author":"Author 2","yearPublished":2021,"genres":["Drama"]},
                    {"title":"Book C","author":"Author 1","yearPublished":2022,"genres":["Fiction","Drama"]}
                ]
                """;
        Files.writeString(path, json);

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "author");

        assertEquals(2L, stats.get("Author 1"));
        assertEquals(1L, stats.get("Author 2"));
        assertEquals(2, stats.size());
    }

    @Test
    void testParseFileAndCollectStats_genres() throws IOException {
        Path path = Files.createTempFile("books", ".json");
        String json = """
                [
                    {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":["Fiction","Drama"]},
                    {"title":"Book B","author":"Author 2","yearPublished":2021,"genres":["Drama"]}
                ]
                """;
        Files.writeString(path, json);

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "genres");

        assertEquals(2L, stats.get("Drama"));
        assertEquals(1L, stats.get("Fiction"));
        assertEquals(2, stats.size());
    }

    @Test
    void testParseFileAndCollectStats_yearPublished() throws IOException {
        Path path = Files.createTempFile("books", ".json");
        String json = """
                [
                    {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":["Fiction"]},
                    {"title":"Book B","author":"Author 2","yearPublished":2020,"genres":["Drama"]},
                    {"title":"Book C","author":"Author 3","yearPublished":2021,"genres":["Fiction"]}
                ]
                """;
        Files.writeString(path, json);

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "yearPublished");

        assertEquals(2L, stats.get("2020"));
        assertEquals(1L, stats.get("2021"));
        assertEquals(2, stats.size());
    }

    @Test
    void testParseFileAndCollectStats_unknownAuthor() throws IOException {
        Path path = Files.createTempFile("books", ".json");
        String json = """
                [
                    {"title":"Book A","yearPublished":2020,"genres":["Fiction"]}
                ]
                """;
        Files.writeString(path, json);

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "author");

        assertEquals(1L, stats.get("Unknown"));
    }

    @Test
    void testParseFileAndCollectStats_emptyArray() throws IOException {
        Path path = Files.createTempFile("empty", ".json");
        Files.writeString(path, "[]");

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "author");
        assertTrue(stats.isEmpty());
    }

    @Test
    void testParseFileAndCollectStats_invalidJson_throws() throws IOException {
        Path path = Files.createTempFile("invalid", ".json");
        Files.writeString(path, "{\"not\": \"an array\"}");

        assertThrows(IOException.class, () ->
                BookParser.parseFileAndCollectStats(path.toFile(), "author"));
    }

    @Test
    void testParseFileAndCollectStats_invalidAttribute_throws() throws IOException {
        Path path = Files.createTempFile("books", ".json");
        String json = """
                [
                    {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":["Fiction"]}
                ]
                """;
        Files.writeString(path, json);

        assertThrows(IllegalArgumentException.class, () ->
                BookParser.parseFileAndCollectStats(path.toFile(), "invalidAttribute"));
    }


    @Test
    void testParseFileAndCollectStats_nullGenres() throws IOException {
        Path path = Files.createTempFile("nullGenres", ".json");
        String json = """
                [
                    {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":null}
                ]
                """;
        Files.writeString(path, json);

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "genres");

        assertTrue(stats.isEmpty());
    }

    @Test
    void testParseFileAndCollectStats_emptyGenres() throws IOException {
        Path path = Files.createTempFile("emptyGenres", ".json");
        String json = """
                [
                    {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":[]}
                ]
                """;
        Files.writeString(path, json);

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "genres");


        assertTrue(stats.isEmpty());
    }

    @Test
    void testParseFileAndCollectStats_blankGenreStrings() throws IOException {
        Path path = Files.createTempFile("blankGenres", ".json");
        String json = """
                [
                    {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":[" ", null, "Fiction"]}
                ]
                """;
        Files.writeString(path, json);

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "genres");


        assertEquals(1L, stats.get("Fiction"));
        assertEquals(1, stats.size());
    }

    @Test
    void testParseFileAndCollectStats_blankAuthor() throws IOException {
        Path path = Files.createTempFile("blankAuthor", ".json");
        String json = """
                [
                    {"title":"Book A","author":"   ","yearPublished":2020,"genres":["Fiction"]}
                ]
                """;
        Files.writeString(path, json);

        Map<String, Long> stats = BookParser.parseFileAndCollectStats(path.toFile(), "author");


        assertEquals(1L, stats.get("Unknown"));
    }


    @Test
    void testReadBook_invalidToken_throwsIOException() throws IOException {
        Path path = Files.createTempFile("invalidBook", ".json");
        Files.writeString(path, "[123]");
        assertThrows(IOException.class, () ->
                BookParser.parseFileAndCollectStats(path.toFile(), "author")
        );
    }
}
