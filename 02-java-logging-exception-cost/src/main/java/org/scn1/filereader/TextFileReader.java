package org.scn1.filereader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFileReader {
    private String content = null;
    private final String fullpath;

    public TextFileReader(String fullPath) {
        this.fullpath = fullPath;
    }

    public void readFileText() throws IOException {
        this.content = Files.readString(Path.of(fullpath));
    }

    public void printFileText() {
        if (this.content == null) {
            System.out.println("File not read yet");
        }
        //System.out.println("Text read: " + this.content);
    }
}