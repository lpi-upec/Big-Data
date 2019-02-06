package ex3;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;

import ex3.AnagramDriver.Counter;

public class AnagramMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        String mot = value.toString();
        char[] tabLettres = mot.toCharArray();
        Arrays.sort(tabLettres);
        String cle = new String(tabLettres);
        context.write(new Text(cle), value);
        context.getCounter(Counter.WORD).increment(1);
    }
}
