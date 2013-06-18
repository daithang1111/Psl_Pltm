package edu.umd.umiacs.clip.tn.analyze;

import java.io.File;
import java.util.ArrayList;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class analyzeAllData {

	public static void main(String args[]) {
		if (args.length == 6) {

			int sample_size = Integer.parseInt(args[0]);
			int train_size = Integer.parseInt(args[1]);
			int random_size = Integer.parseInt(args[2]);
			int num_of_topics = Integer.parseInt(args[3]);
			int loop_number = Integer.parseInt(args[4]);
			String outfile = args[5];
			for (int i = 1; i < loop_number + 1; i++) {
				String fileName = "TEST_" + sample_size + "_" + train_size
						+ "_" + random_size + "_" + num_of_topics + "/"
						+ "tmp_" + i + "/aligned_pair";
				File f =new File(fileName);
				if(!f.exists()){
					System.out.println("No aligned_pair file exists");
					System.exit(0);
				}
				ArrayList<String> title_pair = Preprocess.readFile(fileName);

				int count = 0;
				for (int j = 0; j < title_pair.size(); j++) {
					String[] tmp = title_pair.get(j).split("\\t");
					if (tmp != null && tmp.length == 2) {
						if (tmp[0].replace("_en_content", "").equalsIgnoreCase(
								tmp[1].replace("_de_content", ""))) {
							count++;
						}
					}
				}

				double precision = (double) count / (double) title_pair.size();
				double recall = (double) count / (double) (sample_size-train_size);
				Preprocess.fileWriter(i + "\t" + precision + "\t" + recall + "\n",
						outfile, true);
			}

		} else {
			System.out
					.println("We need 5 arguments\n sample_size, train_size, random_size, num_of_topics, loop_number,outputfile");
		}

	}
}
