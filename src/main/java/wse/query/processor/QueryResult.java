package main.java.wse.query.processor;

public class QueryResult {
    private final int docID;
    private final double score;
    private String url;
    private String snippet;

    public QueryResult(int docID, double score) {
        this.docID = docID;
        this.score = score;
        this.url = null;
        this.snippet = null;
    }

    public int getDocID() {
        return docID;
    }

    public double getScore() {
        return score;
    }

    public String getUrl() {
        return url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public void setUrl(String url) { this.url = url;}
}
