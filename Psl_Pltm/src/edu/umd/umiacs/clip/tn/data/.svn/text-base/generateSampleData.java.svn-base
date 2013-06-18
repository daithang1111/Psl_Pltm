package edu.umd.umiacs.clip.tn.data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class generateSampleData {
	public static void main(String args[]) {
		if (args.length == 5) {
			String doc_en = args[0];
			String doc_de = args[1];
			int SAMPLE_SIZE = Integer.parseInt(args[2]);
			String sample_en = args[3];
			String sample_de = args[4];
			gen(doc_en, doc_de, SAMPLE_SIZE, sample_en, sample_de);
		} else {
			System.out
					.println("We need 5 arguments\ndoc_en, doc_de, SAMPLE_SIZE,sample_en,sample_de");
			System.exit(0);
		}
	}

	/**
	 * 
	 * @param doc_en
	 * @param doc_de
	 * @param SAMPLE_SIZE
	 * @param sample_en
	 * @param sample_de
	 */
	public static void gen(String english_content_sorted,
			String german_content_sorted, int SAMPLE_SIZE, String sample_en,
			String sample_de) {

		int size = Preprocess.CORPUS_SIZE;

		// select sample indexes, increasing order
		HashMap<Integer, Boolean> selectedIndexes = new HashMap<Integer, Boolean>();
		int picked;
		while (selectedIndexes.size() < SAMPLE_SIZE) {
			picked = (int) ((double) (size) * Math.random());
			if (!selectedIndexes.containsKey(picked)) {
				selectedIndexes.put(picked, true);
			}
		}

		// create sample data
		try {

			// for english
			FileInputStream fstream = new FileInputStream(
					english_content_sorted);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int mark = 0;
			while ((strLine = br.readLine()) != null) {
				if (selectedIndexes.containsKey(mark)) {
					Preprocess.fileWriter(strLine + "\n", sample_en, true);
				}
				mark++;
			}
			fstream.close();
			in.close();
			// for german
			fstream = new FileInputStream(german_content_sorted);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			mark = 0;
			while ((strLine = br.readLine()) != null) {
				if (selectedIndexes.containsKey(mark)) {
					Preprocess.fileWriter(strLine + "\n", sample_de, true);
				}
				mark++;
			}
			fstream.close();
			in.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	/**
	 * 
	 * @param doc_en
	 * @param doc_de
	 * @param SAMPLE_SIZE
	 * @param sample_en
	 * @param sample_de
	 */
	// public static void gen(String doc_en, String doc_de, int SAMPLE_SIZE,
	// String sample_en, String sample_de) {
	//
	// // read doc_en and doc_de content
	// // these contents are english and german articles extracted from wiki
	// ArrayList<String> doc_content_en = Preprocess.readFile(doc_en);
	// ArrayList<String> doc_content_de = Preprocess.readFile(doc_de);
	//
	// // calculate size of documents
	// int size_en = doc_content_en.size();
	// int size_de = doc_content_de.size();
	// int size = Math.min(size_en, size_de);// pick the smallest size
	//
	// TreeMap<Integer, Boolean> sortedIndexes = new TreeMap<Integer,
	// Boolean>();
	//
	// while (sortedIndexes.size() < SAMPLE_SIZE) {
	// int picked = (int) ((double) (size) * Math.random());
	// if (!sortedIndexes.containsKey(picked)) {
	// String[] record_en = doc_content_en.get(picked).split("\\t");
	// String[] record_de = doc_content_de.get(picked).split("\\t");
	// if (record_en != null && record_de != null
	// && record_en.length == 3 && record_de.length == 3) {
	// sortedIndexes.put(picked, true);
	// }
	// }
	// }
	//
	// // add value to pickedIndexes
	// Set<Integer> keys = sortedIndexes.keySet();
	// for (int i : keys) {
	// Preprocess.fileWriter(doc_content_en.get(i) + "\n", sample_en, true);
	// Preprocess.fileWriter(doc_content_de.get(i) + "\n", sample_de, true);
	// }
	// }
}
