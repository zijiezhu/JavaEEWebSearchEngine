package main.java.wse.index.generator;

import main.java.wse.FilePaths;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LexiconTableGenerator {

    private FilePaths filePaths = new FilePaths();

    public void writeToDisk(String word, long start, long len, int amount) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePaths.LEXICON_TABLE, true));
            writer.write(word + " " + start + " " + len + " " + amount);
            writer.newLine();
            writer.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
