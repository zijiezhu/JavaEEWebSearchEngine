package main.java.wse.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SnippetAlgo {

    public String generateSnippet(List<String> query, String contents) {
        if (contents == null) return null;
        String[] words = contents.split(" |\\R+");
        String[]lowerWords = contents.toLowerCase().split(" |\\R+");
        List<Integer> wordPos = new ArrayList<>();
        for (String word: query) {
            for (int i = 0; i < lowerWords.length; i ++) {
                String cur = lowerWords[i].trim();
                if (cur.contains(word)) {
                    wordPos.add(i);
                    break;
                }
            }
        }

        List<Integer> snippetPos = getPositions(wordPos, contents.length() - 1);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < snippetPos.size() - 1; i += 2) {
            int start = snippetPos.get(i);
            int end = snippetPos.get(i + 1);
            result.append("...");
            for (int j = start; j < end; j++) {
                result.append(words[j]);
                result.append(" ");
            }
        }
        result.append("...");
        return result.toString();
    }

    private static List<Integer> getPositions(List<Integer> positions, int limit) {
        if (positions == null) return null;
        Collections.sort(positions);
        List<Integer> temp1 = new ArrayList<>();
        for (Integer pos: positions) {
            temp1.add(pos - 10);
            temp1.add(pos + 10);
        }

        for (int i = 0; i < temp1.size(); i++) {
            int cur = temp1.get(i);
            if (cur < 0) temp1.set(i, 0);
            if (cur > limit) temp1.set(i, limit);
        }

        List<Integer> temp2 = new ArrayList<>();
        temp2.add(temp1.get(0));
        temp2.add(temp1.get(1));
        for (int i = 1; i < temp1.size() - 2; i += 2) {
            if (temp1.get(i) > temp1.get(i + 1)) {
                temp2.set(temp2.size() - 1, temp1.get(i + 2));
            } else {
                temp2.add(temp1.get(i + 1));
                temp2.add(temp1.get(i + 2));
            }
        }
        return temp2;
    }
}
