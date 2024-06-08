package com.cw.conexao;

import com.cw.dao.SessaoDAO;
import com.cw.models.Usuario;
import com.cw.services.LoginService;
import com.cw.services.OciosidadeService;
import com.google.gson.Gson;
import com.mysql.cj.log.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class Node {
    public static final Integer PORTA = 12761;

    public static Usuario autenticar() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Aguardando login...");
            Socket socket = serverSocket.accept();

            Scanner data = new Scanner(socket.getInputStream());
            String json = (String) data.nextLine();
            System.out.println(json);

            return new Gson().fromJson(json, Usuario.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static void listenLogout(Integer idSessao) {
        
        Runnable logoutListener = () -> {
            try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
                Socket socket = serverSocket.accept();
                new Scanner(socket.getInputStream());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            new SessaoDAO().updateFimSessao(idSessao);
            LoginService.atualizarRegistro.cancel();
            LoginService.monitorarProcesso.cancel();
            LoginService.atualizarVolume.cancel();
            LoginService.monitorarOciosidade.cancel();
            LoginService.ociosidadeService.isRunning = false;
            LoginService.logar(true, new Usuario(), true);
        };

        new Thread(logoutListener).start();
    }

    public static String getPublicIp() throws IOException {
        String urlString = "http://checkip.amazonaws.com/";
        URL url = new URL(urlString);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.readLine();
        }
    }
}
