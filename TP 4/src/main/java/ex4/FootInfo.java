package ex4;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;

public class FootInfo extends ArrayWritable {

    public FootInfo() {
        super(IntWritable.class);
    }

}
