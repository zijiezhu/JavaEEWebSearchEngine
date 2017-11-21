package main.java.wse.query.processor;

import main.java.wse.query.parser.InvertedIndexParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DisjunctiveQueryProcessor implements QueryProcessor {
    private final QueryProcessorHelper helper = new QueryProcessorHelper();
    private final InvertedIndexParser invertedIndexParser = new InvertedIndexParser();

    @Override
    public List<QueryResult> processQuery(String query) {
        Map<Integer, QueryInfo> map = new HashMap<>();
        List<List<Long>> metadatas = helper.openLists(query);
        if (metadatas == null || metadatas.size() == 0) return null;
        //Process all words
        for (int i = 0; i < metadatas.size(); i++) {
            List<Long> metadata = metadatas.get(i);
            if (metadata == null) continue;
            int num = helper.getNums().get(i);
            //Process all the blocks of one word
             for (int j = 1; j < metadata.size() - 1; j += 3) {
                 long start = metadata.get(j);
                 long len = metadata.get(j + 1);
                 List<Integer> index = invertedIndexParser.readIndexBlock(start, (int)len);
                 //Process one block
                 for (int p = 0; p < index.size()/2; p ++) {
                     int docID = index.get(p);
                     int freq = index.get(index.size()/2 + p);
                     QueryInfo queryInfo = map.getOrDefault(docID, new QueryInfo(new ArrayList<Integer>(), new ArrayList<Integer>()));
                     queryInfo.getFreqs().add(freq);
                     queryInfo.getNums().add(num);
                     map.put(docID, queryInfo);
                 }
             }
        }

        for (Map.Entry<Integer, QueryInfo> entry: map.entrySet()) {
            helper.addToQueue(entry.getKey(), entry.getValue());
        }

        List result = helper.printResult();
        helper.closeList();
        return result;
    }
}
