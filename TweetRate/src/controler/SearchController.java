/*
 * Controller managing the search textfield
 */

package controler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import model.TweetAction;
import view.InterfaceG;

public class SearchController implements ActionListener {
	private TweetAction model;
	InterfaceG view;

	public SearchController(TweetAction model, InterfaceG view) {
		this.model = model;
		this.view = view;
	}

	public void actionPerformed(ActionEvent e) {
		String word = ((JTextField) e.getSource()).getText();

		if (!word.equals("")) {
			model.recherche = word;
			model.doSearch(word);
			view.idTweet.setText("");
			view.usernameTweet.setText("");
			view.tweetText.setText("");
			view.rateComboBox.setSelectedIndex(-1);
			view.tweetText.setBackground(Color.GRAY);
		}
	}
}
