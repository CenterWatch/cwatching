package com.cw.services;

import com.cw.dao.OciosidadeMouseDAO;
import com.cw.models.RegistroOciosidadeMouse;
import com.cw.models.Usuario;
import com.mysql.cj.log.Log;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class OciosidadeService extends TimerTask {
    private final Boolean DEBUG = true  ;

    private Integer tempoDecrescenteMs;
    private Integer tempoCrescenteMs;

    private Integer tempoDecrescente;

    private Integer sensibilidadeThreshold;

    private Boolean mouseMoveu;

    public Timer monitorarMouse;

    OciosidadeMouseDAO ociosidadeMouseDAO = new OciosidadeMouseDAO();

    private Usuario usuario;
    private RegistroOciosidadeMouse registroOciosidadeMouse;

    public Boolean isRunning;

    public OciosidadeService(Usuario u, Integer t, Integer s) {
        isRunning = true;
        tempoCrescenteMs = 0;
        usuario = u;
        tempoDecrescenteMs = t;
        sensibilidadeThreshold = s;
        monitorarMouse = new Timer();
        monitorarMouse.schedule(monitorarMouseTask, 0 , 100);
    }

    @Override
    public void run() {
        try {
            timerDesc(); // Inicia a contagem regressiva
            timerCresc();

            if (DEBUG) System.out.println("Inserido %d segundos".formatted(tempoCrescenteMs));

        } catch (Exception e) {
        }
    }

    private TimerTask monitorarMouseTask = new TimerTask() {
        // Busca coordenadas do mouse em dois intervalos de tempo
        @Override
        public void run() {
            try {
                Point coordAnterior = getCoordenadaMouse();
                Thread.sleep(300);
                Point coordAtual = getCoordenadaMouse();

                // Cálculo para checar se mouse moveu dentro de uma threshold estabelecida com base em suas coordenadas
                int intervaloXMenor = coordAtual.x - sensibilidadeThreshold;
                int intervaloXMaior = coordAtual.x + sensibilidadeThreshold;
                int intervaloYMenor = coordAtual.y - sensibilidadeThreshold;
                int intervaloYMaior = coordAtual.y + sensibilidadeThreshold;

                mouseMoveu = (
                        (coordAnterior.x < intervaloXMenor) ||
                                (coordAnterior.x > intervaloXMaior) ||
                                (coordAnterior.y < intervaloYMenor) ||
                                (coordAnterior.y > intervaloYMaior)
                );

                if (mouseMoveu) tempoDecrescente = tempoDecrescenteMs; // Reseta a contagem regressiva caso o mouse mova
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    };

    public Point getCoordenadaMouse() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    private void timerCresc() throws InterruptedException {
        tempoCrescenteMs += 100;
        Thread.sleep(100);

        if (DEBUG && tempoCrescenteMs % 1000 == 0) System.out.println("Ocioso: " + tempoCrescenteMs);
        if (mouseMoveu)  {
            registroOciosidadeMouse.setTempoRegistroMs(tempoCrescenteMs);
            ociosidadeMouseDAO.updateOciosidadeMouse(registroOciosidadeMouse);
            tempoCrescenteMs = 0;

            return;
        };

        if (isRunning) timerCresc();
    }

    private void timerDesc() throws InterruptedException {
        tempoDecrescente -= 100;
        Thread.sleep(100);

        if (DEBUG && tempoDecrescente % 1000 == 0) System.out.println("Falta para ocioso: " + tempoDecrescente);

        if (tempoDecrescente <= 0) {
            RegistroOciosidadeMouse r = new RegistroOciosidadeMouse(tempoCrescenteMs, usuario.getIdUsuario());
            r.setIdTempoOciosidade(ociosidadeMouseDAO.inserirOciosidadeMouse(r));
            this.registroOciosidadeMouse = r;
            return;
        }

        if (isRunning) timerDesc();
    }
}
