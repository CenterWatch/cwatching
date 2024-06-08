package com.cw.services;

import com.cw.dao.AlertaDAO;
import com.cw.dao.OcorrenciaDAO;
import com.cw.models.*;
import com.github.britooo.looca.api.core.Looca;

import java.util.List;

public class AlertaService {
    private Config config;
    private Boolean registrarProcessos;
    private AlertaDAO alertaDAO;
    private OcorrenciaDAO ocorrenciaDAO;
    private Integer decrementoOcorrencia;
    private SlackService slackVol;
    private SlackService slackCpu;
    private SlackService slackRam;

    private final Integer TEMPO_OCORRENCIA = 5;

    Looca looca = new Looca();

    public AlertaService(Config config) {
        this.config = config;
        this.registrarProcessos = true;
        alertaDAO = new AlertaDAO();
        ocorrenciaDAO = new OcorrenciaDAO();
        this.slackVol = new SlackService();
        this.slackCpu = new SlackService();
        this.slackRam = new SlackService();
        decrementoOcorrencia = TEMPO_OCORRENCIA;
    }

    public Boolean verificarAlerta(Registro r) {
        Double ramPer = ConversorService.converterPorcentagem(looca.getMemoria().getTotal(), r.getUsoRam());
        Double cpuPer = r.getUsoCpu();

        Boolean emAlerta = (ramPer > config.getMaxRam()) || (cpuPer > config.getMaxCpu());

        if (emAlerta) decrementoOcorrencia -= 1;

        if (ramPer > config.getMaxRam()) {
            alertaDAO.inserirAlerta(new Alerta("RAM", "%.1f%%".formatted(ramPer), r.getIdRegistro(), null));

            if (decrementoOcorrencia <= 0) {
                Ocorrencia o = new Ocorrencia(
                        "Alerta Uso de RAM",
                        "A RAM permaneceu acima de %.1f%% por %d segundos.".formatted(config.getMaxRam(), TEMPO_OCORRENCIA * config.getIntervaloRegistroMs() / 1000),
                        "[SISTEMA] RAM",
                        r.getFkSessao()
                );

                ocorrenciaDAO.inserirOcorrencia(o);
                slackRam.postarOcorrencia(o);

                decrementoOcorrencia = TEMPO_OCORRENCIA;
            }
        };

        if (cpuPer > config.getMaxCpu()) {
            alertaDAO.inserirAlerta(new Alerta("CPU", "%.1f%%".formatted(cpuPer), r.getIdRegistro(), null));

            if (decrementoOcorrencia <= 0) {
                Ocorrencia o = new Ocorrencia(
                        "Alerta Uso de CPU",
                        "A CPU permaneceu acima de %.1f%% por %d segundos.".formatted(config.getMaxCpu(), TEMPO_OCORRENCIA * 2),
                        "[SISTEMA] CPU",
                        r.getFkSessao()
                );

                ocorrenciaDAO.inserirOcorrencia(o);
                slackCpu.postarOcorrencia(o);

                decrementoOcorrencia = TEMPO_OCORRENCIA;
            }
        };

        return emAlerta;
    }

    public Boolean verificarAlerta(RegistroVolume r, Long total) {
        Double perVolume = ConversorService.converterPorcentagem(total, r.getVolumeDisponivel());
        Boolean emAlerta = perVolume < (100.0 - config.getMaxVolume());

        if (emAlerta) alertaDAO.inserirAlerta(new Alerta("Volume", "%.1f%%".formatted(perVolume), null, r.getIdRegistroVolume()));

        return emAlerta;
    }

    public void listarProcessosEmAlerta(List<Processo> processos, Registro r) {
        System.out.println("----------------------------");
        System.out.println("Processos %s".formatted(r.getDtHora()));
        System.out.println("----------------------------");

        for (Processo processo : processos) {
            System.out.println(processo);
        }
    }

    public Boolean getRegistrarProcessos() {
        return registrarProcessos;
    }

    public void setRegistrarProcessos(Boolean registrarProcessos) {
        this.registrarProcessos = registrarProcessos;
    }
}
