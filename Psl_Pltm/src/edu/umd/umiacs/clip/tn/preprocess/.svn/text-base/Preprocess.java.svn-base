package edu.umd.umiacs.clip.tn.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocess {

	public static final String regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	public static final String regexWikiLink = "\\[\\[([[-a-zA-Z0-9+&@#/%?=~_|!:,.;]* :|]+)\\]\\]";
	public static int CORPUS_SIZE = 100;
	public static String DATA_DIR = "data";
	public static final String EN_DE_TOPICS = "en_de_topics";
	public static int DOC_LENGTH_THRESHOLD = 100;
	public static int MAX_PER_PAGE = 10;// only crawl 10 wiki links each
										// page
	public static final String botNames = "botNames.txt";
	static {
		System.out.println(new java.util.Date().toString()
				+ " ; Loading Psl_Pltm Properties.");
		// Load the properties file
		try {
			FileInputStream propertiesStream = new FileInputStream(
					"Psl_PltmPreferences.properties");
			Properties appProps = new Properties();
			appProps.load(propertiesStream);
			propertiesStream.close();
			// read data directory prop
			String data_dir = appProps.getProperty("DATA_DIR");
			if (data_dir != null) {
				DATA_DIR = data_dir;
			}

			// corpus size
			String corpus_size = appProps.getProperty("CORPUS_SIZE");
			if (corpus_size != null) {
				CORPUS_SIZE = Integer.parseInt(corpus_size);
			}

			// max doc length allowed
			String doc_length_threshold = appProps
					.getProperty("DOC_LENGTH_THRESHOLD");
			if (doc_length_threshold != null) {
				DOC_LENGTH_THRESHOLD = Integer.parseInt(doc_length_threshold);
			}

			// max wiki links allowed to crawl
			String max_per_page = appProps.getProperty("MAX_PER_PAGE");
			if (max_per_page != null) {
				MAX_PER_PAGE = Integer.parseInt(max_per_page);
			}

		} catch (Exception e) {
			System.err.println("Error loading properties file: "
					+ e.getMessage());
		}
	}// static

	// ////////////////////
	/**
	 * 
	 * @param content_sorted
	 * @param doc_link
	 */
	public static void generateHyperLinks(String content_sorted, String doc_link) {

		try {

			FileInputStream fstream = new FileInputStream(DATA_DIR + "/"
					+ content_sorted);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] oneArticle = strLine.split("\\t");
				if (oneArticle.length == 3) {
					try {
						ArrayList<String> hyperLinks = getHyperLinks(oneArticle[2]);
						for (int j = 0; j < hyperLinks.size(); j++) {
							fileWriterWithDataDir(oneArticle[0] + "\t"
									+ hyperLinks.get(j) + "\n", doc_link, true);
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
			fstream.close();
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		/*
		 * ArrayList<String> english_content_list = readFile(DATA_DIR + "/" +
		 * english_content_sorted);
		 * System.out.println(english_content_list.size()); ArrayList<String>
		 * german_content_list = readFile(DATA_DIR + "/" +
		 * german_content_sorted); if (english_content_list.size() !=
		 * german_content_list.size()) {
		 * System.out.println("Length mismatch between " +
		 * english_content_sorted + " and " + german_content_list); return; }
		 * int size = english_content_list.size();
		 * 
		 * for (int i = 0; i < size; i++) { // en String[] en_content =
		 * english_content_list.get(i).split("\\t"); String[] de_content =
		 * german_content_list.get(i).split("\\t"); if (en_content.length == 3
		 * && de_content.length == 3) { ArrayList<String> hyperLinks =
		 * getHyperLinks(en_content[2]); for (int j = 0; j < hyperLinks.size();
		 * j++) { fileWriterWithDataDir( en_content[0] + "\t" +
		 * hyperLinks.get(j) + "\n", doc_link_en, true); } // de
		 * 
		 * hyperLinks = getHyperLinks(de_content[2]); for (int j = 0; j <
		 * hyperLinks.size(); j++) { fileWriterWithDataDir( de_content[0] + "\t"
		 * + hyperLinks.get(j) + "\n", doc_link_de, true); } } }
		 */
	}

	// /////////////////////////////////
	/**
	 * 
	 * @param english_content_sorted
	 * @param doc_doc_en
	 */
	public static void generateEnglishWikiLinks(String english_content_sorted,
			String doc_doc_en) {
		try {
			FileInputStream fstream = new FileInputStream(DATA_DIR + "/"
					+ english_content_sorted);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] title_label_content = strLine.split("\\t");
				if (title_label_content.length == 3) {
					try {
						HashMap<String, Boolean> linkedTopics = new HashMap<String, Boolean>();

						ArrayList<String> links = getWikiLinks(title_label_content[2]);// get
																						// link
																						// of
																						// content
						for (int i = 0; i < links.size(); i++) {
							String oneLink[] = links.get(i).trim()
									.replace("[", "").replace("]", "")
									.split("\\|");
							// System.out.println(links.get(i));
							// not so great, but works
							String realLink = oneLink[0];
							if (realLink.endsWith(" ")) {
								realLink = realLink.substring(0,
										realLink.length() - 1);
							}
							if (realLink.contains(":")) {
								if (realLink.startsWith("wikt:")) {
									String tmpRealLink[] = realLink.split(":");
									if (tmpRealLink.length > 1) {
										realLink = tmpRealLink[1];
									} else {
										continue;
									}

								} else {
									continue;
								}
							}
							realLink = realLink.replace(" ", "_").replace(
									"&amp;", "&");
							String firstLetter = realLink.substring(0, 1)
									.toUpperCase();
							realLink = firstLetter + realLink.substring(1);
							// newDeTopics.add(realLink);
							linkedTopics.put(realLink, true);

						}
						Set<String> linkedSet = linkedTopics.keySet();
						for (String linked : linkedSet) {
							fileWriterWithDataDir(title_label_content[0] + "\t"
									+ linked + "_en_content" + "\n",
									doc_doc_en, true);
						}
					} catch (Exception e) {
						continue;
					}

				}
			}

			fstream.close();
			in.close();

		} catch (Exception e) {
			System.out.println("ERROR generate english wikilinks");
		}
	}

	// ///////////////////////////////
	/**
	 * 
	 * @param german_content_sorted
	 * @param doc_doc_de
	 */
	public static void generateGermanWikiLinks(String german_content_sorted,
			String doc_doc_de) {
		try {

			// read mapping en-de topics
			FileInputStream fstream = new FileInputStream(DATA_DIR + "/"
					+ EN_DE_TOPICS);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			HashMap<String, String> de_en_map = new HashMap<String, String>();
			while ((strLine = br.readLine()) != null) {
				String[] tmp = strLine.split("\\t");
				if (tmp.length == 2) {
					de_en_map.put(tmp[1], tmp[0]);
				}
			}

			fstream.close();
			in.close();

			// create wikilinks for german
			fstream = new FileInputStream(DATA_DIR + "/"
					+ german_content_sorted);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				String[] title_label_content = strLine.split("\\t");
				if (title_label_content.length == 3) {
					try {
						HashMap<String, Boolean> linkedTopics = new HashMap<String, Boolean>();

						ArrayList<String> links = getWikiLinks(title_label_content[2]);// get
																						// link
																						// of
																						// content
						for (int i = 0; i < links.size(); i++) {
							String oneLink[] = links.get(i).trim()
									.replace("[", "").replace("]", "")
									.split("\\|");
							// System.out.println(links.get(i));
							// not so great, but works
							String realLink = oneLink[0];
							if (realLink.endsWith(" ")) {
								realLink = realLink.substring(0,
										realLink.length() - 1);
							}
							if (realLink.contains(":")) {
								if (realLink.startsWith("wikt:")) {
									String tmpRealLink[] = realLink.split(":");
									if (tmpRealLink.length > 1) {
										realLink = tmpRealLink[1];
									} else {
										continue;
									}

								} else {
									continue;
								}
							}
							realLink = realLink.replace(" ", "_").replace(
									"&amp;", "&");
							String firstLetter = realLink.substring(0, 1)
									.toUpperCase();
							realLink = firstLetter + realLink.substring(1);
							// newDeTopics.add(realLink);
							linkedTopics.put(realLink, true);

						}
						// because linkedSet contains german language titles, to
						// get
						// data format title
						// call map.get(title)
						Set<String> linkedSet = linkedTopics.keySet();
						for (String linked : linkedSet) {
							fileWriterWithDataDir(title_label_content[0] + "\t"
									+ de_en_map.get(linked) + "_de_content"
									+ "\n", doc_doc_de, true);
						}
					} catch (Exception e) {
						continue;
					}
				}
			}

			fstream.close();
			in.close();
		} catch (Exception e) {
			System.out.println("ERROR generate german wiki links");
		}
	}

	// ////////////////////////
	/**
	 * 
	 * @param doc_doc_en
	 * @param doc_doc_de
	 * @throws InterruptedException
	 */
	// public static void generateWikiLinks(String doc_doc_en, String
	// doc_doc_de)
	// throws InterruptedException {
	// ArrayList<String> en_de_topics = readFile(DATA_DIR + "/" + EN_DE_TOPICS);
	// HashMap<String, Boolean> en_topics = new HashMap<String, Boolean>();
	// HashMap<String, Boolean> de_topics = new HashMap<String, Boolean>();
	// HashMap<String, String> de_en_map = new HashMap<String, String>();
	// // create hash for both
	// for (int i = 0; i < en_de_topics.size(); i++) {
	// String[] en_de_topic = en_de_topics.get(i).split("\\t");
	// if (en_de_topic.length == 2) {
	// en_topics.put(en_de_topic[0], true);
	// de_topics.put(en_de_topic[1], true);
	// de_en_map.put(en_de_topic[1], en_de_topic[0]);
	// }
	// }
	//
	// EnglishGermanDocWikiCrawler pwc = new EnglishGermanDocWikiCrawler();
	// // for english
	// Set<String> en_topic_set = en_topics.keySet();
	// for (String en_topic : en_topic_set) {
	// ArrayList<String> linkedTopics = pwc.enWikiLinkCrawler(en_topic);
	// for (int i = 0; i < linkedTopics.size(); i++) {
	// if (en_topics.containsKey(linkedTopics.get(i))) {
	// fileWriterWithDataDir(en_topic + "_en_content" + "\t"
	// + linkedTopics.get(i) + "_en_content" + "\n",
	// doc_doc_en, true);
	// }
	// }
	// }
	// // for german
	// Set<String> de_topic_set = de_topics.keySet();
	// for (String de_topic : de_topic_set) {
	// ArrayList<String> linkedTopics = pwc.deWikiLinkCrawler(de_topic);
	// for (int i = 0; i < linkedTopics.size(); i++) {
	// if (de_topics.containsKey(linkedTopics.get(i))) {
	// fileWriterWithDataDir(
	// de_en_map.get(de_topic) + "_de_content" + "\t"
	// + de_en_map.get(linkedTopics.get(i))
	// + "_de_content" + "\n", doc_doc_de, true);
	//
	// }
	// }
	// }
	//
	// }

	// //////////////////////////////
	/**
	 * 
	 * @param doc_user_bot_en
	 * @param doc_user_bot_de
	 * @param doc_user_en
	 * @param doc_user_de
	 * @param doc_bot_en
	 * @param doc_bot_de
	 */
	public static void separateUserBot(String doc_user_bot_en,
			String doc_user_bot_de, String doc_user_en, String doc_user_de,
			String doc_bot_en, String doc_bot_de) {
		try {
			FileInputStream fstream = new FileInputStream(botNames);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			HashMap<String, Boolean> botusers = new HashMap<String, Boolean>();
			while ((strLine = br.readLine()) != null) {
				String[] bots = strLine.split("\\t");
				if (bots.length == 3) {
					botusers.put(
							bots[1].toLowerCase().trim()
									.replaceAll("[^\\x00-\\x7F]", ""), true);

				}
			}

			fstream.close();
			in.close();

			// read doc_user_en
			fstream = new FileInputStream(DATA_DIR + "/" + doc_user_bot_en);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] doc_user = strLine.split("\\t");
				if (doc_user != null && doc_user.length == 2) {
					if (!botusers.containsKey(doc_user[1].trim().toLowerCase())) {
						fileWriterWithDataDir(strLine + "\n", doc_user_en, true);
					} else {
						fileWriterWithDataDir(strLine + "\n", doc_bot_en, true);
					}
				}
			}

			fstream.close();
			in.close();

			// read doc_user_de
			fstream = new FileInputStream(DATA_DIR + "/" + doc_user_bot_de);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] doc_user = strLine.split("\\t");
				if (doc_user != null && doc_user.length == 2) {
					if (!botusers.containsKey(doc_user[1].trim().toLowerCase())) {
						fileWriterWithDataDir(strLine + "\n", doc_user_de, true);
					} else {
						fileWriterWithDataDir(strLine + "\n", doc_bot_de, true);
					}
				}
			}

			fstream.close();
			in.close();

		} catch (Exception e) {

		}
	}

	// /////////////////////////////////
	/**
	 * 
	 * @param english_history
	 * @param german_history
	 * @param user
	 * @param doc_en
	 * @param doc_de
	 * @param doc_user_bot_en
	 * @param doc_user_bot_de
	 */
	public static void generateMeta(String english_history,
			String german_history, String user, String doc_en, String doc_de,
			String doc_user_bot_en, String doc_user_bot_de) {

		// user
		TreeMap<String, Boolean> user_tree = new TreeMap<String, Boolean>();

		// english meta
		ArrayList<String> eh = readFile(DATA_DIR + "/" + english_history);
		HashMap<String, Boolean> printedUser_en = new HashMap<String, Boolean>();
		for (int i = 0; i < eh.size(); i++) {
			String[] tmp = eh.get(i).split("\\t");
			if (tmp != null && tmp.length > 1) {
				// print doc_en
				fileWriterWithDataDir(tmp[0] + "\n", doc_en, true);

				for (int j = 1; j < tmp.length; j = j + 2) {

					if (!printedUser_en.containsKey(tmp[j + 1])) {
						printedUser_en.put(tmp[j + 1], true);
						// add user [j+1]
						user_tree.put(tmp[j + 1], true);
						// print doc_user_bot_en
						fileWriterWithDataDir(
								tmp[0] + "\t" + tmp[j + 1] + "\n",
								doc_user_bot_en, true);
					}
				}
			}
		}

		// german meta
		ArrayList<String> dh = readFile(DATA_DIR + "/" + german_history);
		HashMap<String, Boolean> printedUser_de = new HashMap<String, Boolean>();
		for (int i = 0; i < dh.size(); i++) {
			String[] tmp = dh.get(i).split("\\t");
			if (tmp != null && tmp.length > 1) {
				// print doc_de
				fileWriterWithDataDir(tmp[0] + "\n", doc_de, true);

				for (int j = 1; j < tmp.length; j = j + 2) {
					if (!printedUser_de.containsKey(tmp[j + 1])) {
						printedUser_de.put(tmp[j + 1], true);

						// add user [j+1]
						user_tree.put(tmp[j + 1], true);

						// print doc_user_bot_de
						fileWriterWithDataDir(
								tmp[0] + "\t" + tmp[j + 1] + "\n",
								doc_user_bot_de, true);
					}

				}

			}
		}

		// print user
		Set<String> user_set = user_tree.keySet();
		for (String u : user_set) {
			fileWriterWithDataDir(u + "\n", user, true);
		}
	}

	// /////////////////////
	/**
	 * 
	 * @param text
	 * @return
	 */
	public static String cleanArticle(String text) {
		return text.toLowerCase().replaceAll("[^\\x00-\\x7F]", "")
				.replaceAll("[^a-z0-9- ]+", "");
	}

	// ////////////////////////
	/**
	 * 
	 * @param dataDir
	 * @param english_content
	 * @param german_content
	 * @param english_history
	 * @param german_history
	 */
	public static void readDataDir(String english_content,
			String german_content, String english_history, String german_history) {
		/**
		 * Read files from directory
		 */
		TreeMap<String, ArrayList<String>> englishHistory = new TreeMap<String, ArrayList<String>>();
		TreeMap<String, ArrayList<String>> germanHistory = new TreeMap<String, ArrayList<String>>();

		File folder = new File(DATA_DIR);
		File[] listOfFiles = folder.listFiles();

		String fileName = "";
		for (File file : listOfFiles) {
			if (!file.isDirectory()) {
				fileName = file.getName();
				if (fileName.endsWith("_en_content")) {
					convertWikiContentToPltmDataFormat(fileName,
							english_content);
				} else if (fileName.endsWith("_de_content")) {
					convertWikiContentToPltmDataFormat(fileName, german_content);
				} else if (fileName.endsWith("_en_history")) {
					ArrayList<String> history = filterUserHistoryInfo(fileName);
					englishHistory.put(
							fileName.replaceAll("_en_history", "_en_content"),
							history);
				} else if (fileName.endsWith("_de_history")) {
					ArrayList<String> history = filterUserHistoryInfo(fileName);
					germanHistory.put(
							fileName.replaceAll("_de_history", "_de_content"),
							history);
				} else {
					// nothing
				}
			}
		}
		Set<String> enHistorySet = englishHistory.keySet();
		for (String file : enHistorySet) {
			ArrayList<String> data_user = englishHistory.get(file);
			Preprocess.fileWriterWithDataDir(file, english_history, true);
			for (int i = 0; i < data_user.size(); i++) {
				Preprocess.fileWriterWithDataDir("\t" + data_user.get(i),
						english_history, true);
			}
			Preprocess.fileWriterWithDataDir("\n", english_history, true);

		}
		// //
		// // // Print german user history
		Set<String> deHistorySet = germanHistory.keySet();
		for (String file : deHistorySet) {
			ArrayList<String> data_user = germanHistory.get(file);
			Preprocess.fileWriterWithDataDir(file, german_history, true);
			for (int i = 0; i < data_user.size(); i++) {
				Preprocess.fileWriterWithDataDir("\t" + data_user.get(i),
						german_history, true);
			}
			Preprocess.fileWriterWithDataDir("\n", german_history, true);

		}
	}

	// //////////////////////////////
	/**
	 * 
	 * @param enTopic
	 */
	public static ArrayList<String> filterUserHistoryInfo(String userHistoryFile) {
		String lookfor1 = "<span class='history-user'>";
		String lookfor2 = "<span class=";
		String lookfor3 = "class=\"mw-changeslist-date\">";// 09:59, 7 October
															// 2012</a>
		ArrayList<String> userDateList = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(DATA_DIR + "/"
					+ userHistoryFile);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				int index1 = strLine.indexOf(lookfor1);
				if (index1 >= 0) {
					// User information
					int index2 = strLine.indexOf(lookfor2,
							index1 + lookfor1.length());
					if (index2 >= 0) {
						String tmpStr = strLine.substring(
								index1 + lookfor1.length(), index2);
						int index3 = tmpStr.indexOf(">");
						if (index3 >= 0) {
							int index4 = tmpStr.indexOf("<", index3);
							if (index4 >= 0) {
								// Date information
								int index5 = strLine.indexOf(lookfor3);
								if (index5 >= 0) {
									int index6 = strLine.indexOf(">", index5);
									int index7 = strLine.indexOf("<", index5);

									userDateList.add(strLine.substring(
											index6 + 1, index7)
											+ "\t"
											+ tmpStr.subSequence(index3 + 1,
													index4));
								}

							}

						}

					}
				}

			}
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return userDateList;
	}

	// //////////////////////////////////////////
	/**
	 * convert a text (article content file) to appropriate format for PLTML the
	 * file name (title) becomes docID
	 * 
	 * @param filename
	 * @param outFile
	 */
	public static void convertWikiContentToPltmDataFormat(String filename,
			String outFile) {
		try {
			FileInputStream fstream = new FileInputStream(DATA_DIR + "/"
					+ filename);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			Preprocess.fileWriterWithDataDir(filename + "\t" + "X" + "\t",
					outFile, true);
			while ((strLine = br.readLine()) != null) {
				Preprocess.fileWriterWithDataDir(strLine.trim() + " ", outFile,
						true);
			}
			Preprocess.fileWriterWithDataDir("\n", outFile, true);
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	// //////////////////////////////////////////////
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static String wikiDocContentExtractor(String fileName) {
		StringBuffer content = new StringBuffer();

		try {
			FileInputStream fstream = new FileInputStream(DATA_DIR + "/"
					+ fileName);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			boolean extract = false;
			while ((strLine = br.readLine()) != null) {
				if (strLine.contains("<textarea ")) {
					extract = true;
				}
				if (strLine.contains("</textarea>")) {
					extract = false;
				}
				if (extract) {
					content.append(strLine + "\n");
				}
			}

			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return content.toString().replaceAll("\\n", "").replaceAll("\\t", "");// replace
																				// newline/tab
	}

	// //////////////////
	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static ArrayList<String> readFile(String filename) {
		ArrayList<String> output = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(filename);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				output.add(strLine);

			}
			fstream.close();
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return output;
	}

	// ///////////////////////////////////////////////
	/**
	 * sort file line by line
	 * 
	 * @param fileIn
	 * @param fileOut
	 */
	public static void sortFile(String fileIn, String fileOut) {
		try {
			FileInputStream fstream = new FileInputStream(DATA_DIR + "/"
					+ fileIn);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			TreeMap<String, Boolean> tree = new TreeMap<String, Boolean>();
			while ((strLine = br.readLine()) != null) {
				tree.put(strLine, true);
			}

			Set<String> set = tree.keySet();
			for (String s : set) {
				fileWriterWithDataDir(s + "\n", fileOut, true);
			}
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.out.println("Can't sort file, maybe it's too big");
			System.err.println("Error: " + e.getMessage());
		}

	}

	// ///////////////////////////////
	/**
	 * 
	 * @param english_content_sorted
	 * @param german_content_sorted
	 * @param doc_link_en
	 * @param doc_link_de
	 */
	public void generateDocHyperLink(String english_content_sorted,
			String german_content_sorted, String doc_link_en, String doc_link_de) {
		try {
			// generate doc_link for english
			FileInputStream fstream = new FileInputStream(
					english_content_sorted);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] title_label_content = strLine.split("\\t");
				if (title_label_content != null
						&& title_label_content.length == 3) {
					ArrayList<String> links = getHyperLinks(title_label_content[2]);

					for (int i = 0; i < links.size(); i++) {
						Preprocess
								.fileWriterWithDataDir(title_label_content[0]
										+ "\t" + links.get(i) + "\n",
										doc_link_en, true);
					}
				}
			}
			fstream.close();
			in.close();

			// generate doc_link for german
			fstream = new FileInputStream(german_content_sorted);

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				String[] title_label_content = strLine.split("\\t");
				if (title_label_content != null
						&& title_label_content.length == 3) {
					ArrayList<String> links = getHyperLinks(title_label_content[2]);

					for (int i = 0; i < links.size(); i++) {
						Preprocess
								.fileWriterWithDataDir(title_label_content[0]
										+ "\t" + links.get(i) + "\n",
										doc_link_de, true);
					}
				}
			}

			fstream.close();
			in.close();
		} catch (Exception e) {

		}

	}

	// ///////////////////////////
	/**
	 * 
	 * @param text
	 * @return
	 */
	public static ArrayList<String> getWikiLinks(String text) {
		ArrayList<String> output = new ArrayList<String>();

		try {
			Pattern patt = Pattern.compile(regexWikiLink);
			Matcher matcher = patt.matcher(text);

			while (matcher.find()) {
				output.add(matcher.group());
			}
		} catch (RuntimeException e) {

		}
		return output;
	}

	// //////////////////////////////////
	/**
	 * 
	 * @param file
	 * @return
	 */
	public static ArrayList<String> getHyperLinks(String text) {
		ArrayList<String> output = new ArrayList<String>();

		try {
			int start = 0, end = 0;
			TreeMap<String, Boolean> hash = new TreeMap<String, Boolean>();
			while (start >= 0 && end >= 0) {
				start = text.indexOf("&lt;ref>", start);
				if (start >= 0) {
					end = text.indexOf("&lt;/ref", start + 1);
					if (end >= 0) {
						// print hyperlink
						hash.put(
								findOneUrl(text.substring(start + 8, end),
										regex), true);
						// System.out.println(text.substring(start + 8, end));
						start = end + 1;
					}
				}
			}

			//
			Set<String> set = hash.keySet();
			for (String s : set) {
				if (s.length() > 0) {// avoid empty link
					output.add(s);
				}
			}

		} catch (Exception e) {
			System.out.println("ERROR");
		}
		return output;

	}

	// //////////////////////////////////
	/**
	 * 
	 * @param s
	 * @param pattern
	 * @return
	 */
	private static String findOneUrl(String s, String pattern) {
		try {
			Pattern patt = Pattern.compile(pattern);
			Matcher matcher = patt.matcher(s);

			while (matcher.find()) {
				return matcher.group();
			}
		} catch (RuntimeException e) {

		}
		return "";
	}

	// /////////////////////////////////////////////
	/**
	 * 
	 * @param str
	 * @param fileName
	 * @param append
	 */
	public static void fileWriterWithDataDir(String str, String fileName,
			boolean append) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(DATA_DIR
					+ "/" + fileName, append));
			output.write(str);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// /////////////////////////////////////////////
	/**
	 * 
	 * @param str
	 * @param fileName
	 * @param append
	 */
	public static void fileWriter(String str, String fileName, boolean append) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(fileName,
					append));
			output.write(str);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
