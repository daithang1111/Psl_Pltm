package edu.umd.umiacs.clip.tn.blocking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

import net.sf.javaml.core.kdtree.KDTree;
import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class KDTreeBlocking {
	public static void main(String[] args) {
		if (args.length == 6) {
			int docSize = Integer.parseInt(args[0]);
			int vectorSize = Integer.parseInt(args[1]);
			int blockingSize = Integer.parseInt(args[2]);
			String hasTopicVector = args[3];
			String vectorNames = args[4];
			String outputFile = args[5];

			generateKdTreeTopicVector(docSize, vectorSize, blockingSize,
					hasTopicVector, vectorNames, outputFile);
		} else {
			System.out
					.println("We need 6 params: docSize, vectorSize, blockingSize, hasTopicVector, vectorNames, outputFile");
			System.exit(0);
		}
	}

	/**
	 * 
	 */
	public static void generateKdTreeTopicVector(int docSize, int vectorSize,
			int blockingSize, String hasTopicVector, String vectorNames,
			String outputFile) {

		TreeMap<String, String> tree = new TreeMap<String, String>();
		try {
			FileInputStream fstream = new FileInputStream(hasTopicVector);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] doc_vec = strLine.split("\\t");
				tree.put(doc_vec[1], doc_vec[0]);
			}

			fstream.close();
			in.close();
		} catch (Exception e) {

		}
		// /
		KDTree enKd = new KDTree(vectorSize);
		KDTree deKd = new KDTree(vectorSize);
		double[][] enKeys = new double[docSize][vectorSize];
		double[][] deKeys = new double[docSize][vectorSize];
		String[] enDocIds = new String[docSize];
		String[] deDocIds = new String[docSize];
		try {
			FileInputStream fstream = new FileInputStream(vectorNames);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String vecs[] = strLine.split("\\t");
				String kvectors[] = vecs[1].split("\\|");

				int tmpDocId = Integer.parseInt(tree.get(vecs[0]));
				// this is english
				if (tmpDocId < docSize) {
					enDocIds[tmpDocId] = tree.get(vecs[0]);
					enKeys[tmpDocId] = new double[vectorSize];
					for (int i = 0; i < vectorSize; i++) {
						enKeys[tmpDocId][i] = 0.0;
					}

					for (int i = 0; i < kvectors.length; i++) {
						int tmpIndex = Integer
								.parseInt(kvectors[i].split(":")[0]);
						enKeys[tmpDocId][tmpIndex] = Double
								.parseDouble(kvectors[i].split(":")[1]);
					}

					enKd.insert(enKeys[tmpDocId], enDocIds[tmpDocId]);

				} else {// this is german
					int index = tmpDocId - docSize;
					deDocIds[index] = tree.get(vecs[0]);
					deKeys[index] = new double[vectorSize];

					for (int i = 0; i < vectorSize; i++) {
						deKeys[index][i] = 0.0;
					}
					for (int i = 0; i < kvectors.length; i++) {

						int tmpIndex = Integer
								.parseInt(kvectors[i].split(":")[0]);
						deKeys[index][tmpIndex] = Double
								.parseDouble(kvectors[i].split(":")[1]);

					}

					deKd.insert(deKeys[index], deDocIds[index]);
				}

			}

			fstream.close();
			in.close();

			// print blocking for each english article
			for (int i = 0; i < docSize; i++) {
				System.out.println("printing blocking for " + enDocIds[i]);

				Object[] nbrs = deKd.nearest(enKeys[i], blockingSize);
				for (int j = 0; j < nbrs.length; j++) {
					Preprocess.fileWriter(
							enDocIds[i] + "\t" + nbrs[j].toString() + "\n",
							outputFile, true);
				}

			}

			// may not needed
			// print blocking for each german articles
			// for (int i = 0; i < docSize; i++) {
			// // print out nearest for english
			// double[] tmp = deKeys[i];
			// System.out.println("printing blocking for " + deDocIds[i]);
			//
			// Object[] nbrs = enKd.nearest(tmp, num);
			// for (int j = 0; j < nbrs.length; j++) {
			// fileWriter(deDocIds[i] + "\t" + nbrs[j].toString() + "\n",
			// blockingFile, true);
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
