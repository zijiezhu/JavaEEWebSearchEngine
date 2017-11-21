package main.java.wse.query.processor;

import java.util.List;

public class QueryInfo {
    private final List<Integer> nums;//num of pages contains this word, from lexiconTable. word
    private final List<Integer> freqs;//freq of the word in that page, from InvertedIndex.

    public QueryInfo(List<Integer> nums, List<Integer> freqs) {
        this.nums = nums;
        this.freqs = freqs;
    }

    public List<Integer> getNums() {
        return nums;
    }

    public List<Integer> getFreqs() {
        return freqs;
    }
}
