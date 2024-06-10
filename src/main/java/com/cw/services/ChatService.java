package com.cw.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;


public class ChatService {
    private static String KEY = "";
    private static String PROMPT = " Essa lista é uma lista de processos que está em execução na máquina de um dos meus funcionários, preciso que você liste o nome dos processos que são indevidos no contexto de trabalho, ou seja, processos que representa um jogo ou processo malicioso. Preciso que você faça essa lista separando os nomes dos processos com , sem espaço entre a virgula e os processos. Importante: Não digite mais nada além dos processos, pois estou usando sua resposta na minha aplicação, e só preciso dos processos.";
    private static long MAX_TOKENS = 100;
    private static float TEMPERATURE = 1;
    private static String MODEL = "gpt-3.5-turbo";

    public String verificarProcesso(String process){
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");

            StringEntity entity = new StringEntity(
                    "{" +
                            "\"model\": \"" + MODEL + "\"," +
                            "\"messages\": [{\"role\": \"system\", \"content\": \"" + process + PROMPT +"\"}]," +
                            "\"max_tokens\": " + MAX_TOKENS + "," +
                            "\"temperature\": " + TEMPERATURE +
                            "}"
            );

            entity.setContentType("application/json");
            post.setEntity(entity);
            post.setHeader("Authorization", "Bearer " + KEY);

            HttpResponse response = null;
            do {
                if (response != null)
                    Thread.sleep(1000); // Espera 1 segundo antes de fazer a próxima tentativa
                response = client.execute(post);

                if (response.getStatusLine().getStatusCode() == 429)
                    System.out.println("Rate limit exceeded, waiting before retrying...");

            } while (response.getStatusLine().getStatusCode() == 429);

            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("HTTP Error: " + response.getStatusLine().getStatusCode());
                return null;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder responseContent = new StringBuilder();
            String output;
            while ((output = reader.readLine()) != null) {
                responseContent.append(output);
            }

            JSONObject jsonResponse = new JSONObject(responseContent.toString());
            JSONArray choices = jsonResponse.optJSONArray("choices");
            if (choices == null) {
                System.out.println("Choices not found in the response.");
                return null;
            }

            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            String gptResponse = message.getString("content");

            return gptResponse;

        } catch (Exception exception) {
            return null;
        }
    }
}
