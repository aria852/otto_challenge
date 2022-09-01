package arn.otto.challenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtil {
    public static String readTestData(String fileName) throws IOException {
        return Files.readString(Paths.get("./src/test/resources/testdata/" + fileName), UTF_8);
    }
}
