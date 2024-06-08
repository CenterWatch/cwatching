package com.cw.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Timer;

import com.cw.models.Ocorrencia;
import com.github.britooo.looca.api.core.Looca;
import org.json.JSONObject;

public class SlackService {
    private static HttpClient client = HttpClient.newHttpClient();
    private static final String URL = ""; // Coloque aqui seu Webhook do Slack [APAGAR ANTES DE DAR COMMIT]

    private static Timeout timeout;

    public SlackService() {
        SlackService.timeout = new Timeout();
        new Timer().schedule(timeout, 0, 100);
    }

    private static void postar(JSONObject content) {
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(URL)
                ).header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(content.toString()))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(String.format("Status: %s", response.statusCode()));
            System.out.println(String.format("Response: %s", response.body()));

        } catch (InterruptedException | IOException e) {
            // Sout;
            LogsService.gerarLog("Erro ao enviar mensagem para o Slack: " + e.getMessage());
        }
    }

    public void postarOcorrencia(Ocorrencia o) {
        String content = """
                %s
                %s
                %s
                Máquina: %s
                """.formatted(o.getTitulo(), o.getDescricao(), o.getTipo(), new Looca().getRede().getParametros().getHostName());

        if (!timeout.getRunning()) {
            postar(new JSONObject().put("text", content));
            timeout.setRunning(true);
        }
    }
}
