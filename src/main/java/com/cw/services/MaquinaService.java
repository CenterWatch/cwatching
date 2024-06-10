package com.cw.services;

import com.cw.conexao.Node;
import com.cw.dao.MaquinaDAO;
import com.cw.dao.VolumeDAO;
import com.cw.models.Empresa;
import com.cw.models.Maquina;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Volume;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MaquinaService {
    private Looca looca = new Looca();
    private MaquinaDAO maquinaDAO = new MaquinaDAO();
    private VolumeDAO volumeDAO = new VolumeDAO();

    private Maquina maquinaAtual;

    public MaquinaService() {
        this.maquinaAtual = new Maquina(
                looca.getSistema().getSistemaOperacional(),
                looca.getProcessador().getNome(),
                looca.getMemoria().getTotal(),
                looca.getRede().getParametros().getHostName()
        );
    }

    public void registrarMaquinaSeNaoExiste(Empresa empresa) {
        Boolean maquinaExiste = maquinaDAO.verificarMaquinaExistePorHostnameEEmpresa(maquinaAtual.getHostname(), empresa);
        Boolean componentesAlterados = maquinaDAO.componentesAlterou(maquinaAtual);

        System.out.println("\nVerificado cadastro da máquina...");

        if (!maquinaExiste) {
            System.out.println("\nMáquina não encontrada. Cadastrando máquina...");

            this.maquinaAtual.setFkEmpresa(empresa.getIdEmpresa());

            try {
                this.maquinaAtual.setIp(Node.getPublicIp());
            } catch (IOException e) {
                System.out.println("Falha ao obter IP Público: " + e.getMessage());
            }

            maquinaDAO.inserirMaquina(maquinaAtual, empresa);
            registrarGrupoVolumePorMaquina(empresa);
        } else {
            System.out.println("\nMáquina encontrada.");
        }

        if (componentesAlterados && maquinaExiste) {
            System.out.println("\nDetectada alteração nos componentes. Atualizando dados...");
            maquinaDAO.atualizarMaquina(maquinaAtual, empresa);
        }

        atualizarGrupoVolumeExistente(empresa);
    }

    private void atualizarGrupoVolumeExistente(Empresa e) {
        List<Volume> volumes = looca.getGrupoDeDiscos().getVolumes();

        for (Volume v : volumes) {
            Map<String, Object> mapVolume;

            mapVolume = volumeDAO.volumeAlterou(new com.cw.models.Volume(v.getUUID(), v.getNome(), v.getPontoDeMontagem(), v.getTotal()));

            com.cw.models.Volume volumeAtual = new com.cw.models.Volume(
                    v.getUUID(),
                    v.getNome(),
                    v.getPontoDeMontagem(),
                    v.getTotal()
            );

            System.out.println(mapVolume.get("existe"));
            if (Integer.parseInt(mapVolume.get("existe").toString()) == 0) {
                System.out.println("\nNovo volume detectado. Inserindo volume...");
                volumeDAO.inserirVolume(volumeAtual, e);

            } else if (Integer.parseInt(mapVolume.get("alterou").toString()) == 1) {
                System.out.println("\n alteração no volume. Atualizando dados...");
                volumeDAO.atualizarVolume(volumeAtual);

            }
        }
    }

    public void registrarGrupoVolumePorMaquina(Empresa emp) {
        List<Volume> volumes = looca.getGrupoDeDiscos().getVolumes();

        for (Volume volume : volumes) {
            try {
                com.cw.models.Volume v = new com.cw.models.Volume(
                        volume.getUUID(),
                        volume.getNome(),
                        volume.getPontoDeMontagem(),
                        volume.getTotal()
                );

                volumeDAO.inserirVolume(v, emp);
            } catch (Exception e) {
                LogsService.gerarLog("Falha ao registrar volume: " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    ;

}
