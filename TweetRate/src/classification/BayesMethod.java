/*
 * Ranking method using bayes
 */

package classification;

import java.util.List;

import model.Tweet;
import model.TweetAction;
import model.TweetAction.Rate;


public class BayesMethod {


	public static int[] nbTweetsMood(List<Tweet> listMood) {
		int[] res = new int[2];
		int cpt = 0;
		res[0] = listMood.size();
		for (int i = 0; i < listMood.size(); i++) {
			cpt += listMood.get(i).getTweet().length();
		}
		res[1] = cpt;
		return res;
	}


	public static String[] tabMotsMood(List<Tweet> listMood) {
		int tab[] = nbTweetsMood(listMood);
		String resTmp[] = new String[tab[1]];
		int cpt = 0;
		int i, j;
		for (i = 0; i < listMood.size(); i++) {
			String[] tmp = listMood.get(i).getTweet().split(" ");
			for (j = 0; j < tmp.length; j++) {
				resTmp[cpt] = tmp[j];
				cpt++;
			}
		}
		int cpt2 = 1;
		for (i = 1; i < resTmp.length; i++) {
			int alreadyPresent = 0;
			for (j = 0; j < i; j++) {
				if (resTmp[i] == resTmp[j])
					alreadyPresent = 1;
			}
			if (alreadyPresent == 0)
				cpt2++;
		}
		String res[] = new String[cpt2];
		int tmp = 1;
		res[0] = resTmp[0];
		for (i = 0; i < cpt2; i++) {
			int alreadyPresent = 0;
			for (j = 0; j < i; j++) {
				if (resTmp[i] == resTmp[j])
					alreadyPresent = 1;
			}
			if ((alreadyPresent == 0) && (tmp < cpt2)) {
				res[tmp] = resTmp[i];
				tmp++;
			}
		}
		return res;
	}


	public static int[] nbMots(String[] mots, List<Tweet> listMood) {
		int nbMots[] = new int[mots.length];
		int i, j, k;
		for (i = 0; i < mots.length; i++) {
			nbMots[i] = 0;
		}
		for (i = 0; i < listMood.size(); i++) {
			String tmp[] = listMood.get(i).getTweet().split(" ");
			for (j = 0; j < tmp.length; j++) {
				for (k = 0; k < mots.length; k++) {
					if (tmp[j].equals(mots[k])) {
						nbMots[k]++;
					}
				}
			}
		}
		return nbMots;
	}

	public static int nbMotsTotalMood(int[] nbMots) {
		int nbTot = 0;
		for (int nbMot : nbMots) {
			nbTot += nbMot;
		}
		return nbTot;
	}

	public static String[] tweetToTab(String tweet) {
		String[] tab = tweet.split(" ");
		if (tweet.length() == 0) {
			return tab;
		}
		int cpt = 1;
		int tmp;
		int pos = 1;
		for (int i = 1; i < tab.length; i++) {
			tmp = 0;
			for (int j = 0; j < i; j++) {
				if (tab[i].equals(tab[j])) {
					tmp = 1;
				}
			}
			if (tmp == 0) {
				cpt++;
			}
		}
		String motsTweet[] = new String[cpt];
		motsTweet[0] = tab[0];
		for (int k = 1; k < tab.length; k++) {
			tmp = 0;
			for (int l = 0; l < pos; l++) {
				if (motsTweet[l].equals(tab[k])) {
					tmp = 1;
				}
			}
			if (tmp == 0) {
				motsTweet[pos] = tab[k];
				pos++;
			}
		}
		return motsTweet;
	}

	public static int[] nbMotstweet(String tweet) {
		int cpt;
		String[] motsTweet = tweetToTab(tweet);
		int[] nbMots = new int[motsTweet.length];
		String[] sp = tweet.split(" ");
		for (int i = 0; i < motsTweet.length; i++) {
			cpt = 0;
			for (String element : sp) {
				if (element.equals(motsTweet[i])) {
					cpt += 1;
				}
			}
			nbMots[i] = cpt;
		}
		return nbMots;
	}

	public static float probaTweetMood(List<Tweet> listTweets,
			List<Tweet> listMood, List<Tweet> secondList,
			List<Tweet> thirdList, String tweet, int classif) {
		int nbTweetsMood[] = nbTweetsMood(listTweets);
		int nbTweets = listTweets.size();

		String[] motsMood = tabMotsMood(listMood);
		String[] secondMotsMood = tabMotsMood(secondList);
		String[] thirdMotsMood = tabMotsMood(thirdList);

		int nbMotsMood[] = nbMots(motsMood, listMood);
		int nbMotsMoodSecond[] = nbMots(secondMotsMood, secondList);
		int nbMotsMoodThird[] = nbMots(thirdMotsMood, thirdList);

		int nbMotsTotMood = nbMotsTotalMood(nbMotsMood);
		int nbMotsTotSecond = nbMotsTotalMood(nbMotsMoodSecond);
		int nbMotsTotThird = nbMotsTotalMood(nbMotsMoodThird);

		int nbTotal = nbMotsTotMood + nbMotsTotSecond + nbMotsTotThird;

		String[] motsTweet = tweetToTab(tweet);
		int[] nbMotsTweet = nbMotstweet(tweet);
		float probaMood = 0;
		probaMood = nbTweetsMood[0] / nbTweets;
		float proba = 0;
		for (int i = 0; i < motsTweet.length; i++) {
			if (motsTweet[i].length() > 3) {
				for (int j = 0; j < motsMood.length; j++) {
					float probaMot = 0;
					if (motsMood[j].equals(motsTweet[i])) {
						if (motsMood[j].length() > 3) {
							if (proba == 0) {
								proba = 1;
							}
							if (classif == 0) {
								//Presence 
								probaMot = (float) (nbMotsMood[j] + 1)
										/ (float) (nbMotsTotMood + nbTotal);
							} else {
								//Frequence
								probaMot = (float) Math.pow(
										((double) (nbMotsMood[j] + 1) / (double) (nbMotsTotMood + nbTotal)),
										nbMotsTweet[i]);

							}
						}
					}
					if (probaMot != 0) {
						proba *= probaMot;
					}
				}
			}
		}
		proba *= probaMood;
		return proba;
	}

	/**
	 * classif = 0 for presence 
	 * 	       = 1 for frequence
	 */
	public static int rankBayes(List<Tweet> listTweets, String tweet, int classif) {
		List<Tweet> listNeg, listPos, listNeu;
		listNeg = TweetAction.getTweetByClasse(Rate.NEGATIVE);
		listNeu = TweetAction.getTweetByClasse(Rate.NEUTRAL);
		listPos = TweetAction.getTweetByClasse(Rate.POSITIVE);

		float probaPos = probaTweetMood(listTweets, listPos, listNeg, listNeu, tweet, classif);
		float probaNeg = probaTweetMood(listTweets, listNeg, listPos, listNeu, tweet, classif);
		float probaNeu = probaTweetMood(listTweets, listNeu, listPos, listNeg, tweet, classif);

		float max = Math.max(probaPos, Math.max(probaNeg, probaNeu));
		return (max == probaPos) ? 4 : ((max == probaNeu) ? 2 : 0);
	}
}
