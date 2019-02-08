package ex5.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class HBaseClient {
    protected static final String FOOT = "111";

    public static void main(String[] args) {
        Configuration conf = HBaseConfiguration.create();
        try {
            Connection connection = ConnectionFactory.createConnection(conf);
            TableName tableName = TableName.valueOf("ex5licences");
            Table licences = connection.getTable(tableName);
            Scan scan = new Scan();
            byte[] geoByte = "geographie".getBytes();
            byte[] sportByte = "sport".getBytes();
            //HColumnDescriptor geographie = new HColumnDescriptor("geographie".getBytes());
            //HColumnDescriptor sport = new HColumnDescriptor("sport".getBytes());Bytes.toBytes("sport")
            byte[] fedeByte = Bytes.toBytes("federation");
            byte[] postalByte = Bytes.toBytes("codepostal");
            byte[] populationByte = Bytes.toBytes("population");
            byte[] nbLicencesByte = Bytes.toBytes("nblicences");
            scan.addColumn(geoByte, postalByte);
            scan.addColumn(geoByte, populationByte);

            scan.addColumn(sportByte, fedeByte);
            scan.addColumn(sportByte, nbLicencesByte);

            ResultScanner scanner = licences.getScanner(scan);

            Map<String, Pair<Integer, Integer>> stats = new HashMap();

            for (Result result = scanner.next(); result != null; scanner.next()) {
                String federation = result.getColumnLatestCell(sportByte, fedeByte).toString();
                if (FOOT.equals(federation)) {
                    String departement = result.getColumnLatestCell(geoByte, postalByte).toString().substring(0, 2);
                    Integer population = Integer.valueOf(result.getColumnLatestCell(geoByte, postalByte).toString());
                    Integer nbLicences = Integer.valueOf(result.getColumnLatestCell(geoByte, nbLicencesByte).toString());
                    Pair<Integer, Integer> values = null;
                    if (stats.containsKey(departement)) {
                        values = stats.get(departement);
                        Integer pop = population + values.getFirst();
                        Integer nbLic = nbLicences + values.getSecond();
                        values.setFirst(pop);
                        values.setSecond(nbLic);
                    } else {
                        values = new Pair<>(population, nbLicences);
                    }
                    stats.put(departement, values);
                }
            }
            PairComparator comparator = new PairComparator();
            Stream<Map.Entry<String, Pair<Integer, Integer>>> sortedStats = stats.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(comparator));
            sortedStats.forEach(System.out::println);

            scanner.close();
            licences.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
