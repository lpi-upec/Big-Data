package ex5.client;

import org.apache.hadoop.hbase.util.Pair;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair<Integer, Integer>> {

    @Override
    public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
        double pourcentage1 = ((p1.getSecond() * (1.0)) / (1.0) * p1.getFirst() ) *100;
        double pourcentage2 = ((p2.getSecond() * (1.0)) / (1.0) * p2.getFirst() ) *100;
        return Double.compare(pourcentage1, pourcentage2);
    }
}
