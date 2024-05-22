package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Ocorrencia;
import org.springframework.jdbc.core.JdbcTemplate;

public class OcorrenciaDAO extends Conexao {
    public OcorrenciaDAO() {
    }

    public void inserirOcorrencia(Ocorrencia o) {
        String sql = "INSERT INTO ocorrencia (titulo, descricao, tipo, fk_sessao) VALUES (?, ?, ?, ?)";
        System.out.println(o);

        conexaoLocal.update(sql, o.getTitulo(), o.getDescricao(), o.getTipo(), o.getFkSessao());
        conexaoNuvem.update(sql, o.getTitulo(), o.getDescricao(), o.getTipo(), o.getFkSessao());
    }
}
