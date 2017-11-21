package main.java.wse.query;

import main.java.wse.query.processor.QueryInfo;

import java.util.List;

public class RankAlgo {
    private static final double K1 = 1.2;
    private static final double B = 0.75;
    private final int pageTotal;
    private final double avgPageLen;

    public RankAlgo(int pageTotal, double avgPageLen) {
        this.pageTotal = pageTotal;
        this.avgPageLen = avgPageLen;
    }

    public double calBM25Score(QueryInfo queryInfo, double pageLen) {
        double score = 0.0;
        double k = calK(pageLen);
        List<Integer> nums = queryInfo.getNums();
        List<Integer> freqs= queryInfo.getFreqs();
        for (int i = 0; i < nums.size(); i++) {
            int num = nums.get(i);
            int freq = freqs.get(i);
            double temp1 = (pageTotal - num + 0.5) / (num + 0.5);
            double temp2 = (K1 + 1) * freq / (k + freq);
            score += Math.log(temp1) * temp2;
        }
        return score;
    }

    private double calK(double pageLen) {
        double k = K1 * ((1 - B) + B * (pageLen/avgPageLen));
        return k;
    }

}
