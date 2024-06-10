package com.cw.models;

import com.cw.services.LogsService;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.memoria.Memoria;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Relatorio {

    Looca looca = new Looca();


    private long memoriaMaxima;
    Memoria memoria = looca.getMemoria();

    public Relatorio(Looca looca, Long memoriaMaxima) {
        this.looca = looca;
        this.memoriaMaxima = memoriaMaxima;
    }

    public Relatorio() {

    }

    public String verificarMemoria() {
        long memoriaEmUso = memoria.getEmUso();
        if (memoriaEmUso > memoriaMaxima) {
            try {
                gerarRelatorio();
                return "A memória ultrapassou o limite e um relatório foi gerado.";
            } catch (IOException e) {
                e.printStackTrace();
                return "Erro ao gerar o relatório.";
            }
        } else {
            return "A memória está dentro do limite.";
        }
    }

        public String gerarRelatorio() throws IOException {
            LocalDateTime dataAtual = LocalDateTime.now();
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            String relatorios = "C:\\Users\\Maria Eduarda\\Documentos\\Relatorios\\" + dataAtual.format(formatoData) + ".txt";

            try (FileWriter gravarArquivo = new FileWriter(relatorios)) {
                gravarArquivo.write("| Relatório de Uso de Memória |\n");
                gravarArquivo.write("A memória está acima do limite!!!\n");
                gravarArquivo.write("Data: " + dataAtual.format(formatoData) + "\n");
                gravarArquivo.write("Memória em uso: " + memoria.getEmUso() + " bytes\n");
                gravarArquivo.write("Memória disponível: " + memoria.getDisponivel() + " bytes\n");
                gravarArquivo.write("Memória total: " + memoria.getTotal() + " bytes\n");
            }
            return relatorios;
        }

        public void gerarLogRelatorios(){
            String mensagem = "Relatório criado em: " + DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            LogsService.gerarLog(mensagem);
        }

    public Long getMemoriaMaxima() {
        return memoriaMaxima;
    }

    public void setMemoriaMaxima(long memoriaMaxima) {
        this.memoriaMaxima = memoriaMaxima;
    }

}
