package main.java.wse.query.parser;

public class InvertedIndexValue {
    private final int docID;
    private final int freq;

    public InvertedIndexValue(int docID, int freq) {
        this.docID = docID;
        this.freq = freq;
    }

    public int getDocID() {
        return docID;
    }

    public int getFreq() {
        return freq;
    }
}
