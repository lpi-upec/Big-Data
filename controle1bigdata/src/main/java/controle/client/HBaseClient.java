package controle.client;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Map;

public class HBaseClient {

	public static void main(String[] args) {
		Configuration conf = HBaseConfiguration.create();
		try {
			Connection connection = ConnectionFactory.createConnection(conf);
			TableName tableName = TableName.valueOf("controleFrequences");
			Table frequences = connection.getTable(tableName);
			Scan scan = new Scan();

			byte[] fam1 = "fam1".getBytes();
			byte[] fam2 = "fam2".getBytes();
			byte[] yearByte = Bytes.toBytes("year");
			byte[] countByte = Bytes.toBytes("count");

			scan.addColumn(fam1, yearByte);
			scan.addColumn(fam2, countByte);

			ResultScanner scanner = frequences.getScanner(scan);

			Map<String, Long> stats = new HashedMap();
			boolean first = true;
			String precedent = "";
			Long count = 0L;
			for(Result result = scanner.next(); result != null; scanner.next()) {
				if(first) {
					precedent = result.getColumnLatestCell(fam1, yearByte).toString();
					first = false;
				}
				String year = result.getColumnLatestCell(fam1, yearByte).toString();
				String subsInt = year.substring(1, 5);
				String actualYear = subsInt;
				if(precedent.equals(actualYear)) {
					count+=1;
				} else {
					count=0L;
				}
				stats.put(subsInt, count);
				scanner.close();
				frequences.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
