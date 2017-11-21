package main.java.wse.query.parser;

public class LexiconValue {
    private long start;
    private int len;
    private int amount;

    public LexiconValue(long start, int len, int amount) {
        this.start = start;
        this.len = len;
        this.amount = amount;
    }

    public long getStart() {
        return start;
    }

    public int getLen() { return len; }

    public int getAmount() {
        return amount;
    }
}
