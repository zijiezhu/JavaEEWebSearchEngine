package main.java.wse.query;

import main.java.wse.query.processor.ConjunctiveQueryProcessor;
import main.java.wse.query.processor.DisjunctiveQueryProcessor;
import main.java.wse.query.processor.QueryResult;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    public final ConjunctiveQueryProcessor andProcessor = new ConjunctiveQueryProcessor();

    public List andQuery(String query) {
        System.out.println("-------here-------");
        String currentPath = System.getProperty("user.dir");
        System.out.println(currentPath);
        List<QueryResult> result = andProcessor.processQuery(query);
        if (result == null) {
            System.out.println("Nothing matched!");
            return null;
        }
        return result;
    }

    public List orQuery(String query) {
        DisjunctiveQueryProcessor orProcessor = new DisjunctiveQueryProcessor();
        List<QueryResult> result = orProcessor.processQuery(query);
        if (result == null) {
            System.out.println("Nothing matched!");
            return null;
        }
        return result;
    }

    public void runQuery() {
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter the query (splitting with space) and type exit to end: ");
            String query = input.nextLine();
            if (query.equals("exit")){
                break;
            }
            andQuery(query);
        }
    }

    public static void main(String[]args) {
        System.out.println("------Start up-------");
        Main runner = new Main();
        System.out.println("------Finish start up--------");
        runner.runQuery();
    }
}
