package com.cw.services;

import com.cw.dao.PermProcessoDAO;
import com.cw.models.Config;
import com.cw.models.PermProcesso;
import com.cw.models.Usuario;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processos.Processo;
import com.google.gson.Gson;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;

import java.io.IOException;
import java.util.*;

public class ProcessoService extends TimerTask {
    private Config config;
    private Map<String, Object> permProcessos;
    private PermProcessoDAO permProcessoDAO;
    private Looca looca;
    private SystemInfo oshi;
    private Set<String> processos;
    Gson gson = new Gson();

    private StringBuilder prompt = new StringBuilder();

    public ProcessoService(Config config) {
        this.config = config;
        this.permProcessoDAO = new PermProcessoDAO();
        looca = new Looca();
        oshi = new SystemInfo();
    }

    @Override
    public void run() {
        this.permProcessos = listToMap(permProcessoDAO.buscarProcessos(this.config));
        this.processos = new HashSet<>(filtrarProcessoNome(looca.getGrupoDeProcessos().getProcessos()));

        int i = 0;
        for (String processo : processos) {
            if (prompt.length() > 0 && i <= processos.size()) {
                prompt.append(",");
            }
            prompt.append(processo);
            i++;
        }
        ChatService chat = new ChatService();
        String res = chat.verificarProcesso(prompt.toString());
        transformResChatToList(res);
    }

    private List<String> filtrarProcessoNome(List<Processo> processos) {
        List<String> p = new ArrayList<>();

        for (Processo processo : processos) {
            p.add(processo.getNome());
        }

        return p;
    }

    private void transformResChatToList(String res){
        String[] resArray = res.split(",");
        finalizarProcesso(resArray);
    }

    private void finalizarProcesso(String[] res){
        for (String processo : res){
            try {
                Runtime.getRuntime().exec("taskkill /F /IM " + processo + ".exe" );
                LogsService.gerarLog("Processo finalizado: " + processo);
            } catch (IOException e) {
                LogsService.gerarLog("Falhou em finalizar um processo: " + e.getMessage());
            }
        }
    }

    private Map<String, Object> listToMap(List<Map<String, Object>> l) {
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < l.size(); i++) {
            map.put((String) l.get(i).get("nome"), l.get(i));
        }

        return map;
    }

    private String getProcessoPath(String nome) {
        for (OSProcess p : oshi.getOperatingSystem().getProcesses()) {
            if (p.getName().equals(nome)) return p.getPath();
        }

        return "";
    }
}
