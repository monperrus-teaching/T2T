import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.trello4j.TrelloImpl;
import org.trello4j.model.Card;

public class UpdateBoard {

	final static String API_KEY = "1ae06ad5bee3715dbcfabb71342c54ed";

	final static String BOARD_KEY = "56af6582ccde1bf9a1bfd85a";

	final static String API_TOKEN = "ed4b95898da90220abc1c6e86e29e7b3d6b8a22bf5c759954a7cad3d678b5bfb";

	static TrelloImpl trello;

	public static void main(String[] args) {
		updateBoard();
	}

	private static void updateBoard() {
		trello = new TrelloImpl(API_KEY, API_TOKEN);

		// Creating the list, on top of the Trello board
		org.trello4j.model.List todolist = getTODOList();
		org.trello4j.model.List donelist = getDONEList();

		Map<String, String> listKeyValueMap = new HashMap<String, String>();
		listKeyValueMap.put("idList", donelist.getId());

		try {
			for (String line : Files.readAllLines(Paths.get("done.t2t"))) {
				// name (0), task desc. (1), date (2), class (3), method (4)
				String[] parts = line.split(";");

				Card formerCard = getCardByNameInList(parts[3] + ":" + parts[4], todolist.getId());

				formerCard = trello.updateCard(todolist.getId(), listKeyValueMap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static org.trello4j.model.List getTODOList() {
		org.trello4j.model.List finallist = null;

		trello.getBoard(BOARD_KEY);

		java.util.List<org.trello4j.model.List> lists = trello.getListByBoard(BOARD_KEY, (String[]) null);

		for (org.trello4j.model.List l : lists) {
			if (l.getName().equals("TODO"))
				finallist = l;
		}

		return finallist;
	}

	private static org.trello4j.model.List getDONEList() {
		org.trello4j.model.List finallist = null;

		trello.getBoard(BOARD_KEY);

		java.util.List<org.trello4j.model.List> lists = trello.getListByBoard(BOARD_KEY, (String[]) null);

		for (org.trello4j.model.List l : lists) {
			if (l.getName().equals("DONE"))
				finallist = l;
		}

		return finallist;
	}

	private static Card getCardByNameInList(String name, String idList) {
		Card card = null;

		trello.getBoard(BOARD_KEY);

		java.util.List<Card> cards = trello.getCardsByList(idList, (String[]) null);

		for (Card c : cards) {
			if (c.getName().equals(name))
				card = c;
		}

		return card;
	}
}
