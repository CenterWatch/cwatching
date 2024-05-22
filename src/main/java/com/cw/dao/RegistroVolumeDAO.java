package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.RegistroVolume;
import com.cw.models.Volume;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class RegistroVolumeDAO extends Conexao {
    public RegistroVolumeDAO() {
    }

    public void inserirRegistroVolume(RegistroVolume r) {
        String sql = "INSERT INTO registro_volume (volume_disponivel, fk_sessao, fk_volume) VALUES (?, ?, ?)";

        conexaoLocal.update(sql, r.getVolumeDisponivel(), r.getFkSessao(), r.getFkVolume());
        conexaoNuvem.update(sql, r.getVolumeDisponivel(), r.getFkSessao(), r.getFkVolume());
    }

    public RegistroVolume buscarUltimoRegVolumePorUUID(String uuid) {
        RegistroVolume r = new RegistroVolume();
        String sql = "SELECT * FROM registro_volume WHERE fk_volume = '%s' ORDER BY dt_hora DESC LIMIT 1".formatted(uuid);

        try {
            r = conexaoLocal.queryForObject(sql, new BeanPropertyRowMapper<>(RegistroVolume.class));
        } catch (Exception e) {
            System.out.println("Não foi possível buscar registro volume: " + e);
        }

        return r;
    }
}
