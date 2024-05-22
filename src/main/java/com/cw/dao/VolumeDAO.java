package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Empresa;
import com.cw.models.Maquina;
import com.cw.models.Volume;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class VolumeDAO extends Conexao {
    MaquinaDAO maquinaDAO = new MaquinaDAO();

    public VolumeDAO() {
    }

    public void inserirVolume(Volume v) {

        String sql = "INSERT INTO volume (uuid, nome, ponto_montagem, volume_total, fk_maquina) VALUES (?, ?, ?, ?, ?)";
        conexaoLocal.update(sql, v.getUUID(), v.getNome(), v.getPontoMontagem(), v.getVolumeTotal(), v.getFkMaquina());
    }

    public void atualizarVolume(Volume v){
        String sql= ("UPDATE volume SET uuid = %d, nome = '%s', ponto_montagem = '%s', volume_total = %d" +
                     "where fk_maquina = %d ").formatted(v.getUUID(),v.getNome(),v.getPontoMontagem(),v.getVolumeTotal());
        conexaoLocal.update(sql, v.getUUID(), v.getNome(), v.getPontoMontagem(), v.getVolumeTotal(),v.getFkMaquina());
    }

    public List<Volume> buscarVolumesPorMaquina(Maquina m) {
        String sql = "SELECT * FROM volume WHERE fk_maquina = %d".formatted(m.getIdMaquina());

        return conexaoLocal.query(sql, new BeanPropertyRowMapper<>(Volume.class));
    }
}
