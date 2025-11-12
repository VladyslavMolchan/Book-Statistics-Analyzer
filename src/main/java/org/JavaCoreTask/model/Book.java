package org.JavaCoreTask.model;

import java.util.List;

public record Book(
        String title,
        String author,
        int yearPublished,
        List<String> genres
) {

    public Book {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (author == null) {
            author = "Unknown";
        } else {
            author = author.trim();
        }
        if (genres == null) {
            genres = List.of();
        } else {
            genres = List.copyOf(genres);
        }
    }
}
