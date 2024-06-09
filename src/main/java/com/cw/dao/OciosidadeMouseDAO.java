package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.RegistroOciosidadeMouse;
import com.cw.models.Usuario;
import com.cw.services.LogsService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class OciosidadeMouseDAO extends Conexao {
    //TODO: remover formatted

    public OciosidadeMouseDAO() {

    }

    public Integer inserirOciosidadeMouse(RegistroOciosidadeMouse r) {
        String sql = "INSERT INTO tempo_ociosidade (fk_usuario) VALUES (?)";
        Integer key = null;

        try {
            key = keyInsert(sql, r.getFkUsuario()).get("nuvem");

        } catch (Exception e) {
            LogsService.gerarLog("Falha ao registrar ociosidade: " + e.getMessage());
        }

        return key;
    }

    public void updateOciosidadeMouse(RegistroOciosidadeMouse r) {
        String sql = "UPDATE tempo_ociosidade SET tempo_registro_ms = ? WHERE id_tempo_ociosidade = ?";
        try {
            insert(sql, r.getTempoRegistroMs(), r.getIdTempoOciosidade());
        } catch (Exception e) {
            LogsService.gerarLog("Falha ao registrar ociosidade: " + e.getMessage());
        }
    }
}
