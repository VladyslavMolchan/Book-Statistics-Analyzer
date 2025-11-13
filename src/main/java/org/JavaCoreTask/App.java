package org.JavaCoreTask;

import org.JavaCoreTask.parser.BookParser;
import org.JavaCoreTask.stats.StatisticsCalculator;
import org.JavaCoreTask.writer.XmlStatisticsWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class App {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        if (args.length != 2) {
            System.out.println("Usage: java -jar book-statistics.jar <folder-path> <attribute>");
            return;
        }

        String folderPath = args[0];
        String attribute = args[1];

        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            System.err.println("Folder path is not a directory.");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.err.println("No JSON files found in the folder.");
            return;
        }

        int[] threadCounts = {1, 2, 4, 8};


        for (int threads : threadCounts) {
            long start = System.currentTimeMillis();
            Map<String, Long> stats = parseFilesAndComputeStats(List.of(files), threads, attribute);
            long end = System.currentTimeMillis();

            StatisticsCalculator.sortByValueDesc(stats);
            System.out.println("Threads: " + threads + ", Time: " + (end - start) + "ms");
        }


        Map<String, Long> finalStats = parseFilesAndComputeStats(List.of(files), 4, attribute);
        Map<String, Long> sortedStats = StatisticsCalculator.sortByValueDesc(finalStats);

        XmlStatisticsWriter.write(attribute, sortedStats);
        System.out.println("Statistics written to statistics_by_" + attribute + ".xml");
    }


    static Map<String, Long> parseFilesAndComputeStats(List<File> files, int threads, String attribute)
            throws InterruptedException, ExecutionException {
        Map<String, Long> combinedStats = new HashMap<>();

        try (ExecutorServiceWrapper executor = new ExecutorServiceWrapper(Executors.newFixedThreadPool(threads))) {
            List<Future<Map<String, Long>>> futures = new ArrayList<>();

            for (File file : files) {
                futures.add(executor.submit(() -> {
                    try {
                        // Виклик нового методу Streaming API
                        return BookParser.parseFileAndCollectStats(file, attribute);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
            }

            for (Future<Map<String, Long>> future : futures) {
                Map<String, Long> partial = future.get();
                partial.forEach((key, value) ->
                        combinedStats.merge(key, value, Long::sum)
                );
            }
        }

        return combinedStats;
    }



    static class ExecutorServiceWrapper implements AutoCloseable {
        private final ExecutorService executor;

        public ExecutorServiceWrapper(ExecutorService executor) {
            this.executor = executor;
        }

        public <T> Future<T> submit(Callable<T> task) {
            return executor.submit(task);
        }

        @Override
        public void close() throws InterruptedException {
            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.err.println("Executor did not terminate in time. Forcing shutdown...");
                executor.shutdownNow();
            }
        }
    }
}
