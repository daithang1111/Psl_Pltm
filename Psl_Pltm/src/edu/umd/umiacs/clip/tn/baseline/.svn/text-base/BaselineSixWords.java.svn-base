package edu.umd.umiacs.clip.tn.baseline;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class BaselineSixWords {
	// public static final int blockingSize =40;
	public static void main(String args[]) {
		if (args.length != 3) {
			System.out
					.println("We need three parameters, fully_aligned_en, fully_aligned_de, outputFile");
			System.exit(0);
		}
		try {
			String enTitle = args[0];// "fully_aligned_en";// args[0];
			String deTitle = args[1];// "fully_aligned_de";//args[1];
			String outputFile = args[2];
			ArrayList<String> enArticles = new ArrayList<String>();
			ArrayList<String> deArticles = new ArrayList<String>();

			FileInputStream fstream = new FileInputStream(enTitle);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] title_label_content = strLine.split("\\t");
				if (title_label_content != null
						&& title_label_content.length == 3) {
					enArticles.add(strLine);
				}
			}
			fstream.close();
			in.close();
			fstream = new FileInputStream(deTitle);

			// load german articles
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] title_label_content = strLine.split("\\t");
				if (title_label_content != null
						&& title_label_content.length == 3) {
					deArticles.add(strLine);
				}
			}

			fstream.close();
			in.close();

			// compare base line
			// number of words which have length >6
			TreeMap<Double, String> matchTree = new TreeMap<Double, String>();
			double small = 0.0000001;
			Pattern p = Pattern.compile("[^a-zA-Z0-9]");
			for (int i = 0; i < enArticles.size(); i++) {
				String ena[] = enArticles.get(i).split(" ");
				// get word length >6
				HashMap<String, Boolean> tmpHashEna = new HashMap<String, Boolean>();
				for (int ii = 0; ii < ena.length; ii++) {
					String tmpena = ena[ii].toLowerCase().trim();
					boolean hasSpecialChar = p.matcher(tmpena).find();
					if (!hasSpecialChar && tmpena.length() > 6) {
						tmpHashEna.put(tmpena, true);
					}

				}

				for (int j = 0; j < deArticles.size(); j++) {
					String dea[] = deArticles.get(j).split(" ");
					// get word length >6
					HashMap<String, Boolean> tmpHashDea = new HashMap<String, Boolean>();
					for (int jj = 0; jj < dea.length; jj++) {
						String tmpdea = dea[jj].toLowerCase().trim();
						boolean hasSpecialChar = p.matcher(tmpdea).find();
						if (!hasSpecialChar && tmpdea.length() > 6) {
							tmpHashDea.put(tmpdea, true);
						}

					}

					int match = 0;
					Set<String> tmpSetDea = tmpHashDea.keySet();
					for (String s : tmpSetDea) {
						if (tmpHashEna.containsKey(s)) {
							match++;
						}
					}

					double tmpKey = (double) (i * deArticles.size() + j)
							* small + ((double) (match));
					matchTree.put(-tmpKey, i + "\t" + j);
				}
			}

			// print
			Set<Double> matchSet = matchTree.keySet();
			for (double d : matchSet) {
				Preprocess.fileWriter(d + "\t" + matchTree.get(d) + "\n",
						outputFile, true);

			}
		} catch (Exception e) {

		}
	}

}
