package main.java.wse.query.processor;

import main.java.wse.query.parser.InvertedIndexValue;

import java.util.ArrayList;
import java.util.List;

public class ConjunctiveQueryProcessor implements QueryProcessor {
    private final QueryProcessorHelper helper = new QueryProcessorHelper();

    @Override
    public List<QueryResult> processQuery(String query) {

        List<List<Long>> metadatas = helper.openLists(query);
        if (metadatas == null || metadatas.size() == 0) return null;
        for (List<Long> metadata: metadatas) {
            if (metadata == null) return null;
        }
        int maxDocID = helper.getMaxDocID(metadatas);
        int preID = 0;
        while (preID <= maxDocID) {
            List<Integer> freqs = new ArrayList<>();
            InvertedIndexValue invertedIndexValue = helper.nextGEQ(metadatas.get(0), preID);
            preID = invertedIndexValue.getDocID();
            if (preID == -1) break;
            freqs.add(invertedIndexValue.getFreq());
            int curID = preID;
            for (int i = 1; i < metadatas.size(); i++) {
                invertedIndexValue = helper.nextGEQ(metadatas.get(i), preID);
                curID = invertedIndexValue.getDocID();
                int freq = invertedIndexValue.getFreq();
                if (curID != preID) {
                    freqs.clear();
                    break;
                }
                freqs.add(freq);
            }
            if (curID < preID) break;
            else if (curID > preID) preID = curID;
            else {
                helper.addToQueue(curID, freqs);
                preID ++;
            }
        }
        List result = helper.printResult();
        helper.closeList();
        return result;
    }
}
