package ex4;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LicencesMapper extends Mapper<LongWritable, Text, Text, FootInfo> {

    public static final int FEDERATION = 2;
    public static final int NB_LICENCES = 3;
    public static final int POPULATION = 36;
    public static final int COG2 = 0;

    public enum Line {
        MALFORMED
    }

    private int code;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        code = context.getConfiguration().getInt(LicencesDriver.FEDERATION_CODE, 0);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() == 0) {
            return;
        }
        String ligne = value.toString();
        String contenu = ligne.substring(1, ligne.length() - 1);
        String[] cellules = contenu.split(";");
        int codeCourant = 0;
        try {
            String departement = cellules[COG2].substring(0, 2);
            codeCourant = Integer.parseInt(cellules[FEDERATION]);
            if (codeCourant != code) {
                return;
            }
            int nbLicences = Integer.parseInt(cellules[NB_LICENCES]);
            int population = Integer.parseInt(cellules[POPULATION]);
            FootInfo info = new FootInfo();
            IntWritable[] values = new IntWritable[2];
            values[0] = new IntWritable(nbLicences);
            values[1] = new IntWritable(population);
            info.set(values);
            context.write(new Text(departement), info);
        } catch (NumberFormatException e) {
            context.getCounter(Line.MALFORMED).increment(1);
        }

    }
}
