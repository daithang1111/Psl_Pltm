package edu.umd.umiacs.clip.tn.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class generateTrainDataFromFile {

	public static void main(String args[]) {
		if (args.length == 5) {
			String aligned_en = args[0];
			String aligned_de = args[1];
			String aligned_pair = args[2];

			String new_aligned_en = args[3];
			String new_aligned_de = args[4];
			gen(aligned_en, aligned_de, aligned_pair, new_aligned_en,
					new_aligned_de);
		} else {
			System.out
					.println("We need 5 arguments\nenglish_doc, german_doc, input_file, train_en, train_de");
			System.exit(0);
		}
	}

	/**
	 * 
	 * @param aligned_en
	 * @param aligned_de
	 * @param aligned_pair
	 * @param new_aligned_en
	 * @param new_aligned_de
	 */
	public static void gen(String aligned_en, String aligned_de,
			String aligned_pair, String new_aligned_en, String new_aligned_de) {

		// read doc_en and doc_de content
		ArrayList<String> doc_content_en = Preprocess.readFile(aligned_en);
		ArrayList<String> doc_content_de = Preprocess.readFile(aligned_de);
		File f = new File(aligned_pair);
		if (!f.exists()) {
			System.out.println("No aligned_pair, no more progress");
			System.exit(0);
		}
		ArrayList<String> titlePairs = Preprocess.readFile(aligned_pair);
		// calculate size of documents
		int size_en = doc_content_en.size();
		int size_de = doc_content_de.size();
		int size = Math.min(size_en, size_de);// pick the smallest size
		System.out.println("doc size = " + size);
		System.out.println("title pair size = " + titlePairs.size());

		// all docID
		HashMap<String, String> enIdDoc = new HashMap<String, String>();
		HashMap<String, String> deIdDoc = new HashMap<String, String>();
		HashMap<String, Boolean> enHasPair = new HashMap<String, Boolean>();// if
																			// this
																			// english
																			// article
																			// has
																			// been
																			// paired?
		HashMap<String, Boolean> deHasPair = new HashMap<String, Boolean>();

		HashMap<String, Boolean> newIdPairs = new HashMap<String, Boolean>();

		for (int i = 0; i < size; i++) {
			String idEn = "", idDe = "";

			String tmp[] = doc_content_en.get(i).split("\\t");
			if (tmp.length == 3) {
				idEn = tmp[0];
				if (!idEn.contains("EMPTY_EN")) {
					enIdDoc.put(idEn, doc_content_en.get(i));
				}

			}
			tmp = doc_content_de.get(i).split("\\t");
			if (tmp.length == 3) {
				idDe = tmp[0];
				if (!idDe.contains("EMPTY_DE")) {
					deIdDoc.put(idDe, doc_content_de.get(i));
				}
			}

			if (!idEn.contains("EMPTY_EN") && !idDe.contains("EMPTY_DE")
					&& idEn.length() > 0 && idDe.length() > 0) {
				newIdPairs.put(idEn + "\t" + idDe, true);
				enHasPair.put(idEn, true);
				deHasPair.put(idDe, true);
			}
		}

		// add new pairs if possible
		for (int i = 0; i < titlePairs.size(); i++) {
			String tmp[] = titlePairs.get(i).split("\\t");
			if (!enHasPair.containsKey(tmp[0])
					&& !deHasPair.containsKey(tmp[1])) {
				newIdPairs.put(titlePairs.get(i), true);
				enHasPair.put(tmp[0], true);
				deHasPair.put(tmp[1], true);
			}
		}

		// create new training aligned docs
		// 1. print aligned pairs
		Set<String> alignedPairs = newIdPairs.keySet();
		for (String s : alignedPairs) {
			// System.out.println(s);
			String ids[] = s.split("\\t");

			Preprocess.fileWriter(enIdDoc.get(ids[0]) + "\n", new_aligned_en,
					true);
			Preprocess.fileWriter(deIdDoc.get(ids[1]) + "\n", new_aligned_de,
					true);

		}

		// 2. print english unaligened
		Set<String> allEn = enIdDoc.keySet();
		int count = 0;
		for (String s : allEn) {
			if (!enHasPair.containsKey(s)) {
				Preprocess.fileWriter(enIdDoc.get(s) + "\n", new_aligned_en,
						true);
				Preprocess.fileWriter("EMPTY_DE_" + count++ + "\tX\t\n",
						new_aligned_de, true);
			}
		}
		// 3. print german unaligned
		Set<String> allDe = deIdDoc.keySet();
		count = 0;
		for (String s : allDe) {
			if (!deHasPair.containsKey(s)) {
				Preprocess.fileWriter(deIdDoc.get(s) + "\n", new_aligned_de,
						true);
				Preprocess.fileWriter("EMPTY_EN_" + count++ + "\tX\t\n",
						new_aligned_en, true);
			}
		}

	}
}
