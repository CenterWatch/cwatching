package com.cw.services;

import com.cw.models.Alerta;
import com.cw.models.Registro;
import com.github.britooo.looca.api.core.Looca;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import oshi.util.FileUtil;

import static com.cw.services.LogsService.gerarLog;

public class LimparVolume {
    private Alerta alerta;

    Looca looca = new Looca();
    Registro registroVolume = new Registro();
    /*Path pastaTemp = Paths.get("C:\\Users\\Alfa User\\AppData\\Local\\Temp");*/
    /*Path pastaTemp = Paths.get("C:\\Users\\Alfa User\\Documents\\testeLimpar");*/
    Path pastaTemp = !looca.getSistema().getSistemaOperacional().contains("Linux") ? Paths.get("C:\\Users\\Alfa User\\Documents\\testeLimpar") : Paths.get("/var/tmp");

    public void iniciarLimpezaVolume() {
        new Thread(threadLimparTemp).start();
    }

    private Runnable threadLimparTemp = new Runnable() {

        public void run() {

            try {

                Thread.sleep(5000);
                //if (alerta.verificarAlerta(registroVolume))
                if (true) {
                    while (true) {

                        long tamanhoPasta = FileUtils.sizeOfDirectory(pastaTemp.toFile());
                        long tamanhoTotalDisco = looca.getGrupoDeDiscos().getTamanhoTotal();

                        Double tamanhoPastaDouble= (double) tamanhoPasta;

                        tamanhoPasta = tamanhoPasta / (1024 * 1024 * 1024);
                        Double tamanhoPastaPorcentagemDouble = ( tamanhoPastaDouble * 100) / tamanhoTotalDisco;
                        Files.walkFileTree(pastaTemp, new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                System.out.println("Fechando arquivo: " + file.getFileName());
                                Files.delete(file);
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult postVisitDirectory(Path pasta, IOException exc) throws IOException {
                                if (!pasta.equals(pastaTemp)) {
                                    System.out.println("Fechando pasta: " + pasta.getFileName());
                                    Files.delete(pasta);

                                }
                                return FileVisitResult.CONTINUE;
                            }
                        });
                        System.out.println("""
                                ---------------------------------------------
                                Foi liberado: %.2f Gigas de memória de disco
                                ou %.2f%% da memória total do disco.                                
                                ---------------------------------------------
                                """.formatted(tamanhoPastaDouble, tamanhoPastaPorcentagemDouble));

                        Thread.sleep(10000);
                    }
                }
            } catch (Exception e) {
                gerarLog("Não foi possivel executar a limpeza " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            }

        }
    };
}






