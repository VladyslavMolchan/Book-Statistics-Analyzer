package org.JavaCoreTask.parser;

import org.JavaCoreTask.model.Book;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookParserTest {

    @Test
    void testParseFile_validJson() throws IOException {
        File file = new File(Objects.requireNonNull(
                getClass().getClassLoader().getResource("books/books.json")).getFile());
        List<Book> books = BookParser.parseFile(file);

        assertFalse(books.isEmpty(), "List of books should not be empty");

        assertTrue(books.stream().anyMatch(b -> "Matt Haig".equals(b.author())),
                "Expected at least one book by Matt Haig");

        assertTrue(books.stream().anyMatch(b -> b.genres().contains("Fiction")),
                "Expected at least one Fiction book");
    }

    @Test
    void testParseFile_invalidJson() throws IOException {
        Path path = Files.createTempFile("invalid", ".json");
        Files.writeString(path, "{\"not\": \"an array\"}");

        assertThrows(IOException.class, () -> BookParser.parseFile(path.toFile()));
    }

    @Test
    void testParseFile_emptyArray() throws IOException {
        Path path = Files.createTempFile("emptyArray", ".json");
        Files.writeString(path, "[]");

        List<Book> books = BookParser.parseFile(path.toFile());
        assertTrue(books.isEmpty(), "List should be empty for empty JSON array");
    }

    @Test
    void testParseFile_multipleBooks() throws IOException {
        Path path = Files.createTempFile("multipleBooks", ".json");
        String json = """
            [
                {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":["Fiction"]},
                {"title":"Book B","author":"Author 2","yearPublished":2021,"genres":["Drama"]}
            ]
            """.stripTrailing();  // прибираємо зайві пробіли в кінці

        Files.writeString(path, json);

        List<Book> books = BookParser.parseFile(path.toFile());
        assertEquals(2, books.size(), "Should parse two books");
        assertTrue(books.stream().anyMatch(b -> b.title().equals("Book A")));
        assertTrue(books.stream().anyMatch(b -> b.title().equals("Book B")));
    }

    @Test
    void testParseFile_invalidBookInsideArray() throws IOException {
        Path path = Files.createTempFile("invalidBook", ".json");

        String json = """
            [
                {"title":"Book A","author":"Author 1","yearPublished":2020,"genres":["Fiction"]},
                {"title":]
            ]
            """.stripTrailing();  // прибираємо зайві пробіли в кінці

        Files.writeString(path, json);

        assertThrows(IOException.class, () -> BookParser.parseFile(path.toFile()));
    }
}
