
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.trello4j.TrelloImpl;

public class MainClass {
	final static String API_KEY = "1ae06ad5bee3715dbcfabb71342c54ed";

	final static String API_TOKEN = "ed4b95898da90220abc1c6e86e29e7b3d6b8a22bf5c759954a7cad3d678b5bfb";

	public static void main(String[] args) throws IOException {
		List<String> classList = new ArrayList<String>();
		Map<String, String> idtolist = new HashMap<String, String>();

		TrelloImpl trello = new TrelloImpl(API_KEY, API_TOKEN);

		for (String line : Files.readAllLines(Paths.get("toDo2TrelloParsed.t2t"))) {
			String[] parts = line.split(";");
			org.trello4j.model.List todolist;

			if (!classList.contains(parts[3])) {
				classList.add(parts[3]);

				Map<String, String> listKeyValueMap = new HashMap<String, String>();
				listKeyValueMap.put("pos", "bottom");
				todolist = trello.createList("56af6582ccde1bf9a1bfd85a", parts[3], listKeyValueMap);
				idtolist.put(parts[3], todolist.getId());
			}

			else {
				todolist = trello.getList(idtolist.get(parts[3]));
			}

			Map<String, String> listKeyValueMap = new HashMap<String, String>();

			listKeyValueMap.put("desc", "\"" + parts[0] + "\" by " + parts[1] + " for " + parts[2]);

			trello.createCard(todolist.getId(), parts[1], listKeyValueMap);
		}

	}
}
