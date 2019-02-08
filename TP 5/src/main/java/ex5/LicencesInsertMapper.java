package ex5;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LicencesInsertMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

    protected static final int CODE_POSTAL = 0;
    protected static final int CODE_FEDERATION = 2;
    protected static final int NB_LICENCES = 3;
    protected static final int POPULATION = 36;

    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, ImmutableBytesWritable, Put>.Context context) throws IOException, InterruptedException {
        if(key.get() == 0 ) {
            return;public class LicencesInsertMapper {
            }

        }
        String ligne = value.toString();
        ligne = ligne.substring(1, ligne.length()-1);
        String[] cellules = ligne.split(";");

        String codePostal = cellules[CODE_POSTAL];
        int codeFederation = -1;
        try {
            codeFederation = Integer.parseInt(cellules[CODE_FEDERATION]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int nbLicences = 0;
        try {
            nbLicences = Integer.parseInt(cellules[NB_LICENCES]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int population = 0;
        try {
            population = Integer.parseInt(cellules[POPULATION]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] rowKey = generateRawId(codePostal, codeFederation);
        Put put = new Put(rowKey);
        byte[] family1 = "geographie".getBytes();
        byte[] col1 = "codePostal".getBytes();
        put.addColumn(family1, col1, codePostal.getBytes()) ;

        byte[] col2 = "population".getBytes();
        put.addColumn(family1, col2, Integer.toString(population).getBytes()) ;

        byte[] family2 = "sport".getBytes();
        byte[] col3 = "federation".getBytes();
        put.addColumn(family2, col3, Integer.toString(codeFederation).getBytes()) ;

        byte[] col4 ="nbLicencies".getBytes();
        put.addColumn(family2, col4, Integer.toString(nbLicences).getBytes()) ;

        context.write(new ImmutableBytesWritable(rowKey), put);
    }

    /** Genere un row index "94010 ,111 **/
    protected byte[] generateRawId(String codePostal, int codeFederation) {
        byte[] b1 = Bytes.toBytes(codePostal);
        byte[] b2 = Bytes.toBytes(codeFederation);
        return Bytes.add(b1,b2);
    }
}
