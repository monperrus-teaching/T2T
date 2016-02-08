import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.trello4j.TrelloImpl;
import org.trello4j.model.Card;
import org.trello4j.model.Member;

public class T2T {

	final static String API_KEY = "1ae06ad5bee3715dbcfabb71342c54ed";

	final static String BOARD_KEY = "56af6582ccde1bf9a1bfd85a";

	final static String API_TOKEN = "ed4b95898da90220abc1c6e86e29e7b3d6b8a22bf5c759954a7cad3d678b5bfb";

	final static TrelloImpl trello = new TrelloImpl(API_KEY, API_TOKEN);

	public static void main(String[] args) throws IOException {
		initBoard();
	}

	public static void initBoard() throws IOException {
		System.out.println();
		Map<String, String> listKeyValueMap = new HashMap<String, String>();

		org.trello4j.model.List todolist;
		org.trello4j.model.List donelist;

		if (getTODOList() == null) {
			listKeyValueMap.put("pos", "top");
			todolist = trello.createList(BOARD_KEY, "TODO", listKeyValueMap);
		} else
			todolist = getTODOList();

		if (getDONEList() == null) {
			listKeyValueMap.put("pos", "bottom");
			donelist = trello.createList(BOARD_KEY, "DONE", listKeyValueMap);
		} else
			donelist = getDONEList();

		for (String line : Files.readAllLines(Paths.get("toDo2TrelloNewTasks.t2t"))) {
			// name (0), task desc. (1), date (2), class (3), method (4)
			String[] parts = line.split(";");

			Map<String, String> cardKeyValueMap = new HashMap<String, String>();
			cardKeyValueMap.put("due", "Due " + parts[2]);
			cardKeyValueMap.put("desc", parts[1]);
			Member dev = trello.getMember(parts[0].toLowerCase().replaceAll("\\s+", ""), (String[]) null);
			cardKeyValueMap.put("idMembers", dev.getId());

			trello.createCard(todolist.getId(), parts[3] + ":" + parts[4], cardKeyValueMap);
		}

		for (String line : Files.readAllLines(Paths.get("toDo2TrelloDone.t2t"))) {
			// name (0), task desc. (1), date (2), class (3), method (4)
			String[] parts = line.split(";");

			Card formerCard = getCardByNameInList(parts[3] + ":" + parts[4], todolist.getId());

			formerCard = trello.updateCard(formerCard.getId(), donelist.getId(), listKeyValueMap);
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
