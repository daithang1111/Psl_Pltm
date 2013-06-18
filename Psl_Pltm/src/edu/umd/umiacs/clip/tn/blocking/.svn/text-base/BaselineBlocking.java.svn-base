package edu.umd.umiacs.clip.tn.blocking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class BaselineBlocking {
	public static void main(String args[]) {
		if (args.length != 4) {
			System.out
					.println("We need four params: fileName, docSize, blockingSize, output");
			System.exit(0);
		}
		String fileName = args[0]; // this is the output from
									// BaselineSixWords_Blocking.java
		int docSize = Integer.parseInt(args[1]);
		int blockingSize = Integer.parseInt(args[2]);
		String outputFile = args[3];
		System.out.println("Generate blocking for :fileName=" + fileName
				+ ", docSize=" + docSize + ", blockingSize=" + blockingSize
				+ ", outputFile=" + outputFile);

		try {
			FileInputStream fstream = new FileInputStream(fileName);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			int enID;
			int deID;
			// int counter =0;
			HashMap<Integer, Integer> countHash = new HashMap<Integer, Integer>();
			while ((strLine = br.readLine()) != null) {
				String[] w_doc1_doc2 = strLine.split("\\t");
				enID = Integer.parseInt(w_doc1_doc2[1]);
				deID = Integer.parseInt(w_doc1_doc2[2]) + docSize;

				if (countHash.containsKey(enID)) {
					if (countHash.get(enID) < blockingSize) {
						countHash.put(enID, countHash.get(enID) + 1);
						Preprocess.fileWriter(enID + "\t" + deID + "\n", outputFile,
								true);
					}
				} else {
					countHash.put(enID, 1);
					Preprocess.fileWriter(enID + "\t" + deID + "\n", outputFile, true);
				}

			}
			fstream.close();
			in.close();
		} catch (Exception e) {

		}
	}
}
