package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Config;
import com.cw.models.PermProcesso;
import com.cw.services.LogsService;
import com.mysql.cj.log.Log;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermProcessoDAO extends Conexao {

    public PermProcessoDAO() {

    }

    public List<Map<String, Object>> buscarProcessos(Config c) {
        List<Map<String, Object>> p = new ArrayList<>();

        try {
            String sql = "SELECT * FROM perm_processo WHERE fk_config = ?";

            p = conNuvem.queryForList(sql, c.getIdConfig());
        } catch (Exception e){
            LogsService.gerarLog("Falha ao buscar processos" + e.getMessage());
        }

        return p;
    }

    public void inserirPermProcesso(String p, String path, Config c) {
        String sql = "INSERT INTO perm_processo (nome, path, fk_config) values (?, ?, ?)";
        try{
            System.out.println("Adicionado: " + p);
            conLocal.update(sql, p, path, c.getIdConfig());
            conNuvem.update(sql, p, path, c.getIdConfig());

        }catch (Exception e){
            LogsService.gerarLog("Falha ao inserir processo: " + e.getMessage());
        }
    }
}
