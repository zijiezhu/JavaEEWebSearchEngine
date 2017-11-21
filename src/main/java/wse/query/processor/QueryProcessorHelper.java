package main.java.wse.query.processor;

import main.java.wse.query.RankAlgo;
import main.java.wse.query.SnippetAlgo;
import main.java.wse.query.parser.InvertedIndexParser;
import main.java.wse.query.parser.InvertedIndexValue;
import main.java.wse.query.parser.LexiconParser;
import main.java.wse.query.parser.LexiconValue;
import main.java.wse.query.parser.PageTableParser;
import main.java.wse.util.MongoDBUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class QueryProcessorHelper {

    private final PriorityQueue<QueryResult> queue = new PriorityQueue<>(10, new Comparator<QueryResult>(){
        public int compare(QueryResult q1, QueryResult q2) {
            if (q2.getScore() > q1.getScore()) return 1;
            else if (q2.getScore() == q1.getScore()) return 0;
            return -1;
        }
    });

    private LexiconParser lexiconParser = new LexiconParser();
    private PageTableParser pageTableParser = new PageTableParser();
    private InvertedIndexParser invertedIndexParser = new InvertedIndexParser();
    private RankAlgo rankAlgo = new RankAlgo(pageTableParser.getPageTotal(), pageTableParser.getAvgPageLen());
    private SnippetAlgo snippetAlgo = new SnippetAlgo();
    private List<Integer> nums = new ArrayList<>();
    private List<String> words = new ArrayList<>();

    public List<List<Long>> openLists(String query) {
        List<List<Long>> metadatas = new ArrayList<>();
        String[] words = splitWord(query);
        for (String word: words) {
            this.words.add(word);
            List<Long> metadata = openList(word);
            metadatas.add(metadata);
        }
        return metadatas;
    }

    public void closeList() {
        nums.clear();
        queue.clear();
        words.clear();
    }

    public InvertedIndexValue nextGEQ(List<Long>metadata, int did) {
        long start = 0;
        int len =0;
        for (int i = 0; i < metadata.size() - 2; i += 3) {
            long cur = metadata.get(i);
            if ((int) cur >= did) {
                start = metadata.get(i + 1);
                long tempLen = metadata.get(i + 2);
                len = (int)tempLen;
                break;
            }
        }
        if (len == 0) return new InvertedIndexValue(-1, -1);
        List<Integer> index = invertedIndexParser.readIndexBlock(start, len);
        int size = index.size()/2;
        int docID = index.get(size - 1);
        int freq = index.get(index.size() - 1);
        for (int i = 0; i < size; i++) {
            docID = index.get(i);
            if (docID >= did) {
                freq = index.get(size + i);
                break;
            }
        }
        InvertedIndexValue invertedIndexValue = new InvertedIndexValue(docID, freq);
        return invertedIndexValue;
    }

    public int getMaxDocID(List<List<Long>> metadatas) {
        long max = 0;
        for(int i = 0; i < metadatas.size(); i++) {
            List<Long> metadata = metadatas.get(i);
            long value = metadata.get(metadata.size() - 3);
            if (value > max) max = value;
        }
        return (int)max;
    }


    public void addToQueue(int docID, List<Integer> freqs) {
        QueryInfo queryInfo = new QueryInfo(nums, freqs);
        QueryResult queryResult = generateQueryResult(docID, queryInfo);
        queue.add(queryResult);
    }

    public void addToQueue(int docID, QueryInfo queryInfo) {
        QueryResult queryResult = generateQueryResult(docID, queryInfo);
        queue.add(queryResult);
    }

    public List printResult() {
        List<QueryResult> result =  generateTop10();

        System.out.println("------printResult--------");
        for (QueryResult queryResult: result) {
            int docID = queryResult.getDocID();
            double score = queryResult.getScore();
            String url = queryResult.getUrl();
            String snippet = queryResult.getSnippet();
            System.out.println("--------------");
            System.out.println("DocID: " + docID + " Score: " + score);
            System.out.println("Url: " + url );
            System.out.println("Snippet: " + snippet);
        }

        return result;
    }

    public PriorityQueue<QueryResult> getQueue() {
        return queue;
    }

    public List<Integer> getNums() {
        return nums;
    }

    private String[] splitWord(String query) {
        String[] words = query.split(" ");
        return words;
    }

    private List<Long> openList(String word) {
        LexiconValue lexiconValue = lexiconParser.getLexiconValue(word);
        if (lexiconValue == null) {
            nums.add(-1);
            return null;
        }
        long start = lexiconValue.getStart();
        int len = lexiconValue.getLen();
        nums.add(lexiconValue.getAmount());
        List<Long> list = invertedIndexParser.readDisk(start, len);
        if (list.size() % 3 != 0) list.add(1, 0L); //compression will
        return list;
    }

    private QueryResult generateQueryResult(int docID, QueryInfo queryInfo) {
        int pageLen = pageTableParser.getPageLen(docID);
        double score = rankAlgo.calBM25Score(queryInfo, pageLen);

        QueryResult queryResult = new QueryResult(docID, score);
        return queryResult;
    }

    private List generateTop10() {
        int count = 0;
        List<QueryResult> result = new ArrayList<>();
        while(!queue.isEmpty() && count < 10) {
            QueryResult queryResult = queue.poll();
            result.add(queryResult);
            count ++;
        }

        MongoDBUtil mongoDBUtil = new MongoDBUtil();
        for (QueryResult queryResult: result) {
            String url = mongoDBUtil.searchUrl(queryResult.getDocID());
            String contents = mongoDBUtil.searchContents(url);
            String snippet = snippetAlgo.generateSnippet(words, contents);
            queryResult.setUrl(url);
            queryResult.setSnippet(snippet);
        }
        mongoDBUtil.close();

        return result;
    }
}
