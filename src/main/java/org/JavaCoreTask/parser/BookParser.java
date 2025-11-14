package org.JavaCoreTask.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.JavaCoreTask.model.Book;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookParser {

    public static Map<String, Long> parseFileAndCollectStats(File file, String attribute) throws IOException {
        Map<String, Long> stats = new HashMap<>();
        JsonFactory factory = new JsonFactory();

        try (JsonParser parser = factory.createParser(file)) {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IOException("Expected START_ARRAY token");
            }

            while (parser.nextToken() != JsonToken.END_ARRAY) {

                Book book = readBook(parser);

                switch (attribute.toLowerCase()) {
                    case "author" -> {
                        String author = book.author() == null || book.author().isBlank()
                                ? "Unknown" : book.author().trim();
                        stats.merge(author, 1L, Long::sum);
                    }
                    case "year", "yearpublished" -> {
                        String year = String.valueOf(book.yearPublished());
                        stats.merge(year, 1L, Long::sum);
                    }
                    case "genre", "genres" -> {
                        if (book.genres() != null) {
                            book.genres().stream()
                                    .filter(g -> g != null && !g.isBlank())
                                    .map(String::trim)
                                    .forEach(g -> stats.merge(g, 1L, Long::sum));
                        }
                    }
                    default -> throw new IllegalArgumentException(
                            "Unsupported attribute: " + attribute +
                                    " (available: author, yearPublished, genres)"
                    );
                }
            }
        }

        return stats;
    }


    private static Book readBook(JsonParser parser) throws IOException {
        String title = null;
        String author = null;
        int yearPublished = 0;
        List<String> genres = new ArrayList<>();

        if (parser.currentToken() != JsonToken.START_OBJECT) {
            throw new IOException("Expected START_OBJECT for a book");
        }

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            parser.nextToken();

            switch (fieldName) {
                case "title" -> title = parser.getValueAsString();
                case "author" -> author = parser.getValueAsString();
                case "yearPublished" -> yearPublished = parser.getIntValue();
                case "genres" -> {
                    if (parser.currentToken() == JsonToken.START_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            genres.add(parser.getValueAsString());
                        }
                    }
                }
            }
        }

        return new Book(title, author, yearPublished, genres);
    }
}