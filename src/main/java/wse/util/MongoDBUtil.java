package main.java.wse.util;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBUtil {
    private static final int PORT_NUMBER = 27017;
    private static final String HOST_NAME = "localhost";
    private static final String DATABASE_NAME = "wse";
    private static final String PAGES = "pages";
    private static final String PAGE_TABLE = "pageTable";
    private static final String URL = "url";
    private static final String CONTENTS = "contents";
    private static final String DOC_ID = "docID";
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBUtil() {
        MongoClient mongoClient = new MongoClient(HOST_NAME, PORT_NUMBER);
        this.mongoClient = mongoClient;
        this.database = mongoClient.getDatabase(DATABASE_NAME);
    }

    public void close() {
        mongoClient.close();
    }

    public String searchContents(String url) {
        MongoCollection<Document> collection = this.database.getCollection(PAGES);
        Document query = new Document();
        query.put(URL, url);
        FindIterable<Document> results = collection.find(query);
        if (results == null) return null;
        return results.first().getString(CONTENTS);
    }

    public String searchUrl(int docID) {
        MongoCollection<Document> collection = this.database.getCollection(PAGE_TABLE);
        Document query = new Document();
        query.put(DOC_ID, docID);
        Document result = collection.find(query).first();
        if (result == null) return null;
        return result.getString(URL);
    }

    public void storeContents(String url, String contents) {
        MongoCollection<Document> collection = this.database.getCollection(PAGES);
        if (url == null) return;
        Document document = new Document();
        document.put(URL, url);
        document.put(CONTENTS, contents);
        collection.insertOne(document);
    }
}
