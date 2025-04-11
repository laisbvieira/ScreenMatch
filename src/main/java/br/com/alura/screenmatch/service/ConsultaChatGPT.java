package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;

public class ConsultaChatGPT {


    private static final Dotenv dotenv = Dotenv.load();
//    private static final String TOKEN = dotenv.get("TOKEN_OPENAI");
//    private static final OpenAiService service = new OpenAiService(TOKEN);


//    public static String obterTraducao(String texto) {
//
//        CompletionRequest requisicao = CompletionRequest.builder()
//                .model("gpt-3.5-turbo-instruct")
//                .prompt("traduza para o português o texto: " + texto)
//                .maxTokens(1000)
//                .temperature(0.7)
//                .build();
//
//        var resposta = service.createCompletion(requisicao);
//        return resposta.getChoices().get(0).getText();
    }
//}
