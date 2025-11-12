package org.JavaCoreTask.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookTest {

    @Test
    public void testBookConstructorValid() {
        List<String> genres = List.of("Fantasy", "Adventure");
        Book book = new Book("Title", "Author", 2023, genres);

        assertEquals("Title", book.title());
        assertEquals("Author", book.author());
        assertEquals(2023, book.yearPublished());
        assertEquals(List.of("Fantasy", "Adventure"), book.genres());
    }

    @Test
    public void testBookConstructorInvalidTitle() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Book("", "Author", 2023, List.of("Fantasy")));

        assertEquals("Title cannot be null or blank", exception.getMessage());
    }

    @Test
    public void testBookConstructorDefaultAuthor() {
        Book book = new Book("Title", null, 2023, List.of("Fantasy"));
        assertEquals("Unknown", book.author());
    }

    @Test
    public void testBookConstructorNullGenres() {
        Book book = new Book("Title", "Author", 2023, null);
        assertTrue(book.genres().isEmpty());
    }
}
