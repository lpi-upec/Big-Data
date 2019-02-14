package controle;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import java.io.IOException;

public class FrequenceDriver extends Configured implements Tool {
	protected static Logger controleLogger = Logger.getLogger("controle.FrequenceDriver");

	public static void main(String[] args) throws Exception {
		try {
			int exitCode = ToolRunner.run(new FrequenceDriver(), args);
			System.exit(exitCode);
		} catch (Exception e) {
			controleLogger.error(e.getMessage());
		}
	}

	protected void setup(Configuration conf) throws IOException {
		Connection connection = ConnectionFactory.createConnection(conf);
		Admin admin = connection.getAdmin();
		String tableName = "frequences";
		TableName table = TableName.valueOf(tableName);
		if(!admin.isTableAvailable(table)) {
			HTableDescriptor hbaseTable = new HTableDescriptor(table);
			HColumnDescriptor family1 = new HColumnDescriptor("fam1");
			HColumnDescriptor family2 = new HColumnDescriptor("fam2");
			hbaseTable.addFamily(family1);
			hbaseTable.addFamily(family2);
			admin.createTable(hbaseTable);
		}
	}

	@Override
	public int run(String[] strings) throws Exception {
		String jobName = "controle.FrequenceDriver";
		Configuration conf = super.getConf();
		conf.addResource(new Path("/bigdata/hbase-2.1.2/conf/hbase-site.xml"));
		this.setup(conf);
		conf.set(TableOutputFormat.OUTPUT_TABLE, "frequences");
		Job hbaseJob = Job.getInstance(conf, jobName);
		hbaseJob.setJarByClass(getClass());
		Path inputPath = new Path("/user/hduser/controle/BX-Books.csv");
		TextInputFormat.addInputPath(hbaseJob, inputPath);

		hbaseJob.setInputFormatClass(TextInputFormat.class);
		hbaseJob.setOutputFormatClass(TableOutputFormat.class);

		hbaseJob.setOutputKeyClass(ImmutableBytesWritable.class);
		hbaseJob.setOutputValueClass(Writable.class);

		hbaseJob.setMapperClass(FrequenceMapper.class);
		hbaseJob.setNumReduceTasks(0);

		return hbaseJob.waitForCompletion(true) ? 0 : 1;
	}
}
