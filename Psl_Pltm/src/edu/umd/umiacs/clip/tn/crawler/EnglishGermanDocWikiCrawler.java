package edu.umd.umiacs.clip.tn.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class EnglishGermanDocWikiCrawler {

	public static final String FIND_EN = "[[en:";
	public static final String FIND_DE = "[[de:";
	public static final String EN_WIKI_URL = "http://en.wikipedia.org/w/index.php?title=";
	public static final String DE_WIKI_URL = "http://de.wikipedia.org/w/index.php?title=";
	public static final String ACTION_EDIT = "&action=edit";
	public static final String ACTION_HISTORY = "&action=history";
	public static final String DE_WIKI_LINK_SIG = "<li class=\"interwiki-de\"><a href=";

	public static void main(String args[]) {

	}

	// /////////////////////////////////////////
	/**
	 * 
	 * @param enTopic
	 * @throws InterruptedException
	 */
	public String enHistoryCrawler(String enTopic) throws InterruptedException {
		StringBuffer enContent = new StringBuffer();
		try {
			String enLink = EN_WIKI_URL + enTopic + ACTION_HISTORY;
			// Create a URL for the desired page
			URL url = new URL(enLink);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			boolean hasHistory = false;

			while ((str = in.readLine()) != null) {
				if (str.contains("<ul id=\"pagehistory\">")) {
					hasHistory = true;
				}
				if (str.contains("</ul>")) {
					hasHistory = false;
				}

				if (hasHistory) {
					enContent.append(str + "\n");
				}

			}

			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return enContent.toString();
	}

	// ///////////////////////////////////////////
	/**
	 * 
	 * @param deTopic
	 * @return
	 * @throws InterruptedException
	 */
	public String deHistoryCrawler(String deTopic) throws InterruptedException {
		StringBuffer deContent = new StringBuffer();
		try {
			String deLink = DE_WIKI_URL + deTopic + ACTION_HISTORY;
			// Create a URL for the desired page
			URL url = new URL(deLink);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			boolean hasHistory = false;

			while ((str = in.readLine()) != null) {
				if (str.contains("<ul id=\"pagehistory\">")) {
					hasHistory = true;
				}
				if (str.contains("</ul>")) {
					hasHistory = false;
				}

				if (hasHistory) {
					deContent.append(str + "\n");
				}

			}

			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return deContent.toString();
	}

	// /////////////////////////////////////////////
	/**
	 * 
	 * @param enTopic
	 * @throws InterruptedException
	 */
	public boolean enCrawler(String enTopic) throws InterruptedException {

		boolean hasEN_DE = false;
		try {
			// check original link to find german link
			String enLink = EN_WIKI_URL + enTopic;
			// Create a URL for the desired page
			URL url = new URL(enLink);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			String deTopic = "";
			while ((str = in.readLine()) != null) {
				if (str.contains(DE_WIKI_LINK_SIG)) {

					int tmpStart = str.indexOf("title=\"");
					tmpStart += 7;
					int tmpEnd = str.indexOf("\" lang=\"de\" ");
					deTopic = str.substring(tmpStart, tmpEnd).replace(" ", "_")
							.replace("&amp;", "&");
					break;
				}
			}
			in.close();

			// only crawl english article if there exists an german article
			if (deTopic.length() > 0) {
				// now looking into real edit content to extract content
				enLink = EN_WIKI_URL + enTopic + ACTION_EDIT;
				// Create a URL for the desired page
				url = new URL(enLink);

				// Read all the text returned by the server
				in = new BufferedReader(new InputStreamReader(url.openStream()));
				StringBuffer enContent = new StringBuffer();
				while ((str = in.readLine()) != null) {
					// System.out.println(str);
					enContent.append(str + "\n");
				}

				in.close();

				// only craw article if length >MAX_CRAWL_LENGTH
				if (enContent.length() > Preprocess.DOC_LENGTH_THRESHOLD) {
					Preprocess.fileWriterWithDataDir(enTopic + "\t" + deTopic
							+ "\n", Preprocess.EN_DE_TOPICS, true);
					hasEN_DE = true;
					Preprocess.fileWriterWithDataDir(enContent.toString(),
							enTopic + "_en", true);
					Thread.sleep(10); // to avoid crawl too fast

					Preprocess.fileWriterWithDataDir(deCrawler(deTopic),
							enTopic + "_de", true);
					Thread.sleep(10); // to avoid crawl too fast

					Preprocess.fileWriterWithDataDir(enHistoryCrawler(enTopic),
							enTopic + "_en_history", true);
					Thread.sleep(0); // to avoid crawl too fast

					Preprocess.fileWriterWithDataDir(deHistoryCrawler(deTopic),
							enTopic + "_de_history", true);
				}

			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return hasEN_DE;
	}

	// /////////////////////////////////////////////////
	/**
	 * 
	 * @param deTopic
	 */
	public String deCrawler(String deTopic) {
		StringBuffer deContent = new StringBuffer();
		try {
			String deLink = DE_WIKI_URL + deTopic + ACTION_EDIT;
			// Create a URL for the desired page
			URL url = new URL(deLink);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			while ((str = in.readLine()) != null) {
				deContent.append(str + "\n");
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return deContent.toString();
	}

	// ////////////////////////////////////////////
	/**
	 * 
	 * @param enTopic
	 * @throws InterruptedException
	 */
	public ArrayList<String> enWikiLinkCrawler(String enTopic)
			throws InterruptedException {
		ArrayList<String> newEnTopics = new ArrayList<String>();
		HashMap<String, Boolean> linkedTopics = new HashMap<String, Boolean>();
		try {
			// check original link to find german link
			String enLink = EN_WIKI_URL + enTopic;
			// Create a URL for the desired page
			URL url = new URL(enLink);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			String deTopic = "";
			while ((str = in.readLine()) != null) {
				if (str.contains(DE_WIKI_LINK_SIG)) {

					int tmpStart = str.indexOf("title=\"");
					tmpStart += 7;
					int tmpEnd = str.indexOf("\" lang=\"de\" ");
					deTopic = str.substring(tmpStart, tmpEnd).replace(" ", "_")
							.replace("&amp;", "&");
					break;
				}
			}
			in.close();

			// only crawl english article if there exists an german article
			if (deTopic.length() > 0) {
				// now looking into real edit content to extract content
				enLink = EN_WIKI_URL + enTopic + ACTION_EDIT;
				// Create a URL for the desired page
				url = new URL(enLink);

				// Read all the text returned by the server
				in = new BufferedReader(new InputStreamReader(url.openStream()));
				StringBuffer enContent = new StringBuffer();
				while ((str = in.readLine()) != null) {
					// System.out.println(str);
					enContent.append(str + "\n");
				}

				in.close();
				// find all wiki link to this english articles
				int startIndex = enContent.toString().indexOf("<textarea ");
				if (startIndex != -1) {
					int endIndex = enContent.toString().indexOf("</textarea>",
							startIndex);
					if (endIndex != -1) {
						String enTextContent = enContent.toString().substring(
								startIndex, endIndex);
						// look for url
						ArrayList<String> links = Preprocess
								.getWikiLinks(enTextContent);
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
							linkedTopics.put(realLink, true);

						}

					}

				}

			}

		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}

		Set<String> linkedSet = linkedTopics.keySet();
		for (String linked : linkedSet) {
			newEnTopics.add(linked);
		}
		return newEnTopics;
	}

	// ////////////////////////////////////////////
	/**
	 * This function assumes that this german article already has link to
	 * english article
	 * 
	 * @param germanTopic
	 * @return
	 * @throws InterruptedException
	 */
	public ArrayList<String> deWikiLinkCrawler(String germanTopic)
			throws InterruptedException {

		ArrayList<String> newDeTopics = new ArrayList<String>();
		HashMap<String, Boolean> linkedTopics = new HashMap<String, Boolean>();
		try {
			String germanLink = DE_WIKI_URL + germanTopic + ACTION_EDIT;
			// Create a URL for the desired page
			URL url = new URL(germanLink);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			StringBuffer deContent = new StringBuffer();
			while ((str = in.readLine()) != null) {
				deContent.append(str + "\n");
			}

			// find all wiki link to this german articles
			int startIndex = deContent.toString().indexOf("<textarea ");
			if (startIndex != -1) {
				int endIndex = deContent.toString().indexOf("</textarea>",
						startIndex);
				if (endIndex != -1) {
					String deTextContent = deContent.toString().substring(
							startIndex, endIndex);
					// look for url
					ArrayList<String> links = Preprocess
							.getWikiLinks(deTextContent);
					for (int i = 0; i < links.size(); i++) {
						String oneLink[] = links.get(i).trim().replace("[", "")
								.replace("]", "").split("\\|");
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
						realLink = realLink.replace(" ", "_").replace("&amp;",
								"&");
						String firstLetter = realLink.substring(0, 1)
								.toUpperCase();
						realLink = firstLetter + realLink.substring(1);
						// newDeTopics.add(realLink);
						linkedTopics.put(realLink, true);

					}

				}

			}

			// }
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		Set<String> linkedSet = linkedTopics.keySet();
		for (String linked : linkedSet) {
			newDeTopics.add(linked);
		}
		return newDeTopics;
	}
}
