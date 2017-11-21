package main.java.wse.query.parser;

import main.java.wse.FilePaths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageTableParser {
    private final FilePaths filePaths = new FilePaths();
    private final List<Integer> pageLens = new ArrayList<>();

    private double avgPageLen;
    private int pageTotal;

    public PageTableParser() {
        cachePageTable();
    }

    public void printPageTableInfo() {
        System.out.println("-------PageTable size--------: " + pageLens.size());
    }

    public double getAvgPageLen() {
        return avgPageLen;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public int getPageLen(int docID) {
        return pageLens.get(docID - 1);
    }

    private void cachePageTable() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePaths.PAGE_TABLE));
            String line;
            int docID = 0;
            long temp = 0L;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                docID = Integer.valueOf(data[0]);
                int pageLen = Integer.valueOf(data[2]);
                pageLens.add(pageLen);
                temp += pageLen;
            }
            pageTotal = docID;
            avgPageLen = temp / pageTotal;
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
