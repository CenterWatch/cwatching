package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Alerta;
import org.springframework.jdbc.core.JdbcTemplate;

public class AlertaDAO extends Conexao {
    public AlertaDAO() {
    }

    public void inserirAlerta(Alerta l) {
        System.out.println("Inserindo alerta...");
        String sql = "INSERT INTO alerta(tipo, descricao, fk_registro, fk_reg_volume) values (?, ?, ?, ?)";
        conexaoLocal.update(sql, l.getTipo(), l.getDescricao(), l.getFkRegistro(), l.getFkRegVolume());
    }
}
