package com.cw.services;

import com.cw.dao.RegistroVolumeDAO;
import com.cw.models.Empresa;
import com.cw.models.RegistroVolume;
import com.cw.models.Sessao;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Volume;

import java.util.List;
import java.util.TimerTask;

public class RegistroVolumeService extends TimerTask {
    private AlertaService alertaService;
    private Empresa empresa;
    private Looca looca;
    private RegistroVolumeDAO registroVolumeDAO;

    public RegistroVolumeService(Empresa empresa, AlertaService alertaService) {
        this.alertaService = alertaService;
        this.empresa = empresa;
        this.looca = new Looca();
        this.registroVolumeDAO = new RegistroVolumeDAO();
    }

    public void run() {
        List<Volume> volumeAtual = looca.getGrupoDeDiscos().getVolumes();

        try {
            for (Volume v : volumeAtual) {
                RegistroVolume registroVolume = new RegistroVolume(v.getDisponivel(), v.getUUID());
                registroVolumeDAO.inserirRegistroVolume(registroVolume, empresa);

                alertaService.verificarAlerta(registroVolumeDAO.buscarUltimoRegVolumePorUUID(v.getUUID()), v.getTotal());
            }

        } catch (Exception e) {
            System.out.println();
            //console Sout
            LogsService.gerarLog("falhou ao inserir um registro de volume: " + e.getMessage());
        }
    }
}
