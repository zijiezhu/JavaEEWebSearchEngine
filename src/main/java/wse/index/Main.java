package main.java.wse.index;

import main.java.wse.FilePaths;
import main.java.wse.index.parser.CrawlParser;
import main.java.wse.index.parser.PostingParser;
import main.java.wse.util.SortUtil;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    private CrawlParser crawlParser = new CrawlParser();
    private PostingParser postingParser = new PostingParser();
    private SortUtil sortUtil = new SortUtil();
    private FilePaths filePaths = new FilePaths();


    public void generate() throws IOException {
          crawlParser.parsePages();
          System.out.println("start sort");
          sortUtil.sortByUnixSort(filePaths.UNSORTED_INTERMEDIATE_POSTING, filePaths.SORTED_INTERMEDIATE_POSTING);
          System.out.println("finish sort");
          postingParser.parsePosting(filePaths.SORTED_INTERMEDIATE_POSTING);
    }

    public static void main(String[]args) throws IOException, URISyntaxException {
        Main generator = new Main();
        generator.generate();
    }
}
