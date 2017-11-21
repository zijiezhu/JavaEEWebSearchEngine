package main.java.wse.query.processor;

import java.util.List;

public interface QueryProcessor {
    List<QueryResult> processQuery(String query);
}
