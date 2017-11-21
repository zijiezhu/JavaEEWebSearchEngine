package main.java.wse.index.generator;

import main.java.wse.FilePaths;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PageTableGenerator {

    private FilePaths filePaths = new FilePaths();

    public void writeToDisk(int docId, long pageLength, String url) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePaths.PAGE_TABLE, true));
            writer.write(docId + " " + url + " " + pageLength);
            writer.newLine();
            writer.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
