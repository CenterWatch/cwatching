package com.cw.services;

import com.github.britooo.looca.api.core.Looca;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;

public class LogsService {
    private static final Integer QTD_MAX_LINHAS = 100;

    public static void gerarLog(String mensagem) {
        Path path = Paths.get("/cwatching/logs");
        File dir = new File(String.valueOf(path));

        mensagem += "\n";

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            } else {
                Integer qtdArquivos = dir.list().length == 0 ? qtdArquivos = 1 : dir.list().length;

                String nameFileAtual = "/%s_log_%s.log".formatted(new Looca().getRede().getParametros().getHostName(), String.format("%07d", qtdArquivos));
                String nameFileNovo = "/%s_log_%s.log".formatted(new Looca().getRede().getParametros().getHostName(), String.format("%07d", qtdArquivos+1));

                File file = new File(path + nameFileAtual);


                FileWriter log = new FileWriter(file, true);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime dtHora = LocalDateTime.now(ZoneId.of("UTC-3"));
                log.write(dtHora.format(formatter) + " " + mensagem);

                log.close();

                if (getQtdLinhas(file) >= QTD_MAX_LINHAS) {
                    file = new File(path + nameFileNovo);
                    file.createNewFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer getQtdLinhas(File file){
        Integer linhas = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.readLine() != null) linhas++;
            reader.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        return linhas;
    }
}