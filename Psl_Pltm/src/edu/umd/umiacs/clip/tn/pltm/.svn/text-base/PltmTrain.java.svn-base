package edu.umd.umiacs.clip.tn.pltm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.PolylingualTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.InstanceList;
import edu.umd.umiacs.clip.tn.preprocess.Preprocess;

public class PltmTrain {

	int numTopics = 10;
	PolylingualTopicModel model;
	InstanceList[] trains;
	InstanceList[] tests;
	int langLen = 2;
	public static final String stoplistDir = "stoplists/";

	public static void main(String[] args) throws Exception {
		System.out.println("_____TRAINING PLTM ....START___");
		if (args.length == 6) {
			String train_en = args[0];
			String train_de = args[1];
			int topic = Integer.parseInt(args[2]);
			int iterate = Integer.parseInt(args[3]);
			String test_en = args[4];
			String test_de = args[5];

			InstanceList wiki_train_en = readData(train_en, "en");
			InstanceList wiki_train_de = readData(train_de, "de");
			InstanceList wiki_test_en = readData(test_en, "en");
			InstanceList wiki_test_de = readData(test_de, "de");

			PltmTrain pp = new PltmTrain();

			InstanceList[] trains = new InstanceList[] { wiki_train_en,
					wiki_train_de };
			try {
				pp.train(trains, topic, iterate);
				DocVector[] dvs_en = pp.test(wiki_test_en, 0);
				DocVector[] dvs_de = pp.test(wiki_test_de, 1);

				String printOut = test_en + ".dvs";
				// print english vectors for test_en
				for (int i = 0; i < dvs_en.length; i++) {
					Preprocess.fileWriter(dvs_en[i].getDocName() + "\n", printOut,
							true);
					double[] props = dvs_en[i].getVectors();
					for (int j = 0; j < props.length; j++) {
						Preprocess.fileWriter(props[j] + ":", printOut, true);
					}
					Preprocess.fileWriter("\n", printOut, true);
				}

				printOut = test_de + ".dvs";
				// print german vectors for test_en
				for (int i = 0; i < dvs_de.length; i++) {
					Preprocess.fileWriter(dvs_de[i].getDocName() + "\n", printOut,
							true);
					double[] props = dvs_de[i].getVectors();
					for (int j = 0; j < props.length; j++) {
						Preprocess.fileWriter(props[j] + ":", printOut, true);
					}
					Preprocess.fileWriter("\n", printOut, true);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			System.out
					.println("Program requires 6 parameters: train_en, train_de, num_of_topic, iteration, test_en, test_de");
			System.exit(0);
		}

	}

	////////////////////////////////
	/**
	 * 
	 * @param training
	 * @return
	 * @throws IOException
	 */
	public void train(InstanceList[] training, int numTopics, int iteration)
			throws IOException {
		this.numTopics = numTopics;
		model = new PolylingualTopicModel(numTopics);
		model.addInstances(training);
		model.setNumIterations(iteration);
		model.estimate();
	}

	///////////////////////////////////
	/**
	 * 
	 * @param training
	 * @param testing
	 * @param languageOrder
	 * @return
	 * @throws IOException
	 */
	public DocVector[] test(InstanceList testing, int language)
			throws IOException {

		TopicInferencer inferencer = model.getInferencer(language);
		DocVector[] dvs = new DocVector[testing.size()];

		double[] docProbabilities;

		for (int j = 0; j < testing.size(); j++) {
			docProbabilities = inferencer.getSampledDistribution(
					testing.get(j), 10, 1, 5);

			DocVector dv = new DocVector(testing.get(j).getName().toString(),
					docProbabilities);
			dvs[j] = dv;

		}

		return dvs;
	}

	////////////////////////////////////
	/**
	 * 
	 * @param file
	 * @param language
	 *            (en, de...)
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static InstanceList readData(String file, String language)
			throws UnsupportedEncodingException, FileNotFoundException {
		// Begin by importing documents from text to feature sequences
		ArrayList<Pipe> trainingPipe = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
		trainingPipe.add(new CharSequenceLowercase());
		trainingPipe.add(new CharSequence2TokenSequence(Pattern
				.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		trainingPipe.add(new TokenSequenceRemoveStopwords(new File(stoplistDir
				+ language + ".txt"), "UTF-8", false, false, false));
		trainingPipe.add(new TokenSequence2FeatureSequence());

		// training english, german
		InstanceList instances = new InstanceList(new SerialPipes(trainingPipe));

		Reader fileReader = new InputStreamReader(new FileInputStream(new File(
				file)), "UTF-8");

		instances.addThruPipe(new CsvIterator(fileReader, Pattern
				.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1));
		return instances;
	}
}