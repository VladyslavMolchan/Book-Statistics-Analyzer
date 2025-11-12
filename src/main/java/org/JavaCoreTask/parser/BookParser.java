package org.JavaCoreTask.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.JavaCoreTask.model.Book;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Book> parseFile(File file) throws IOException {
        List<Book> books = new ArrayList<>();
        try (JsonParser parser = mapper.getFactory().createParser(file)) {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IOException("Expected START_ARRAY token");
            }
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                Book book = mapper.readValue(parser, Book.class);
                books.add(book);
            }
        }
        return books;
    }
}
