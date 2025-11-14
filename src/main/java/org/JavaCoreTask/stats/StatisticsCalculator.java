package org.JavaCoreTask.stats;

import org.JavaCoreTask.model.Book;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class StatisticsCalculator {

    public static Map<String, Long> calculate(List<Book> books, String attribute) {
        if (books == null || books.isEmpty()) return Map.of();

        String attr = attribute == null ? "" : attribute.trim().toLowerCase();

        return switch (attr) {
            case "author" -> books.stream()
                    .map(b -> b.author() == null || b.author().isBlank() ? "Unknown" : b.author().trim())
                    .collect(java.util.stream.Collectors.groupingBy(
                            a -> a,
                            java.util.stream.Collectors.counting()
                    ));

            case "year", "yearpublished" -> books.stream()
                    .map(b -> String.valueOf(b.yearPublished()))
                    .collect(java.util.stream.Collectors.groupingBy(
                            y -> y,
                            java.util.stream.Collectors.counting()
                    ));

            case "genre", "genres" -> books.stream()
                    .flatMap(b -> b.genres() == null
                            ? java.util.stream.Stream.empty()
                            : b.genres().stream())
                    .filter(g -> g != null && !g.isBlank())
                    .map(String::trim)
                    .collect(java.util.stream.Collectors.groupingBy(
                            g -> g,
                            java.util.stream.Collectors.counting()
                    ));

            default -> throw new IllegalArgumentException(
                    "Unsupported attribute: " + attribute +
                            " (available: author, yearPublished, genres)"
            );
        };
    }


    public static LinkedHashMap<String, Long> sortByValueDesc(Map<String, Long> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        LinkedHashMap::putAll
                );
    }
}