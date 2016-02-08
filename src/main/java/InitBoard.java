import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.trello4j.TrelloImpl;
import org.trello4j.model.Member;

public class InitBoard {

	final static String API_KEY = "1ae06ad5bee3715dbcfabb71342c54ed";

	final static String BOARD_KEY = "56af6582ccde1bf9a1bfd85a";

	final static String API_TOKEN = "ed4b95898da90220abc1c6e86e29e7b3d6b8a22bf5c759954a7cad3d678b5bfb";

	public static void main(String[] args) throws IOException {
		initBoard();
	}

	public static void initBoard() throws IOException {
		System.out.println();
		Map<String, String> listKeyValueMap = new HashMap<String, String>();

		TrelloImpl trello = new TrelloImpl(API_KEY, API_TOKEN);

		// Creating the list, on top of the Trello board
		listKeyValueMap.put("pos", "top");
		org.trello4j.model.List todolist = trello.createList(BOARD_KEY, "TODO", listKeyValueMap);
		listKeyValueMap.put("pos", "bottom");
		org.trello4j.model.List donelist = trello.createList(BOARD_KEY, "DONE", listKeyValueMap);

		for (String line : Files.readAllLines(Paths.get("toDo2TrelloParsed.t2t"))) {
			// name (0), task desc. (1), date (2), class (3), method (4)
			String[] parts = line.split(";");

			Map<String, String> cardKeyValueMap = new HashMap<String, String>();
			cardKeyValueMap.put("due", "Due " + parts[2]);
			cardKeyValueMap.put("desc", parts[1]);
			Member dev = trello.getMember(parts[0].toLowerCase().replaceAll("\\s+", ""), (String[]) null);
			cardKeyValueMap.put("idMembers", dev.getId());

			trello.createCard(todolist.getId(), parts[3] + ":" + parts[4], cardKeyValueMap);
		}
	}
}
