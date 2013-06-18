package edu.umd.umiacs.clip.tn.analyze;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class AnalyzeBaseline {

	public static void main(String args[]) {
		if (args.length != 4) {
			System.out
					.println("We need 4 arguments: data_size, train_size, baselineOut file, and iteration times");
			System.exit(0);
		}
		try {
			int data_size = Integer.parseInt(args[0]);
			String train_size = args[1];
			String baselineOut = args[2];
			int maxDir = Integer.parseInt(args[3]);
			for (int i = 1; i < maxDir; i++) {
				String dir = "TEST_" + data_size + "_" + train_size + "_" + i;
				FileInputStream fstream = new FileInputStream(dir + "/"
						+ baselineOut);

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;

				int match = 0;
				int count = 0;
				while ((strLine = br.readLine()) != null) {
					if (count < data_size) {
						String[] str = strLine.split("\\|");
						if (str != null && str.length == 3) {
							if (str[1].equalsIgnoreCase(str[2])) {
								match++;
							}
						}
						count++;
					}

				}
				double precision = (double) match / (double) data_size;
				double recall = precision;
				Preprocess.fileWriterWithDataDir(i + "\t" + precision + "\t" + recall + "\n",
						"precision_recall_baseline_" + data_size + "_"
								+ train_size + "", true);
				fstream.close();
				in.close();

			}

		} catch (Exception e) {

		}
	}
}
