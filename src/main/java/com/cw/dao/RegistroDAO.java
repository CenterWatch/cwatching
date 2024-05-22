package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Registro;
import com.cw.models.Sessao;
import com.cw.models.Usuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class RegistroDAO extends Conexao {
    public RegistroDAO() {
    }

    public Registro buscarUltimoRegistroPorSessao(Sessao s) {
        Registro r = new Registro();
        String sql = "SELECT * FROM registro WHERE fk_sessao = %d ORDER BY dt_hora DESC LIMIT 1".formatted(s.getIdSessao());

        try {
            r = conexaoLocal.queryForObject(sql, new BeanPropertyRowMapper<>(Registro.class));
        } catch (Exception e) {}

        return r;
    }

    public void inserirRegistro(Registro r) {
        String sql = "INSERT INTO registro (uso_cpu, uso_ram, disponivel_ram, uptime, fk_sessao) VALUES (?, ?, ?, ?, ?)";

        conexaoLocal.update(sql, r.getUsoCpu(), r.getUsoRam(), r.getDisponivelRam(), r.getUptime(), r.getFkSessao());
        conexaoNuvem.update(sql, r.getUsoCpu(), r.getUsoRam(), r.getDisponivelRam(), r.getUptime(), r.getFkSessao());
    }
}
