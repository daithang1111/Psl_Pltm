package edu.umd.umiacs.clip.tn.data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class generatePSLData {

	public static void main(String args[]) {
		if (args.length == 12) {// add 2 more into arguments
			String fully_aligned_en = args[0];
			String fully_aligned_de = args[1];
			String doc_doc_en = args[2];
			String doc_doc_de = args[3];
			String doc_user_en = args[4];
			String doc_user_de = args[5];
			String doc_bot_en = args[6];
			String doc_bot_de = args[7];
			String doc_link_en = args[8];
			String doc_link_de = args[9];
			String aligned_en_dvs = args[10];
			String aligned_de_dvs = args[11];
			gen(fully_aligned_en, fully_aligned_de, doc_doc_en, doc_doc_de,
					doc_user_en, doc_user_de, doc_bot_en, doc_bot_de,
					doc_link_en, doc_link_de, aligned_en_dvs, aligned_de_dvs);
		} else {
			System.out
					.println("We need 12 arguments fully_aligned_en, fully_aligned_de, doc_doc_en, doc_doc_de,"
							+ " doc_user_en, doc_user_de, doc_bot_en, doc_bot_de, doc_link_en, doc_link_de,aligned_en_dvs, aligned_de_dvs");
			System.exit(0);
		}
	}

	/**
	 * 
	 * @param fully_aligned_en
	 * @param fully_aligned_de
	 * @param doc_cat_en
	 * @param doc_cat_de
	 * @param doc_user_en
	 * @param doc_user_de
	 * @param aligned_en_dvs
	 * @param aligned_de_dvs
	 */
	public static void gen(String fully_aligned_en, String fully_aligned_de,
			String doc_doc_en, String doc_doc_de, String doc_user_en,
			String doc_user_de, String doc_bot_en, String doc_bot_de,
			String doc_link_en, String doc_link_de, String aligned_en_dvs,
			String aligned_de_dvs) {

		// generate name, hasCategory, hasEditor file for PSL

		try {
			// index for name
			int nameIndex = 0;
			int maxIndex_en = 0;
			int maxIndex_de = 0;
			HashMap<String, Integer> docNameHash = new HashMap<String, Integer>();// for
																					// article
																					// only
			HashMap<String, Integer> nameHash = new HashMap<String, Integer>();// for
																				// all
																				// names
			// create name
			// read en articles
			FileInputStream fstream = new FileInputStream(fully_aligned_en);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] title_label_content = strLine.split("\\t");// size
																	// should be
																	// 3
				if (title_label_content.length == 3) {
					Preprocess.fileWriter(nameIndex + "\t"
							+ title_label_content[0] + "\n", "name", true);
					nameHash.put(title_label_content[0], nameIndex);
					docNameHash.put(title_label_content[0], nameIndex);
					nameIndex++;
				}
			}

			maxIndex_en = nameIndex - 1;

			fstream.close();
			in.close();

			// read de articles
			fstream = new FileInputStream(fully_aligned_de);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] title_label_content = strLine.split("\\t");// size
				// should be
				// 3
				if (title_label_content.length == 3) {
					Preprocess.fileWriter(nameIndex + "\t"
							+ title_label_content[0] + "\n", "name", true);
					nameHash.put(title_label_content[0], nameIndex);
					docNameHash.put(title_label_content[0], nameIndex);
					nameIndex++;
				}
			}
			maxIndex_de = nameIndex - 1;
			fstream.close();
			in.close();

			// // doc_cat_en
			// fstream = new FileInputStream(doc_cat_en);
			//
			// in = new DataInputStream(fstream);
			// br = new BufferedReader(new InputStreamReader(in));
			//
			// HashMap<String, Boolean> catHash = new HashMap<String,
			// Boolean>();
			// while ((strLine = br.readLine()) != null) {
			// String[] doc_cat = strLine.split("\\t");
			// if (doc_cat != null && doc_cat.length == 2) {
			// if (docHash.containsKey(doc_cat[0])) {
			// catHash.put(doc_cat[1], true);
			// }
			// }
			//
			// }
			//
			// fstream.close();
			// in.close();
			// // end
			//
			// // doc_cat_de
			// fstream = new FileInputStream(doc_cat_de);
			//
			// in = new DataInputStream(fstream);
			// br = new BufferedReader(new InputStreamReader(in));
			//
			// while ((strLine = br.readLine()) != null) {
			// String[] doc_cat = strLine.split("\\t");
			// if (doc_cat != null && doc_cat.length == 2) {
			// if (docHash.containsKey(doc_cat[0])) {
			// catHash.put(doc_cat[1], true);
			// }
			// }
			// }
			// fstream.close();
			// in.close();
			// end

			// PRINT category names
			// Set<String> catSet = catHash.keySet();
			//
			// for (String c : catSet) {
			// Util.fileWriter(nameIndex + "\t" + c + "\n", "name");
			// nameHash.put(c, nameIndex);
			// nameIndex++;
			// }
			// end

			// doc_user_en
			fstream = new FileInputStream(doc_user_en);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			HashMap<String, Boolean> userHash = new HashMap<String, Boolean>();
			while ((strLine = br.readLine()) != null) {
				String[] doc_user = strLine.split("\\t");
				if (doc_user != null && doc_user.length == 2
						&& doc_user[1].trim().length() > 0) {
					if (docNameHash.containsKey(doc_user[0])) {
						userHash.put(doc_user[1], true);
					}
				}
			}

			fstream.close();
			in.close();

			// doc_user_de
			fstream = new FileInputStream(doc_user_de);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] doc_user = strLine.split("\\t");
				if (doc_user != null && doc_user.length == 2
						&& doc_user[1].trim().length() > 0) {
					if (docNameHash.containsKey(doc_user[0])) {
						userHash.put(doc_user[1], true);
					}
				}
			}

			fstream.close();
			in.close();

			// end

			// PRINT user names
			Set<String> userSet = userHash.keySet();

			for (String c : userSet) {
				Preprocess
						.fileWriter(nameIndex + "\t" + c + "\n", "name", true);
				nameHash.put(c, nameIndex);
				nameIndex++;
			}
			// end

			// now compose index index mappings

			// // doc_cat_en
			// fstream = new FileInputStream(doc_cat_en);
			//
			// in = new DataInputStream(fstream);
			// br = new BufferedReader(new InputStreamReader(in));
			//
			// while ((strLine = br.readLine()) != null) {
			// String[] doc_cat = strLine.split("\\t");
			// if (doc_cat != null && doc_cat.length == 2) {
			// if (docHash.containsKey(doc_cat[0])) {
			// Util.fileWriter(
			// docHash.get(doc_cat[0]) + "\t"
			// + nameHash.get(doc_cat[1]) + "\n",
			// "hasCategory");
			// }
			// }
			// }
			//
			// // end
			// fstream.close();
			// in.close();
			// // doc_cat_de
			// fstream = new FileInputStream(doc_cat_de);
			//
			// in = new DataInputStream(fstream);
			// br = new BufferedReader(new InputStreamReader(in));
			//
			// while ((strLine = br.readLine()) != null) {
			// String[] doc_cat = strLine.split("\\t");
			// if (doc_cat != null && doc_cat.length == 2) {
			// if (docHash.containsKey(doc_cat[0])) {
			// Util.fileWriter(
			// docHash.get(doc_cat[0]) + "\t"
			// + nameHash.get(doc_cat[1]) + "\n",
			// "hasCategory");
			// }
			// }
			// }
			// fstream.close();
			// in.close();
			// end
			// doc_user_en
			fstream = new FileInputStream(doc_user_en);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] doc_user = strLine.split("\\t");
				if (doc_user != null && doc_user.length == 2
						&& doc_user[1].trim().length() > 0) {
					if (docNameHash.containsKey(doc_user[0])) {
						Preprocess.fileWriter(docNameHash.get(doc_user[0])
								+ "\t" + nameHash.get(doc_user[1]) + "\n",
								"hasEditor", true);
					}
				}
			}
			fstream.close();
			in.close();
			// end
			// doc_user_de
			fstream = new FileInputStream(doc_user_de);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] doc_user = strLine.split("\\t");
				if (doc_user != null && doc_user.length == 2
						&& doc_user[1].trim().length() > 0) {
					if (docNameHash.containsKey(doc_user[0])) {
						Preprocess.fileWriter(docNameHash.get(doc_user[0])
								+ "\t" + nameHash.get(doc_user[1]) + "\n",
								"hasEditor", true);
					}
				}
			}
			fstream.close();
			in.close();
			/*******************************/
			// GENERATE VETOR
			// train_en_dvs
			fstream = new FileInputStream(aligned_en_dvs);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			ArrayList<String> list = new ArrayList<String>();
			while ((strLine = br.readLine()) != null) {
				list.add(strLine);
			}

			fstream.close();
			in.close();
			// end
			// train_de_dvs
			fstream = new FileInputStream(aligned_de_dvs);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				list.add(strLine);
			}

			fstream.close();
			in.close();
			// end

			HashMap<String, String> doc_vector = new HashMap<String, String>();
			for (int i = 0; i < list.size() / 2; i++) {
				String docTitle = list.get(2 * i);
				String vector = list.get(2 * i + 1);

				if (!docTitle.contains("EMPTY_")
						&& docNameHash.containsKey(docTitle)) {
					doc_vector.put(docTitle, vector);
				}
			}

			//

			Set<String> doc_set = doc_vector.keySet();
			for (String s : doc_set) {
				if (!docNameHash.containsKey(s)) {
					System.out.println(s);
				}
				StringBuffer newVector = new StringBuffer();
				String vec[] = doc_vector.get(s).split(":");
				for (int i = 0; i < vec.length; i++) {
					if (vec[i].contains("E")) {
						// newVector.append(i + ":0.0|");// newVector.append(i +
						// ":0.0|");//ThangNguyen 020413
					} else {

						double tmpVal = Double.parseDouble(vec[i]);
						if (tmpVal > 0.01) {
							newVector.append(i
									+ ":"
									+ vec[i].substring(0,
											Math.min(4, vec[i].length() - 1))
									+ "|");// psl
											// can't
											// handle
											// long
											// string
						}
					}
				}
				Preprocess.fileWriter(nameIndex + "\t" + newVector.toString()
						+ "\n", "name", true);
				Preprocess.fileWriter(nameIndex + "\t" + newVector.toString()
						+ "\n", "vectorNames", true);// this is used merely for
													// generating vector
													// blocking
				Preprocess.fileWriter(docNameHash.get(s) + "\t" + nameIndex
						+ "\n", "hasTopicVector", true);

				nameIndex++;
			}

			// create doc -bot information
			HashMap<String, Boolean> botHash = new HashMap<String, Boolean>();
			ArrayList<String> doc_bot = new ArrayList<String>();
			fstream = new FileInputStream(doc_bot_en);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] db = strLine.split("\\t");
				if (db != null && db.length == 2) {
					if (docNameHash.containsKey(db[0])) {
						doc_bot.add(strLine);
						botHash.put(strLine.split("\\t")[1], true);
					}

				}

			}

			fstream.close();
			in.close();

			// start
			fstream = new FileInputStream(doc_bot_de);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] db = strLine.split("\\t");
				if (db != null && db.length == 2) {
					if (docNameHash.containsKey(db[0])) {
						doc_bot.add(strLine);
						botHash.put(strLine.split("\\t")[1], true);
					}

				}
			}

			fstream.close();
			in.close();

			// end
			Set<String> botSet = botHash.keySet();
			for (String b : botSet) {
				Preprocess.fileWriter(nameIndex + "\t" + b + "_BOT" + "\n",
						"name", true);
				nameHash.put(b + "_BOT", nameIndex);
				nameIndex++;
			}

			// print hasBot
			for (int i = 0; i < doc_bot.size(); i++) {
				String[] db = doc_bot.get(i).split("\\t");
				if (db != null && db.length == 2) {
					Preprocess.fileWriter(docNameHash.get(db[0]) + "\t"
							+ nameHash.get(db[1] + "_BOT") + "\n", "hasBot",
							true);
				}
			}

			// //////////////Doc -link
			HashMap<String, Boolean> linkHash = new HashMap<String, Boolean>();
			ArrayList<String> doc_link = new ArrayList<String>();
			fstream = new FileInputStream(doc_link_en);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] db = strLine.split("\\t");
				if (db != null && db.length == 2) {
					if (docNameHash.containsKey(db[0])) {
						doc_link.add(strLine);
						linkHash.put(strLine.split("\\t")[1], true);
					}

				}

			}

			fstream.close();
			in.close();

			// start
			fstream = new FileInputStream(doc_link_de);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] db = strLine.split("\\t");
				if (db != null && db.length == 2) {
					if (docNameHash.containsKey(db[0])) {
						doc_link.add(strLine);
						linkHash.put(strLine.split("\\t")[1], true);
					}

				}
			}

			fstream.close();
			in.close();

			// end
			Set<String> linkSet = linkHash.keySet();
			HashMap<String, String> pseudoLinks = new HashMap<String, String>();
			for (String l : linkSet) {
				Preprocess.fileWriter(nameIndex + "\t" + nameIndex + "_LINK"
						+ "\n", "name", true);
				nameHash.put(nameIndex + "_LINK", nameIndex);
				pseudoLinks.put(l, nameIndex + "_LINK");
				Preprocess.fileWriter(l + "\t" + nameIndex + "_LINK\n",
						"pseudoLink", true);
				nameIndex++;
			}

			// print hasLink
			for (int i = 0; i < doc_link.size(); i++) {
				String[] db = doc_link.get(i).split("\\t");
				if (db != null && db.length == 2) {
					Preprocess.fileWriter(docNameHash.get(db[0]) + "\t"
							+ nameHash.get(pseudoLinks.get(db[1])) + "\n",
							"hasLink", true);
				}
			}

			// //////end
			// nameIndex is the namesize+1
			// generate hasLanguage
			Preprocess.fileWriter(nameIndex + "\tenglish\n", "name", true);
			nameIndex++; // name size +2 for 2 more names (en,de
			Preprocess.fileWriter(nameIndex + "\tgerman\n", "name", true);

			int tmpIndex = nameIndex - 1;
			for (int i = 0; i <= maxIndex_en; i++) {
				Preprocess.fileWriter(i + "\t" + tmpIndex + "\n",
						"hasLanguage", true);
			}
			for (int i = maxIndex_en + 1; i <= maxIndex_de; i++) {
				Preprocess.fileWriter(i + "\t" + nameIndex + "\n",
						"hasLanguage", true);
			}

			// doc doc en
			fstream = new FileInputStream(doc_doc_en);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] db = strLine.split("\\t");
				if (db != null && db.length == 2) {
					if (docNameHash.containsKey(db[0])
							&& docNameHash.containsKey(db[1])) {
						Preprocess.fileWriter(docNameHash.get(db[0]) + "\t"
								+ docNameHash.get(db[1]) + "\n", "hasWikiLink",
								true);
					}

				}

			}

			fstream.close();
			in.close();
			// doc doc de
			fstream = new FileInputStream(doc_doc_de);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] db = strLine.split("\\t");
				if (db != null && db.length == 2) {
					if (docNameHash.containsKey(db[0])
							&& docNameHash.containsKey(db[1])) {
						Preprocess.fileWriter(docNameHash.get(db[0]) + "\t"
								+ docNameHash.get(db[1]) + "\n", "hasWikiLink",
								true);
					}

				}

			}

			fstream.close();
			in.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
