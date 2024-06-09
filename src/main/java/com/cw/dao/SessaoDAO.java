package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.*;
import com.cw.services.LogsService;
import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class SessaoDAO extends Conexao {

    public SessaoDAO() {
    }

    public void registrarSessao(Usuario u, Empresa emp) {
        System.out.println("Registrando sessão");
        String sql = "INSERT INTO sessao (fk_maquina, fk_usuario) VALUES ((SELECT id_maquina FROM maquina WHERE hostname = ? AND fk_empresa = ?), (select id_usuario from usuario where username = ?))";
        try {
            insert(sql, new Looca().getRede().getParametros().getHostName(), emp.getIdEmpresa(), u.getUsername());
            System.out.println("Sessão registrada");
        } catch(Exception e) {
            LogsService.gerarLog("Falha ao registrar sessão: " + e.getMessage());
        }
    }

    public void updateFimSessao(Empresa emp) {
        String sql1 = "UPDATE sessao SET fim_sessao = now() WHERE id_sessao = (SELECT id_sessao FROM sessao JOIN maquina ON id_maquina = fk_maquina WHERE hostname = ? AND fk_empresa = ?)";
        String sql2 = "UPDATE sessao SET fim_sessao = now() WHERE id_sessao = (SELECT id_sessao FROM sessao JOIN maquina ON id_maquina = fk_maquina WHERE hostname = ? AND fk_empresa = ?)";
        try {
            insert(sql1, sql2, new Looca().getRede().getParametros().getHostName(), emp.getIdEmpresa());
        } catch (Exception e) {
            LogsService.gerarLog("Falha ao registrar ociosidade: " + e.getMessage());
        }
    }
}
