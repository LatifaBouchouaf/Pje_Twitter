/*
 * Ranking method using KNN
 */

package classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Tweet;

public class KnnMethod {


	private static int distanceTweet(String t1, String t2) {
		int total = 0, commonWords = 0;
		
		String[] tab1 = t1.split(" ");
		String[] tab2 = t2.split(" ");
		total = tab1.length + tab2.length;

		for (String wordT1 : tab1) {
			for (String wordT2 : tab2) {
				if (wordT1.equals(wordT2)) {
					commonWords++;
				}
			}
		}

		return ((total - commonWords) / total);
	}


	private static String compareNeighbourDist(int tweetDist, Map<String, Integer> neighbourDist) {
		int max = 0;
		String mostDistant = null;

		for (Entry<String, Integer> entry : neighbourDist.entrySet()) {
			String key = entry.getKey();
			int distance = entry.getValue();
			if (max < distance) {
				max = distance;
				mostDistant = key;
			}
		}

		if (max > tweetDist)
			return mostDistant;
		else return null;
	}


	private static int vote(Map<String, Integer> neighbours) {
		int class0 = 0, class2 = 0, class4 = 0, max;

		for (Entry<String, Integer> entry : neighbours.entrySet()) {
			switch (entry.getValue()) {
			case 0:
				class0++;
				break;
			case 2:
				class2++;
				break;
			case 4:
				class4++;
				break;
			}
		}

		max = Math.max(class0, Math.max(class2, class4));
		return (max == class0) ? 0 : ((max == class2) ? 2 : 4);
	}


	public static int knn(String tweet, int nbNeighbours, List<Tweet> listName) throws IOException {
		Map<String, Integer> neighbours = new HashMap<String, Integer>(nbNeighbours);
		Map<String, Integer> distNeighbours = new HashMap<String, Integer>(nbNeighbours);
		int i, j;
		
		String newTweet;
		int note;
		
		for (i = 0; i < nbNeighbours; i++) {
			newTweet = listName.get(i).getTweet();
			note = listName.get(i).getNote();

			distNeighbours.put(newTweet, distanceTweet(newTweet, tweet));
			neighbours.put(newTweet, note);
		}

		for (j = i; j < listName.size(); j++) {
			newTweet = listName.get(j).getTweet();
			note = listName.get(j).getNote();

			int newDistance = distanceTweet(newTweet, tweet);
			String key = compareNeighbourDist(distanceTweet(newTweet, tweet), distNeighbours);

			if (key != null) {
				neighbours.remove(key);
				neighbours.put(newTweet, note);
				distNeighbours.remove(key);
				distNeighbours.put(newTweet, newDistance);
			}
		}

		return vote(neighbours);
	}

}
