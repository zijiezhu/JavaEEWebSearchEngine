package main.java.wse.query.parser;

import main.java.wse.FilePaths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LexiconParser {

    private FilePaths filePaths = new FilePaths();
    private Map<String, LexiconValue> map = new HashMap<>();

    public LexiconParser() {
        cacheLexicon();
    }

    public LexiconValue getLexiconValue(String word) {
        LexiconValue lexiconValue = map.getOrDefault(word, null);
        return lexiconValue;
    }

    public void printLexiconInfo() {
        if (map == null) System.out.println("-------Lexicon null map--------");
        else System.out.println("-------Lexicon Map Size-------" + map.size());
    }

    private void cacheLexicon() {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePaths.LEXICON_TABLE));
            String line;
            while((line = reader.readLine())!= null) {
                String[] data = line.split(" ");
                String word = data[0];
                long start = Long.valueOf(data[1]);
                int len  = Integer.valueOf(data[2]);
                int amount = Integer.valueOf(data[3]);//amount of pages that contains this word
                LexiconValue lexiconValue = new LexiconValue(start, len, amount);
                map.put(word, lexiconValue);
            }
            reader.close();
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
