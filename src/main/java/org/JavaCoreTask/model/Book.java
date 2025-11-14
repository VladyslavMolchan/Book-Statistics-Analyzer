package org.JavaCoreTask.model;

import java.util.List;
import java.util.Objects;

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
            genres = genres.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .toList();
        }
    }
}