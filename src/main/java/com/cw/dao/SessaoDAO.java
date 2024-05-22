package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Empresa;
import com.cw.models.Maquina;
import com.cw.models.Sessao;
import com.cw.models.Usuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class SessaoDAO extends Conexao {
    public SessaoDAO() {
    }

    public void registrarSessao(Integer fkMaquina, Integer fkUsuario) {
        String sql = "INSERT INTO sessao (fk_maquina, fk_usuario) VALUES (?, ?)";

        conexaoLocal.update(sql, fkMaquina, fkUsuario);
        conexaoNuvem.update(sql, fkMaquina, fkUsuario);
    }

    public Sessao buscarUltimaSessaoPorMaquina(Integer fkMaquina) {
        String sql = "SELECT * FROM sessao WHERE fk_maquina = %d ORDER BY dt_hora_sessao DESC LIMIT 1".formatted(fkMaquina);

        return conexaoLocal.queryForObject(sql, new BeanPropertyRowMapper<>(Sessao.class));
    }
}
