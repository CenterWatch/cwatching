package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Empresa;
import com.cw.models.RegistroVolume;
import com.cw.models.Volume;
import com.cw.services.LogsService;
import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class RegistroVolumeDAO extends Conexao {

    public RegistroVolumeDAO() {
    }

    public void inserirRegistroVolume(RegistroVolume r, Empresa emp) {
        String sql1 = "INSERT INTO registro_volume (volume_disponivel, fk_volume, fk_sessao) VALUES (?, ?, (SELECT id_sessao FROM sessao JOIN maquina ON id_maquina = fk_maquina WHERE hostname = ? AND fk_empresa = ? ORDER BY dt_hora_sessao DESC LIMIT 1))";
        String sql2 = "INSERT INTO registro_volume (volume_disponivel, fk_volume, fk_sessao) VALUES (?, ?, (SELECT TOP 1 id_sessao FROM sessao JOIN maquina ON id_maquina = fk_maquina WHERE hostname = ? AND fk_empresa = ? ORDER BY dt_hora_sessao DESC))";

        try {
            insertDiff(sql1, sql2, r.getVolumeDisponivel(), r.getFkVolume(), new Looca().getRede().getParametros().getHostName(), emp.getIdEmpresa());
        } catch(Exception e) {
            LogsService.gerarLog("Falha ao inserir registro de volume: " + e.getMessage());
        }
    }

    public RegistroVolume buscarUltimoRegVolumePorUUID(String uuid) {
        RegistroVolume r = new RegistroVolume();
        String sql = "SELECT TOP 1 * FROM registro_volume WHERE fk_volume = ? ORDER BY dt_hora DESC";

        try {
            r = conNuvem.queryForObject(sql, new BeanPropertyRowMapper<>(RegistroVolume.class), uuid);
        } catch (Exception e) {
            System.out.println("Não foi possível buscar registro volume: " + e.getMessage());
            LogsService.gerarLog("Falha ao buscar volume: " + e.getMessage());
        }

        return r;
    }
}
