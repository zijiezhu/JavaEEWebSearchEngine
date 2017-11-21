package main.java.wse.index.generator;

import main.java.wse.FilePaths;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostingGenerator {

    private FilePaths filePaths = new FilePaths();

    public void writeToDisk(int docID, String contents) {
        try {
            Map<String, Integer> map = generateWordMap(contents);
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePaths.UNSORTED_INTERMEDIATE_POSTING, true));
            for (Map.Entry<String, Integer>entry: map.entrySet()) {
                writer.write(entry.getKey() + " " + docID + " " + entry.getValue());
                writer.newLine();
            }
            writer.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Map generateWordMap(String contents) {
        Map<String, Integer> map = new HashMap<>();
        String[] words = contents.split("\\W+");

        for (String word: words) {
            if (word.length() > 15) continue;
            if (!word.matches("[a-zA-Z]+")) continue;
            String unifiedWord = word.toLowerCase();
            int value = map.getOrDefault(unifiedWord, 0);
            map.put(unifiedWord, value + 1);
        }

        return map;
    }
}
