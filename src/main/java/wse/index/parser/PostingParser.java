package main.java.wse.index.parser;

import main.java.wse.index.generator.InvertedIndexGenerator;
import main.java.wse.index.generator.LexiconTableGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class PostingParser {

    private InvertedIndexGenerator invertedIndexGenerator = new InvertedIndexGenerator();
    private LexiconTableGenerator lexiconTableGenerator = new LexiconTableGenerator();

    public void parsePosting(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        String preWord = null;
        int docCount = 0;
        int postingCount = 0;

        while((line = reader.readLine())!= null) {
            String[] posting = line.split(" ");
            String word = posting[0];
            int docId = Integer.valueOf(posting[1]);
            int freq = Integer.valueOf(posting[2]);

            if (preWord == null) {
                preWord = word;
            }

            if (postingCount > 127 || !word.equals(preWord))  {
                invertedIndexGenerator.writeBlockToDisk();
                docCount += postingCount;
                postingCount = 0;
            }

            if (!word.equals(preWord)) {
                invertedIndexGenerator.writeMetaDataToDisk();

                long start = invertedIndexGenerator.MetadatasPos.get(0);
                long end = invertedIndexGenerator.MetadatasPos.get(1);
                lexiconTableGenerator.writeToDisk(preWord, start, end, docCount);

                docCount = 0;
                preWord = word;
            }

            invertedIndexGenerator.appendToBlock(docId, freq);
            postingCount ++;
        }

        invertedIndexGenerator.writeBlockToDisk();
        docCount += postingCount;
        invertedIndexGenerator.writeMetaDataToDisk();
        long start = invertedIndexGenerator.MetadatasPos.get(0);
        long end = invertedIndexGenerator.MetadatasPos.get(1);
        lexiconTableGenerator.writeToDisk(preWord, start, end, docCount);

        reader.close();
    }
}
