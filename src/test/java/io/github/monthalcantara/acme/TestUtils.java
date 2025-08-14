package io.github.monthalcantara.acme;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class TestUtils {

    private TestUtils() {}

    public static String lerPayloadDoArquivo(String nomeArquivo) throws Exception {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/payloads", nomeArquivo)));
    }
}