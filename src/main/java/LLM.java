import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.chat.OllamaChatMessage;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatRequestModel;
import io.github.ollama4j.utils.PromptBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LLM {
    private String host;
    private String promptFile;
    private PromptBuilder promptBuilder;
    OllamaAPI ollamaAPI;
    OllamaChatRequestBuilder builder;

    //Uses a locally running mistral model
    public LLM() {
        //default ollama port
        host = "http://localhost:11434/";
        ollamaAPI = new OllamaAPI(host);

        //5 min timeout. To decrease with hardware upgrades.
        ollamaAPI.setRequestTimeoutSeconds(300);

        //input specific model name here if not using mistral
        builder = OllamaChatRequestBuilder.getInstance("mistral");
    }

    private String buildPrompt() {
        InputStream inputStream = LLM.class.getResourceAsStream("/"+promptFile+".txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        promptBuilder = new PromptBuilder();
        try {
            String line = br.readLine();
            while (line != null) {
                if(!line.contains("*")){
                    promptBuilder.addLine(line);
                } else {
                    promptBuilder.addSeparator();
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return promptBuilder.build();
    }

    public OllamaChatRequestModel getPromptRequestModel(String fileName){
        // OptionsBuilder options = new OptionsBuilder().setTemperature(1.2F);
        promptFile = fileName;
        return builder.withMessage(OllamaChatMessageRole.USER, buildPrompt())
            //     .withOptions(options.build())
            .build();
    }

    public OllamaChatRequestModel getChatRequestModel(List<OllamaChatMessage> history, String input){
        return builder.withMessages(history).withMessage(OllamaChatMessageRole.USER, input).build();
    }

    public OllamaAPI getOllamaAPI() {
        return ollamaAPI;
    }
}
