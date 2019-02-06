package ex2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final IntWritable once = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        String ligne = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(ligne);
        while (tokenizer.hasMoreTokens()) {
            String mot = tokenizer.nextToken();
            context.write(new Text(mot), once);
        }
    }
}
