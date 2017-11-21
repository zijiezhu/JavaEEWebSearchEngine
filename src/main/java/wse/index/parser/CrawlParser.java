package main.java.wse.index.parser;

import main.java.wse.index.generator.PageTableGenerator;
import main.java.wse.index.generator.PostingGenerator;
import main.java.wse.FilePaths;
import main.java.wse.util.MongoDBUtil;

import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class CrawlParser {

    private FilePaths filePaths = new FilePaths();
    private PostingGenerator postingGenerator = new PostingGenerator();
    private PageTableGenerator pageTableGenerator = new PageTableGenerator();
    //private MongoDBUtil mongoDBUtil = new MongoDBUtil();

    public void parsePages() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePaths.WETS));
        int docID = 1;
        String line;
        int count = 0;
        while((line = reader.readLine())!= null) {
            docID = parsePage(docID, line);
            count++;
            if (count > 0)break;
        }
        reader.close();
    }

    private int parsePage(int docID, String compressedFile) throws IOException {
        if (compressedFile == null) return docID;
        System.out.println("Parsing + " + compressedFile);
        FileInputStream is = new FileInputStream(compressedFile);
        ArchiveReader ar = WARCReaderFactory.get(compressedFile, is, true);

        int count = 0;
        for (ArchiveRecord r: ar) {
            if (count == 0) {
                count = 1;
                continue;
            }
            byte[] rawData = IOUtils.toByteArray(r, r.available());
            String contents = new String(rawData);
            String url = r.getHeader().getUrl();
            long pageLength = r.getHeader().getLength();

            //storeToDB
            //mongoDBUtil.store(url, contents);

            docID = writeToDisk(docID, pageLength, url, contents);
        }

        return docID;
    }

    private int writeToDisk(int docID, long pageLength, String url, String contents) {
        if (url == null || contents == null) return docID;
        postingGenerator.writeToDisk(docID, contents);
        pageTableGenerator.writeToDisk(docID, pageLength, url);
        docID ++;
        return docID;
    }
}
