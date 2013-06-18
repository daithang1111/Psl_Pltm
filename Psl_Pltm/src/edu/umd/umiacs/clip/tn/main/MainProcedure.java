package edu.umd.umiacs.clip.tn.main;

import edu.umd.umiacs.clip.tn.crawler.WikiDocSampler;
import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class MainProcedure {

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out
					.println("Please enter an initial topic, and iteration number to crawl");
			System.exit(0);
		}

		/**
		 * NOTE: docid is the english name on wikipedia of an article
		 */
		/**
		 * Step1: Run snow ball sampling to get all linked topics For each
		 * topic, get all documents and its history INPUT: initial topic OUTPUT:
		 * all files of forms: docid_en_content, docid_de_content,
		 * docid_en_history, docid_de_history
		 * 
		 */
		try {
			WikiDocSampler reader = new WikiDocSampler();
			reader.setMaxLevel(Integer.parseInt(args[1]));
			reader.crawl(args[0]);// "Abraham_Wald");//args[0]);// crawl
		} catch (Exception e) {
			System.out.println("Error crawling " + args[0]);
			System.exit(0);
		}
		/**
		 * Step2: Generate huge english_content, german_content,
		 * english_history, german_history INPUT: data dir OUTPUT: four files
		 * while english_content and german_content are PLTM file format, and
		 * english_history, german_history have format of
		 * docid|time1|user1|time2|user2....
		 */
		try {
			Preprocess.readDataDir("english_content", "german_content",
					"english_history", "german_history");
		} catch (Exception e) {
			System.out.println("Error readDataDir " + Preprocess.DATA_DIR);
			System.exit(0);
		}
		/**
		 * Step3: Sort english_content ->english_content_sorted, german_content
		 * ->german_content_sorted
		 */
		try {
			Preprocess.sortFile("english_content", "english_content_sorted");
			Preprocess.sortFile("german_content", "german_content_sorted");
		} catch (Exception e) {
			System.out
					.println("Error sorting file, this can be done using command line ");
			System.exit(0);
		}
		/**
		 * This steps use english/german(_content_sorted) and
		 * english/german_(history) Step4: Generate metadata such as user,
		 * doc_en, doc_de, doc_user_bot_en, doc_user_bot_de
		 */
		try {
			Preprocess.generateMeta("english_history", "german_history",
					"user", "doc_en", "doc_de", "doc_user_bot_en",
					"doc_user_bot_de");
		} catch (Exception e) {
			System.out.println("Error generating meta file");
			System.exit(0);
		}
		/**
		 * Step5: Generate doc_user_en, doc_user_de, doc_bot_en, doc_bot_de
		 */
		try {
			Preprocess.separateUserBot("doc_user_bot_en", "doc_user_bot_de",
					"doc_user_en", "doc_user_de", "doc_bot_en", "doc_bot_de");
		} catch (Exception e) {
			System.out.println("Error separating bot/user");
			System.exit(0);
		}
		/**
		 * Step6: Generate doc_link_en, doc_link_de
		 */
		try {
			Preprocess.generateHyperLinks("english_content_sorted",
					"doc_link_en");
			Preprocess.generateHyperLinks("german_content_sorted",
					"doc_link_de");
		} catch (Exception e) {
			System.out.println("Error generating hyperlinks, doc_link files");
			System.exit(0);
		}

		/**
		 * Step7: Generate doc_doc_en, doc_doc_de (wikilinks)
		 */
		try {
			Preprocess.generateEnglishWikiLinks("english_content_sorted",
					"doc_doc_en");
			Preprocess.generateGermanWikiLinks("german_content_sorted",
					"doc_doc_de");
		} catch (Exception e) {
			System.out.println("Error generating wikilinks");
			System.exit(0);
		}
		// That's all for wiki data

	}
}