package ex1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class ListerRepertoire {

    public static void main(String[] args) {
        if (args.length == 1) {
            Path repertoire = new Path(args[0]);
            Configuration config = new Configuration();
            config.addResource(new Path("/opt/hadoop-2.9.2/etc/hadoop/core-site.xml"));
            config.addResource(new Path("/opt/hadoop-2.9.2/etc/hadoop/hdfs-site.xml"));
            try {
                FileSystem hdfs = FileSystem.get(config);
                FileStatus[] files = hdfs.listStatus(repertoire);
                for(FileStatus fileStatus : files) {
                    System.out.println(fileStatus.getPath().getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
