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

        for (String processo : processos) {
            if (permProcessos.get(processo) == null) {
                String path = getProcessoPath(processo);
                if (!path.isEmpty() && !path.contains("C:\\Windows\\System32")) permProcessoDAO.inserirPermProcesso(processo, path, config);
            } else {
                PermProcesso p = gson.fromJson(gson.toJson(permProcessos.get(processo)), PermProcesso.class);

                if (p.getPermitido() != null && !p.getPermitido()) finalizarProcesso(processo);
            }
        }
    }

    private List<String> filtrarProcessoNome(List<Processo> processos) {
        List<String> p = new ArrayList<>();

        for (Processo processo : processos) {
            p.add(processo.getNome());
        }

        return p;
    }

    private void finalizarProcesso(String nome){
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + nome + ".exe" );
            LogsService.gerarLog("Processo finalizado: " + nome);
        } catch (IOException e) {
            LogsService.gerarLog("Falhou em finalizar um processo: " + e.getMessage());
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
