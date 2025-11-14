package org.JavaCoreTask.stats;

import org.JavaCoreTask.model.Book;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsCalculatorTest {

    @Test
    public void testCalculateStatisticsByAuthor() {
        Book book1 = new Book("Book1", "Author1", 2021, List.of("Fantasy"));
        Book book2 = new Book("Book2", "Author1", 2022, List.of("Sci-Fi"));
        Book book3 = new Book("Book3", "Author2", 2021, List.of("Fantasy"));

        List<Book> books = List.of(book1, book2, book3);

        Map<String, Long> result = StatisticsCalculator.calculate(books, "author");

        assertEquals(2, result.get("Author1"));
        assertEquals(1, result.get("Author2"));
    }

    @Test
    public void testCalculateStatisticsByYear() {
        Book book1 = new Book("Book1", "Author1", 2021, List.of("Fantasy"));
        Book book2 = new Book("Book2", "Author1", 2022, List.of("Sci-Fi"));
        Book book3 = new Book("Book3", "Author2", 2021, List.of("Fantasy"));

        List<Book> books = List.of(book1, book2, book3);

        Map<String, Long> result = StatisticsCalculator.calculate(books, "year");

        assertEquals(2, result.get("2021"));
        assertEquals(1, result.get("2022"));
    }

    @Test
    public void testCalculateStatisticsByGenre() {
        Book book1 = new Book("Book1", "Author1", 2021, List.of("Fantasy"));
        Book book2 = new Book("Book2", "Author1", 2022, List.of("Sci-Fi"));
        Book book3 = new Book("Book3", "Author2", 2021, List.of("Fantasy"));

        List<Book> books = List.of(book1, book2, book3);

        Map<String, Long> result = StatisticsCalculator.calculate(books, "genre");

        assertEquals(2, result.get("Fantasy"));
        assertEquals(1, result.get("Sci-Fi"));
    }

    @Test
    public void testSortByValueDesc() {
        Map<String, Long> stats = Map.of("Fantasy", 2L, "Sci-Fi", 1L, "Adventure", 3L);
        Map<String, Long> sorted = StatisticsCalculator.sortByValueDesc(stats);

        assertEquals("Adventure", sorted.keySet().toArray()[0]);
        assertEquals("Fantasy", sorted.keySet().toArray()[1]);
        assertEquals("Sci-Fi", sorted.keySet().toArray()[2]);
    }


    @Test
    public void testCalculateWithNullBooks() {
        Map<String, Long> result = StatisticsCalculator.calculate(null, "author");
        assertEquals(0, result.size());
    }

    @Test
    public void testCalculateWithEmptyBooks() {
        Map<String, Long> result = StatisticsCalculator.calculate(List.of(), "year");
        assertEquals(0, result.size());
    }

    @Test
    public void testCalculateWithUnsupportedAttribute() {
        Book book = new Book("Book1", "Author1", 2021, List.of("Fantasy"));
        List<Book> books = List.of(book);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatisticsCalculator.calculate(books, "unknown")
        );

        assertEquals("Unsupported attribute: unknown (available: author, yearPublished, genres)",
                exception.getMessage());
    }

    @Test
    public void testCalculateWithNullAttribute() {
        Book book = new Book("Book1", "Author1", 2021, List.of("Fantasy"));
        List<Book> books = List.of(book);

        assertThrows(
                IllegalArgumentException.class,
                () -> StatisticsCalculator.calculate(books, null)
        );
    }


    @Test
    public void testCalculateWithNullGenres() {
        Book book1 = new Book("Book1", "Author1", 2021, null);
        Book book2 = new Book("Book2", "Author2", 2022, List.of(""));
        List<Book> books = List.of(book1, book2);

        Map<String, Long> result = StatisticsCalculator.calculate(books, "genres");


        assertEquals(0, result.size());
    }


    @Test
    void testCalculateWithBlankAuthorsAndGenres() {
        Book book1 = new Book("Book1", "   ", 2021, List.of("", "  "));
        Book book2 = new Book("Book2", null, 2022, null);
        List<Book> books = List.of(book1, book2);

        Map<String, Long> resultAuthor = StatisticsCalculator.calculate(books, "author");
        assertEquals(2, resultAuthor.get("Unknown")); // null or blank author → Unknown

        Map<String, Long> resultGenres = StatisticsCalculator.calculate(books, "genres");
        assertEquals(0, resultGenres.size()); // only null/blank genres → empty map
    }

    @Test
    void testCalculateWithAttributeYearPublished() {
        Book book = new Book("Book1", "Author1", 2023, List.of("Fantasy"));
        List<Book> books = List.of(book);

        Map<String, Long> result = StatisticsCalculator.calculate(books, "yearpublished");
        assertEquals(1, result.get("2023"));
    }

    @Test
    void testCalculateWithEmptyStringAttribute() {
        Book book = new Book("Book1", "Author1", 2023, List.of("Fantasy"));
        List<Book> books = List.of(book);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> StatisticsCalculator.calculate(books, ""));
        assertTrue(ex.getMessage().contains("Unsupported attribute"));
    }

}