package ex4;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class LicencesReducer extends Reducer<Text, FootInfo, Text, DoubleWritable> {
    @Override
    protected void reduce(Text key, Iterable<FootInfo> values, Reducer<Text, FootInfo, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        int nbLicences = 0;
        int population = 0;
        for (FootInfo fi : values) {
            Writable[] paire = fi.get();
            nbLicences += ((IntWritable) paire[0]).get();
            population += ((IntWritable) paire[1]).get();
        }
        double pourcentage = 100 * (1.0 * nbLicences) / (1.0 * population);
        context.write(key, new DoubleWritable(pourcentage));
    }
}
