package edu.umd.umiacs.clip.tn.preprocess;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class CleanPSLOutput {
	public static void main(String args[]) {
		try {

			if (args.length != 4) {
				System.out
						.println("We need 4 arguments isComparable.txt name threshold aligned_pair");
				System.exit(0);
			}

			FileInputStream fstream = new FileInputStream(args[0]);// args[0]
			double threshold = Double.parseDouble(args[2]);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			TreeMap<Double, String> rank = new TreeMap<Double, String>();
			double verySmall = 0.000000001;
			double count = 1.0;
			HashMap<String, Double> maxScore = new HashMap<String, Double>();// only
																				// keep
																				// the
			while ((strLine = br.readLine()) != null) {
				// System.out.println(strLine+"XXXXXXXXXXXXXXXXX");
				if (strLine.startsWith("isComparable")) {
					// System.out.println(strLine);
					String newStr[] = strLine.replace("isComparable(", "")
							.replace(", ", "|").replace(") V=[", "|")
							.replace("]", "").split("\\|");
					//
					// System.out.println(newStr[0]);
					double score = Double.parseDouble(newStr[2]);
					score = 2.0 - score - verySmall * count++;
					if (maxScore.containsKey(newStr[0])) {
						//select smaller value
						if (maxScore.get(newStr[0]) > score) {
							maxScore.put(newStr[0], score);
						}
					} else {
						maxScore.put(newStr[0], score);
					}
					rank.put(score, newStr[0] + "\t" + newStr[1]);
				}

			}

			fstream.close();
			in.close();

			// read name
			fstream = new FileInputStream(args[1]);// args[1]
			String aligned_pair = args[3];
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			HashMap<String, String> names = new HashMap<String, String>();// index
																			// -name
			while ((strLine = br.readLine()) != null) {
				String[] name_index = strLine.split("\\t");
				names.put(name_index[0], name_index[1]);
			}

			fstream.close();
			in.close();

			//
			Set<Double> rankSet = rank.keySet();
			TreeMap<String, String> title_title = new TreeMap<String, String>();
			HashMap<String, Boolean> dePrinted = new HashMap<String, Boolean>();
			for (double d : rankSet) {
				if (d < (2 - threshold)) {//if score >threshold
					String index[] = rank.get(d).split("\\t");

					String title_en = names.get(index[0]);
					String title_de = names.get(index[1]);
					if (!title_title.containsKey(title_en)
							&& (d == maxScore.get(index[0]) && !dePrinted
									.containsKey(index[1]))) {
						title_title.put(title_en, title_de);
						dePrinted.put(index[1], true);
					}
				}
			}

			Set<String> titleSet = title_title.keySet();
			for (String t : titleSet) {
				Preprocess.fileWriter(t + "\t" + title_title.get(t) + "\n",
						aligned_pair, true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
