package ex3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class AnagramDriver extends Configured implements Tool {

    public enum Counter {
        WORD, ANAGRAM
    }

    public int run(String[] args) throws Exception {
        Configuration conf = super.getConf();
        Job anagramJob = Job.getInstance(conf);
        anagramJob.setJobName("ex3.AnagramDriver");
        anagramJob.setJarByClass(getClass());
        Path inputPat = new Path("/user/hduser/seance4/liste_mots_francais.txt");
        TextInputFormat.addInputPath(anagramJob, inputPat);
        anagramJob.setInputFormatClass(TextInputFormat.class);
        Path outputPath = new Path("/user/hduser/seance4/anagram");
        TextOutputFormat.setOutputPath(anagramJob, outputPath);
        anagramJob.setOutputFormatClass(TextOutputFormat.class);
        anagramJob.setMapperClass(AnagramMapper.class);
        anagramJob.setReducerClass(AnagramReducer.class);
        anagramJob.setOutputKeyClass(Text.class);
        anagramJob.setOutputValueClass(Text.class);
        return anagramJob.waitForCompletion(true) ? 0:1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new AnagramDriver(), args);
        System.out.println(exitCode);
    }
}
