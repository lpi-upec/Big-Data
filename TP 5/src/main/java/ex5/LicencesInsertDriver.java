package ex5;

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

public class LicencesInsertDriver extends Configured implements Tool {
    protected static Logger ex5Log = Logger.getLogger("ex5.LicencesInsertDriver");

    public static void main(String[] args) {
        try {
            int exitCode = ToolRunner.run(new LicencesInsertDriver(), args);
            System.exit(exitCode);
        } catch (Exception e) {
            ex5Log.error(e.getMessage());
        }
    }
    /**Création de la table dans HBase **/
    protected void setup(Configuration conf) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        String tableName = "licences";
        TableName table = TableName.valueOf(tableName);
        if(!admin.isTableAvailable(table)) {
            //création de la table si elle n'existe pas
            HTableDescriptor hbaseTable = new HTableDescriptor(table);
            HColumnDescriptor family1 = new HColumnDescriptor("geographie");
            hbaseTable.addFamily(family1);
            HColumnDescriptor family2 = new HColumnDescriptor("sport");
            hbaseTable.addFamily(family2);
            admin.createTable(hbaseTable);
            // TODO Utilliser TableDescriptorBuilder
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        //configurer le job Map/Reduce
        String jobName = "ex5.LicencesInsertDriver";
        Configuration conf = super.getConf();
        //ajouter les fichiers de configuration hbase
        conf.addResource(new Path("/bigdata/hbase-2.1.2/conf/hbase-site.xml"));
        this.setup(conf);
        conf.set(TableOutputFormat.OUTPUT_TABLE, "licences");
        Job hbaseJob = Job.getInstance(conf, jobName);
        hbaseJob.setJarByClass(getClass());
        Path  inputPath = new Path("/user/hduser/seance2/licences_2012.csv");
        TextInputFormat.addInputPath(hbaseJob, inputPath);
        hbaseJob.setInputFormatClass(TextInputFormat.class);

        hbaseJob.setOutputFormatClass(TableOutputFormat.class);

        hbaseJob.setOutputKeyClass(ImmutableBytesWritable.class);
        hbaseJob.setOutputValueClass(Writable.class);

        hbaseJob.setMapperClass(LicencesInsertMapper.class);
        hbaseJob.setNumReduceTasks(0);

        return hbaseJob.waitForCompletion(true) ? 0 : 1;
    }
}
