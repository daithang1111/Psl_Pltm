package edu.umd.umiacs.clip.tn.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class WikiDocSampler {

	HashMap<String, Boolean> hashTopics = new HashMap<String, Boolean>();;
	int SNOWBALL_MAX_LEVEL = 1;

	EnglishGermanDocWikiCrawler pwc = new EnglishGermanDocWikiCrawler();

	public static void main(String args[]) throws InterruptedException {

	}

	// ////////////////////////////
	/**
	 * 
	 * @param l
	 */
	public void setMaxLevel(int l) {
		SNOWBALL_MAX_LEVEL = l;
	}

	// //////////////////////////////
	/**
	 * check if topic1 contains wikilink to topic2
	 * 
	 * @param topic1
	 * @param topic2
	 * @return
	 * @throws InterruptedException
	 */
	public boolean existLink(String topic1, String topic2)
			throws InterruptedException {
		// get all link from first topic
		ArrayList<String> wikiLinks = pwc.enWikiLinkCrawler(topic1);

		for (int i = 0; i < wikiLinks.size(); i++) {
			if (topic2.equalsIgnoreCase(wikiLinks.get(i)))
				return true;
		}

		// if topic1 links don't contain topic2
		wikiLinks = pwc.enWikiLinkCrawler(topic2);
		for (int i = 0; i < wikiLinks.size(); i++) {
			if (topic1.equalsIgnoreCase(wikiLinks.get(i)))
				return true;
		}
		return false;
	}

	// ////////////////////////////////////////////////////
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void crawl(String initTopic) throws InterruptedException {
		snowBallSampling(initTopic, 0);
		Set<String> topics = hashTopics.keySet();
		String enContent = "", deContent = "";
		for (String enTopic : topics) {
			System.out.println("Crawling... " + enTopic);
			if (pwc.enCrawler(enTopic)) { // extract content
				enContent = Preprocess.wikiDocContentExtractor(enTopic + "_en");
				deContent = Preprocess.wikiDocContentExtractor(enTopic + "_de");

				if (enContent.length() > 0 && deContent.length() > 0) {
					Preprocess.fileWriterWithDataDir(enContent, enTopic
							+ "_en_content", true);

					Preprocess.fileWriterWithDataDir(deContent, enTopic
							+ "_de_content", true);
				}
			}
		}
	}

	// /////////////////////////////////////////////////////
	/**
	 * The initial topic is an english topic in wiki
	 * 
	 * @param initTopic
	 * @param level
	 * @throws InterruptedException
	 */
	public void snowBallSampling(String initTopic, int level)
			throws InterruptedException {

		if (level >= SNOWBALL_MAX_LEVEL) {
			return;
		}

		// crawl
		System.out.println("Crawling...==========>>>" + initTopic);
		ArrayList<String> wikiLinks = pwc.enWikiLinkCrawler(initTopic);

		for (int i = 0; i < Math.min(Preprocess.MAX_PER_PAGE, wikiLinks.size()); i++) {
			if (!hashTopics.containsKey(wikiLinks.get(i))) {
				Thread.sleep(10);
				// put current topic into
				hashTopics.put(wikiLinks.get(i), true);
				snowBallSampling(wikiLinks.get(i), level + 1);
			}
		}
		// at the end of this function, hasTopics contains all english topics
		// which link together
		// and each of these articles also contain link to at least 1 german
		// articleS
	}

}
