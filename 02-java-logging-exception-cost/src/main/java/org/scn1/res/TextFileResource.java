package org.scn1.res;

import org.scn1.filereader.TextFileReader;
import java.io.IOException;

public class TextFileResource {
    private final TextFileReader fileReader;

    public TextFileResource(String path, String fileName) {
        this.fileReader = new TextFileReader(path + "/" + fileName);
    }

    // With println
    public void printText() throws IOException {
        fileReader.readFileText();
        fileReader.printFileText();
    }

    // No println — for insulated benchmark
    public void readText() throws IOException {
        fileReader.readFileText();
    }
}