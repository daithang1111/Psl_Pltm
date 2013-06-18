package edu.umd.umiacs.clip.tn.data;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class generateTrainData {

	public static void main(String args[]) {
		if (args.length == 8) {
			String fully_aligned_en = args[0];
			String fully_aligned_de = args[1];
			int start = Integer.parseInt(args[2]);
			int end = Integer.parseInt(args[3]);
			String aligned_en = args[4];
			String aligned_de = args[5];
			int train_size = Integer.parseInt(args[6]);
			boolean random = (args[7].equalsIgnoreCase("true")) ? true : false;

			gen(fully_aligned_en, fully_aligned_de, start, end, aligned_en,
					aligned_de, train_size, random);
		} else {
			System.out
					.println("We need 8 arguments\nenglish_doc, german_doc, start, end, train_en, train_de, train_size, random (true,false)");
			System.exit(0);
		}
	}

	/**
	 * if random =true, use train_size (must be >0) if random =false, use start
	 * and end
	 * 
	 * @param fully_aligned_en
	 * @param fully_aligned_de
	 * @param start
	 * @param end
	 * @param aligned_en
	 * @param aligned_de
	 * @param train_size
	 * @param random
	 */
	public static void gen(String fully_aligned_en, String fully_aligned_de,
			int start_in, int end_in, String aligned_en, String aligned_de,
			int train_size_in, boolean random) {

		// read doc_en and doc_de content
		ArrayList<String> doc_content_en = Preprocess.readFile(fully_aligned_en);
		ArrayList<String> doc_content_de = Preprocess.readFile(fully_aligned_de);

		// calculate size of documents
		int size_en = doc_content_en.size();
		int size_de = doc_content_de.size();
		int size = Math.min(size_en, size_de);// pick the smallest size

		// calculate variables
		int train_size, start, end;
		ArrayList<Integer> pickedIndexes = new ArrayList<Integer>();

		if (random) {// use train_size
			TreeMap<Integer, Boolean> sortedIndexes = new TreeMap<Integer, Boolean>();
			if (train_size_in <= 0) {
				System.out.println("train_size must be >0 if random is true");
				return;
			} else {
				train_size = Math.min(train_size_in, size);
				while (sortedIndexes.size() < train_size) {
					int picked = (int) ((double) (size) * Math.random());
					if (!sortedIndexes.containsKey(picked)) {
						sortedIndexes.put(picked, true);
					}
				}

				// add value to pickedIndexes
				Set<Integer> keys = sortedIndexes.keySet();
				for (int i : keys) {
					pickedIndexes.add(i);
				}
			}
		} else {// use start and end
			start = Math.max(0, start_in);
			end = Math.min(size - 1, end_in);
			if (start >= size || end < 0 || start > end) {
				System.out
						.println("Invalid index start, end. Must be 0<=start<end<size");
			} else {
				for (int i = start; i < end; i++) {
					pickedIndexes.add(i);
				}
			}
		}

		// pickedIndexes has increasing indexes
		int pickedCount = 0;

		// generate aligned docs
		for (int i = 0; i < size; i++) {
			// i is the chosen index in sample

			if (pickedCount == pickedIndexes.size()) {
				Preprocess.fileWriter(doc_content_en.get(i) + "\n", aligned_en, true);
				Preprocess.fileWriter("EMPTY_DE_" + i + "\tX\t\n", aligned_de, true);
				Preprocess.fileWriter("EMPTY_EN_" + i + "\tX\t\n", aligned_en, true);
				Preprocess.fileWriter(doc_content_de.get(i) + "\n", aligned_de, true);
			} else {
				if (pickedIndexes.get(pickedCount) == i) {
					Preprocess.fileWriter(doc_content_en.get(i) + "\n", aligned_en,
							true);
					Preprocess.fileWriter(doc_content_de.get(i) + "\n", aligned_de,
							true);
					// print glue documents title
					String title_en = doc_content_en.get(i).split("\\t")[0];
					String title_de = doc_content_de.get(i).split("\\t")[0];
					Preprocess.fileWriter(title_en + "\t" + title_de + "\n",
							"glueDocuments", true); // ThangNguyen 103012

					pickedCount++;
				} else {
					Preprocess.fileWriter(doc_content_en.get(i) + "\n", aligned_en,
							true);
					Preprocess.fileWriter("EMPTY_DE_" + i + "\tX\t\n", aligned_de,
							true);
					Preprocess.fileWriter("EMPTY_EN_" + i + "\tX\t\n", aligned_en,
							true);
					Preprocess.fileWriter(doc_content_de.get(i) + "\n", aligned_de,
							true);
				}
			}
		}
	}
}
