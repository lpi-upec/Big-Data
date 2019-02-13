package ex6;

import org.apache.log4j.Logger;
import org.apache.pig.ExecType;
import org.apache.pig.PigRunner;
import org.apache.pig.PigServer;
import org.apache.pig.tools.pigstats.PigStats;

import java.io.InputStream;
import java.util.Properties;

public class LicencesLillePerFederation {

	protected static Logger ex6Log = Logger.getLogger(LicencesLillePerFederation.class);

	public static void main(String[] args) {
		ex6Log.info("ex6.ex6.LicencesLillePerFederation start");
		Properties properties = new Properties();

		properties.setProperty("fs.default.name", "hdfs://localhost:8020");
		properties.setProperty("mapred.job.tracker", "http://localhost:10020");

		try {
			PigServer pigServer = new PigServer(ExecType.MAPREDUCE);
			InputStream pigStream = LicencesLillePerFederation.class.getClassLoader().getResourceAsStream("licences_lille_per_federation.pig");
			pigServer.registerScript(pigStream);
			PigStats pigStats = PigRunner.run(args, null);
		} catch (Exception e) {
			ex6Log.error(e.getMessage());
		}
	}
}
