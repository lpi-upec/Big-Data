package controle;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FrequenceMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

	protected static final int YEAR = 3;
	protected static int INCRE = 0;

	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, ImmutableBytesWritable, Put>.Context context) throws IOException, InterruptedException {
		String ligne = value.toString();
		String[] cellules = ligne.split(";");

		String year = cellules[YEAR];
		year = year.substring(1, 5);
		int yearInt = 0;
		try {
			yearInt = Integer.parseInt(year);
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] keyId = generateId();

		Put put = new Put(keyId);

		byte[] family1 = "fam1".getBytes();
		byte[] col1 = "year".getBytes();

		put.addColumn(family1, col1, Integer.toString(yearInt).getBytes());

		byte[] family2 = "fam2".getBytes();
		byte[] col2 = "count".getBytes();

		put.addColumn(family2, col2, Integer.toString(INCRE).getBytes());

		context.write(new ImmutableBytesWritable(keyId), put);
	}

	protected byte[] generateId() {
		INCRE += 1;
		return Bytes.toBytes(INCRE);
	}

}
