package com.william.dev.f1stats.data.db.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestDataReader {

    public static String readData(final String filename) {
        final StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> contentStream = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)) {
            contentStream.forEach(contentBuilder::append);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
