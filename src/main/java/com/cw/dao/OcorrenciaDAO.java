package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Config;
import com.cw.models.Empresa;
import com.cw.models.Ocorrencia;
import com.cw.services.LogsService;
import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

public class OcorrenciaDAO extends Conexao {

    public OcorrenciaDAO() {
    }

    public void inserirOcorrencia(Ocorrencia o, Config c) {
        String sql1 = "INSERT INTO ocorrencia (titulo, descricao, tipo, fk_sessao) VALUES (?, ?, ?, (SELECT id_sessao FROM sessao JOIN maquina ON id_maquina = fk_maquina WHERE hostname = ? AND fk_empresa = ? ORDER BY dt_hora_sessao DESC LIMIT 1))";
        String sql2 = "INSERT INTO ocorrencia (titulo, descricao, tipo, fk_sessao) VALUES (?, ?, ?, (SELECT TOP 1 id_sessao FROM sessao JOIN maquina ON id_maquina = fk_maquina WHERE hostname = ? AND fk_empresa = ? ORDER BY dt_hora_sessao DESC))";

        try {
            insertDiff(sql1, sql2, o.getTitulo(), o.getDescricao(), o.getTipo(), new Looca().getRede().getParametros().getHostName(), c.getIdConfig());
        } catch(Exception e) {
            LogsService.gerarLog("Falha ao inserir ocorrencia: " + e.getMessage());
        }
    }
}
