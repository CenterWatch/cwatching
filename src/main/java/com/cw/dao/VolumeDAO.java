package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Empresa;
import com.cw.models.Maquina;
import com.cw.models.Volume;
import com.cw.services.LogsService;
import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class VolumeDAO extends Conexao {
    //TODO: remover formatted
    MaquinaDAO maquinaDAO = new MaquinaDAO();

    public VolumeDAO() {
    }

    public void inserirVolume(Volume v, Empresa emp) {
        String sql = "INSERT INTO volume (uuid, nome, ponto_montagem, volume_total, fk_maquina) VALUES (?, ?, ?, ?, (SELECT id_maquina FROM maquina WHERE hostname = ? AND fk_empresa = ?))";

        try{
            insert(sql, v.getUUID(), v.getNome(), v.getPontoMontagem(), v.getVolumeTotal(), new Looca().getRede().getParametros().getHostName(), emp.getIdEmpresa());
        }catch (Exception e) {
            LogsService.gerarLog("Falha ao inserir volume: " + e.getMessage());
        }

    }
    public void atualizarVolume(Volume v){
        String sql= ("UPDATE volume SET nome = ?, ponto_montagem = ?, volume_total = ? WHERE uuid = ? ");

        try {
            insert(sql, v.getNome(), v.getPontoMontagem(), v.getVolumeTotal(), v.getUUID());
        } catch (Exception e) {
            LogsService.gerarLog("Falha ao atualizar volume: " + e.getMessage());
        }


    }

    public List<Volume> buscarVolumesPorMaquina(Maquina m, Empresa e) {
        String sql = "SELECT v.* FROM volume v JOIN maquina ON fk_maquina = id_maquina WHERE hostname = '%s' AND fk_empresa = %d".formatted(m.getHostname(), e.getIdEmpresa());

        List<Volume> v;

        try {
            v = conNuvem.query(sql, new BeanPropertyRowMapper<>(Volume.class));
        } catch (Exception ex) {
            LogsService.gerarLog("Falha ao buscar volume por máquina: " + ex.getMessage());
            return null;
        }

        return v;
    }

    public Map<String, Object> volumeAlterou(Volume v) {
        String sql = """
                SELECT
                    CASE WHEN (SELECT COUNT(*) FROM volume WHERE uuid=?) > 0 THEN 1 ELSE 0 END AS existe,
                    CASE WHEN (SELECT COUNT(*) FROM volume WHERE uuid=? AND nome=? AND ponto_montagem=? AND volume_total=?) = 0 THEN 1 ELSE 0 END AS alterou;
                """;

        try {
            return conNuvem.queryForMap(sql, v.getUUID(), v.getUUID(), v.getNome(), v.getPontoMontagem(), v.getVolumeTotal());
        } catch (Exception e) {
            LogsService.gerarLog("Falha ao conferir se volume alterou: " + e.getMessage() + e.getStackTrace());
            return null;
        }

    }
}
