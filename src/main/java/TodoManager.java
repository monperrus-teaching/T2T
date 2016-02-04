import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

import org.trello4j.TrelloImpl;
public class TodoManager {
	TrelloImpl trello ;

	public static final String BASE_URL = "https://api.trello.com/1/";
	public static final String API_KEY = "fd0a2e1e1210542ec51d5f3e93d1f8a1";
	public static final String TOKEN = "1d332edec30a815dfbbff555c682cac01f54b3834ed70114f3d7bca5eaf280c7";
	public static String ID_BOARD ;

	public java.util.List<org.trello4j.model.List> trelloLists;

	public void init(){
		trello = new TrelloImpl(API_KEY, TOKEN);
		trelloLists = trello.getListByBoard(ID_BOARD);
		for (org.trello4j.model.List list : trelloLists) {
			System.out.println(list.getName());
		}
	}

	public void createCard(Map<String, String> params){
	}


	public static void main(String[] args) {
		TodoManager t2t = new TodoManager();
		String fileName;

		if (args.length < 2) {
			System.err.println("Missing argument, need to provide .t2t file.");
			System.exit(1);
		}

		ID_BOARD = args[1];
		fileName = args[0];

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}
		t2t.init();

		//t2t.testAPI(boardId);


	}
}
