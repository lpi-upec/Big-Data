package ex3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ex3.AnagramDriver.Counter;

public class AnagramReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        List<String> result = new ArrayList<>();
        for (Text val : values) {
            result.add(val.toString());
        }
        if (result.size() > 1) {
            context.getCounter(Counter.ANAGRAM).increment(1);
            context.write(key, new Text(result.toString()));
        }
    }
}
