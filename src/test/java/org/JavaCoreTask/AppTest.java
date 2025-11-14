package org.JavaCoreTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @BeforeEach
    void setup() throws IOException {
        File testFolder = Files.createTempDirectory("books").toFile();
        Files.writeString(
                new File(testFolder, "test.json").toPath(),
                "[{\"title\":\"Book A\",\"author\":\"Author 1\",\"yearPublished\":2020,\"genres\":[\"Fiction\"]}]"
        );
    }

    @Test
    void testMain_invalidArgs() throws Exception {
        App.main(new String[]{});
    }

    @Test
    void testMain_folderNotDirectory() throws Exception {
        File tempFile = File.createTempFile("notdir", ".txt");
        App.main(new String[]{tempFile.getAbsolutePath(), "author"});
    }

    @Test
    void testMain_emptyFolder() throws Exception {
        File emptyDir = Files.createTempDirectory("empty").toFile();
        App.main(new String[]{emptyDir.getAbsolutePath(), "author"});
    }

    @Test
    void testMain_validRun() throws Exception {
        File folder = Files.createTempDirectory("booksRun").toFile();
        File jsonFile = new File(folder, "book.json");

        Files.writeString(jsonFile.toPath(),
                "[{\"title\":\"Book A\",\"author\":\"Author 1\",\"yearPublished\":2020,\"genres\":[\"Fiction\"]}]");

        App.main(new String[]{folder.getAbsolutePath(), "author"});

        File xmlFile = new File("statistics_by_author.xml");
        assertTrue(xmlFile.exists());

        Files.deleteIfExists(xmlFile.toPath());
        Files.deleteIfExists(jsonFile.toPath());
        Files.deleteIfExists(folder.toPath());
    }



    @Test
    void testParseFilesAndComputeStats_invalidAttribute() throws Exception {
        File folder = Files.createTempDirectory("invalidAttrTest").toFile();
        File jsonFile = new File(folder, "book.json");

        Files.writeString(jsonFile.toPath(),
                "[{\"title\":\"Book A\",\"author\":\"Author 1\",\"yearPublished\":2020}]");

        List<File> files = List.of(jsonFile);

        Map<String, Long> stats = App.parseFilesAndComputeStats(files, 2, "genres");

        assertTrue(stats.isEmpty());

        Files.deleteIfExists(jsonFile.toPath());
        Files.deleteIfExists(folder.toPath());
    }

    @Test
    void testParseFilesAndComputeStats_ioException() throws Exception {
        File folder = Files.createTempDirectory("ioErrTest").toFile();

        File dirInsteadOfJson = new File(folder, "not_a_file.json");
        boolean created = dirInsteadOfJson.mkdir();
        assertTrue(created);

        List<File> files = List.of(dirInsteadOfJson);

        boolean thrown = false;
        try {
            App.parseFilesAndComputeStats(files, 2, "author");
        } catch (ExecutionException e) {
            thrown = true;
        }

        assertTrue(thrown);

        Files.deleteIfExists(dirInsteadOfJson.toPath());
        Files.deleteIfExists(folder.toPath());
    }
}
