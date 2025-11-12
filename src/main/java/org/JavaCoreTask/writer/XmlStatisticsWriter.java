package org.JavaCoreTask.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class XmlStatisticsWriter {

    public static void write(String attribute, Map<String, Long> stats) throws IOException {
        String fileName = "statistics_by_" + attribute + ".xml";

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8)) {
            writer.write("<statistics>\n");

            for (Map.Entry<String, Long> entry : stats.entrySet()) {
                StringBuilder itemXml = new StringBuilder();
                itemXml.append("  <item>\n")
                        .append("    <value>").append(escapeXml(entry.getKey())).append("</value>\n")
                        .append("    <count>").append(entry.getValue()).append("</count>\n")
                        .append("  </item>\n");
                writer.write(itemXml.toString());
            }

            writer.write("</statistics>\n");
        }
    }

    public static String escapeXml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
