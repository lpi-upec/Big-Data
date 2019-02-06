package ex2;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCountDriver extends Configured implements Tool {
    public static Logger ex2log = LoggerFactory.getLogger(WordCountDriver.class);

    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(getConf(), "ex2.ex2.WordCountDriver");
        job.setJarByClass(getClass());
        Path inputPath = new Path("/user/hduser/seance3/sherlock.txt");
        TextInputFormat.addInputPath(job, inputPath);
        job.setInputFormatClass(TextInputFormat.class);

        Path outputPath = new Path("/user/hduser/seance3/wordCount");
        TextOutputFormat.setOutputPath(job, outputPath);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setCombinerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        return job.waitForCompletion(true) ? 0 : 1;

    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordCountDriver(), args);
        ex2log.info("end of wordcountdriver " + exitCode);
    }
}
