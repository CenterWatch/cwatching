package com.cw.services;

import com.cw.dao.RegistroFuncionarioDAO;
import com.cw.dao.RegistroVolumeDAO;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class RegistroFuncionario extends TimerTask {

    private Integer idRegistro;
    private Date inicioSessao;
    private Date fimSessao;
    private Integer tempo_osiosidade; // em segundos
    private Integer apontamento; // de 0 a 10
    private Integer tarefasTotal;
    private Integer tarefasConcluidas;

    private RegistroFuncionarioDAO registroFuncionarioDAO = new RegistroFuncionarioDAO();


    public RegistroFuncionario(Integer idRegistro, Date inicioSessao, Date fimSessao, Integer tempo_osiosidade, Integer apontamento, Integer tarefasTotal, Integer tarefasConcluidas) {
        this.idRegistro = idRegistro;
        this.inicioSessao = inicioSessao;
        this.fimSessao = fimSessao;
        this.tempo_osiosidade = tempo_osiosidade;
        this.apontamento = apontamento;
        this.tarefasTotal = tarefasTotal;
        this.tarefasConcluidas = tarefasConcluidas;
    }

    public RegistroFuncionario() {
    }

    @Override
    public void run() {
        List<RegistroFuncionario> registroFuncionarios = registroFuncionarioDAO.getRegistroFuncionario();
        System.out.println(registroFuncionarios);
    }

    private double calcularPorcentagemTarefas() {
        return ((double) tarefasConcluidas / tarefasTotal) * 100;
    } // % das tarefas concluidas

    private double calcularPorcentagemOsiosidade() {
        double porcentagemOsiosidade = (10000 - tempo_osiosidade) / 100.0; // Porcentagem de ociosidade invertida
        if (porcentagemOsiosidade < 0) {
            porcentagemOsiosidade = 0; // Limitando a 0% se for negativo
        }
        return porcentagemOsiosidade;
    } // % do tempo que o funcionário ficou sem mexer o mouse, 10.000 segundos equivalem a 0% para o desempenho no cálculo de OEE

    private double calcularOEE(double porcentagemTarefas, double porcentagemApontamento, double porcentagemOsiosidade) {
        double pesoTarefas = 0.4; // 40%
        double pesoApontamento = 0.3; // 30%
        double pesoTempoOsiosidade = 0.3; // 30%

        double oee = (porcentagemTarefas * pesoTarefas) + (porcentagemApontamento * pesoApontamento) + (porcentagemOsiosidade * pesoTempoOsiosidade);

        return oee;
    } // % calculo OEE

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Date getInicioSessao() {
        return inicioSessao;
    }

    public void setInicioSessao(Date inicioSessao) {
        this.inicioSessao = inicioSessao;
    }

    public Date getFimSessao() {
        return fimSessao;
    }

    public void setFimSessao(Date fimSessao) {
        this.fimSessao = fimSessao;
    }

    public Integer getTempo_osiosidade() {
        return tempo_osiosidade;
    }

    public void setTempo_osiosidade(Integer tempo_osiosidade) {
        this.tempo_osiosidade = tempo_osiosidade;
    }

    public Integer getApontamento() {
        return apontamento;
    }

    public void setApontamento(Integer apontamento) {
        this.apontamento = apontamento;
    }

    public Integer getTarefasTotal() {
        return tarefasTotal;
    }

    public void setTarefasTotal(Integer tarefasTotal) {
        this.tarefasTotal = tarefasTotal;
    }

    public Integer getTarefasConcluidas() {
        return tarefasConcluidas;
    }

    public void setTarefasConcluidas(Integer tarefasConcluidas) {
        this.tarefasConcluidas = tarefasConcluidas;
    }

    @Override
    public String toString() {
        double porcentagemTarefas = calcularPorcentagemTarefas();
        double porcentagemApontamento = apontamento;
        double porcentagemOsiosidade = calcularPorcentagemOsiosidade();
        double oee = calcularOEE(porcentagemTarefas, porcentagemApontamento, porcentagemOsiosidade);

        return String.format("""
                        +-----------------------------------------------+
                        |                                                |
                        |    ID do Registro: %d                         |
                        |    OEE: %.2f%%                               |
                        |    Porcentagem de Tarefas Concluídas: %.2f%% |
                        |    Porcentagem de Apontamento: %.2f%%        |
                        |    Porcentagem de Osiosidade: %.2f%%         |
                        |                                                |
                        +-----------------------------------------------+
                        """,
                idRegistro, oee, porcentagemTarefas, porcentagemApontamento, porcentagemOsiosidade);
    }
}
