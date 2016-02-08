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

	static String BOARD_KEY;

	static String USER_TOKEN;

	static TrelloImpl trello;

	static org.trello4j.model.List todolist;
	static org.trello4j.model.List donelist;

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Mauvais usage, vous devez fournir un token utilisateur (1)\n et l'ID du board\n Token user (2):\n\n"
					+ "https://trello.com/1/authorize?key=1ae06ad5bee3715dbcfabb71342c54ed&name=T2T&expiration=never&response_type=token&scope=read,write");
			System.exit(1);
		}
		USER_TOKEN = args[0];
		BOARD_KEY = args[1];

		trello = new TrelloImpl(API_KEY, USER_TOKEN);

		try {
			init();
			process();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void process() {
		Map<String, String> listKeyValueMap = new HashMap<String, String>();

		try {
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

				formerCard = trello.updateCard(formerCard.getId(), donelist.getId(), null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void init() throws IOException {
		Map<String, String> listKeyValueMap = new HashMap<String, String>();

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
