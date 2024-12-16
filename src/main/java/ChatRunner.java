import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.impl.ConsoleOutputStreamHandler;
import io.github.ollama4j.models.chat.OllamaChatMessage;
import io.github.ollama4j.models.chat.OllamaChatRequestModel;
import io.github.ollama4j.models.chat.OllamaChatResult;
import io.github.ollama4j.models.generate.OllamaStreamHandler;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ChatRunner {
    private LLM llm = new LLM();
    private Scanner in = new Scanner(System.in);
    private List<OllamaChatMessage> history;
    String input = "";

    public static void main(String[] args) throws IOException, InterruptedException, OllamaBaseException {
        ChatRunner chatRunner = new ChatRunner();
        chatRunner.initializeLLM();
        chatRunner.runChat();

        System.out.println("Save last conversation?");
        if(chatRunner.in.nextLine().equalsIgnoreCase("yes")){
            chatRunner.saveChat();
        }
    }

    private void saveChat() throws IOException {
        Logger logger = Logger.getLogger("ChatHistoryLogger");
        FileHandler fileHandler = new FileHandler("logs/ChatLog.log");
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
        fileHandler.setFormatter(new SimpleFormatter());
        for(OllamaChatMessage line : history) {
            logger.info(line.getContent());
        }
    }

    private void runChat() throws OllamaBaseException, IOException, InterruptedException {
        System.out.println("Chat Started:");
        input = in.nextLine();

        // create next userQuestion
        while(!input.equals("end")) {
            OllamaStreamHandler streamHandler = new ConsoleOutputStreamHandler();
            OllamaChatRequestModel requestModel = llm.getChatRequestModel(history, input);
            OllamaChatResult chatResult = llm.getOllamaAPI().chat(requestModel, streamHandler);
            history = chatResult.getChatHistory();
            System.out.println();
            input = in.nextLine();
        }
    }

    // create llm with prompt
    private void initializeLLM(){
        System.out.println();
        System.out.println("Input prompt file name, excluding extension type");
        String fileName = in.nextLine();
        System.out.println();
        history = llm.getPromptRequestModel(fileName).getMessages();
    }
}
