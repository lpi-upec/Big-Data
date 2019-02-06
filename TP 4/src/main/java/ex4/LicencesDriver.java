package ex4;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class LicencesDriver extends Configured implements Tool {

    public static final String FEDERATION_CODE = "licences.federation.code";

    public int run(String[] args) throws Exception {
        Configuration conf = super.getConf();
        conf.setInt(FEDERATION_CODE, 111);
        Job licencesJob = Job.getInstance(getConf(), "ex4.LicencesDriver");
        licencesJob.setJarByClass(getClass());
        Path inputPath = new Path("/user/hduser/seance2/licences_2012.csv");
        TextInputFormat.addInputPath(licencesJob, inputPath);
        licencesJob.setInputFormatClass(TextInputFormat.class);
        Path outputPath = new Path("/user/hduser/seance2/licences");
        TextOutputFormat.setOutputPath(licencesJob, outputPath);
        licencesJob.setOutputValueClass(TextOutputFormat.class);
        licencesJob.setMapperClass(LicencesMapper.class);
        licencesJob.setReducerClass(LicencesReducer.class);
        licencesJob.setOutputKeyClass(Text.class);
        licencesJob.setOutputValueClass(DoubleWritable.class);
        licencesJob.setMapOutputKeyClass(Text.class);
        licencesJob.setMapOutputValueClass(FootInfo.class);
        return licencesJob.waitForCompletion(true) ? 0:1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new LicencesDriver(), args);
        System.out.println(exitCode);
    }

}
