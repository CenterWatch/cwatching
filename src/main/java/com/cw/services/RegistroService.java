package com.cw.services;

import com.cw.dao.ProcessoDAO;
import com.cw.dao.RegistroDAO;
import com.cw.models.Empresa;
import com.cw.models.Processo;
import com.cw.models.Registro;
import com.cw.models.Sessao;
import com.github.britooo.looca.api.core.Looca;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class RegistroService extends TimerTask {
    private AlertaService alertaService;

    private Looca looca = new Looca();
    private SystemInfo oshi = new SystemInfo();

    private RegistroDAO registroDAO = new RegistroDAO();
    private ProcessoDAO processoDAO = new ProcessoDAO();
    private Empresa empresa;

    private Boolean registrarProcessos = true;

    public RegistroService(Empresa empresa, AlertaService alertaService) {
        this.alertaService = alertaService;
        this.empresa = empresa;
    }

    public void run() {

        try {
            Registro registro = new Registro(
                    looca.getProcessador().getUso()*10 > 100 ? 100.0 : looca.getProcessador().getUso()*10,
                    looca.getMemoria().getEmUso(),
                    looca.getMemoria().getDisponivel(),
                    looca.getSistema().getTempoDeAtividade());

            registroDAO.inserirRegistro(registro, empresa);

            Registro r = registroDAO.buscarUltimoRegistroPorSessao(empresa);

            if (alertaService.verificarAlerta(r)) registrarProcessos(r);
        } catch (Exception e) {
            System.out.println("Não foi possivel inserir o registro: " + e.getMessage() + Arrays.toString(e.getStackTrace()));
            LogsService.gerarLog("Falhou ao inserir um registro de componentes: " + e.getMessage());
        }
    }

    public void registrarProcessos(Registro r) {
        if (!alertaService.getRegistrarProcessos()) return;

        for (OSProcess processo : oshi.getOperatingSystem().getProcesses()) {
            if ((!processo.getPath().contains("C:\\Windows\\System32\\") && !processo.getPath().isEmpty())) {
                Processo p = new Processo(
                        processo.getName(),
                        processo.getPath(),
                        processo.getResidentSetSize(),
                        r.getIdRegistro());

                processoDAO.inserirProcesso(p);
            }
        }

        alertaService.setRegistrarProcessos(false);

        // Reinicializando timeout para inserir novamente os processos
        // TODO: parametrizar o timer
        new Timer().schedule(new TimerProcessosService(alertaService), 15000);

        System.out.println("Registrando processos...\n");
    }
}

