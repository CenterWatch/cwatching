package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Config;
import com.cw.models.Empresa;
import com.cw.models.PermProcesso;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ConfigDAO extends Conexao {
    public Config buscarConfigPorEmpresa(Empresa e) {
        String sql = "SELECT * FROM config WHERE id_config = %d".formatted(e.getIdEmpresa());

        return conexaoLocal.queryForObject(sql, new BeanPropertyRowMapper<>(Config.class));
    }

    public List<PermProcesso> buscarPermProcessosPorConfig(Config c) {
        String sql = "SELECT * FROM perm_processo WHERE fk_config = %d".formatted(c.getIdConfig());

        return conexaoLocal.query(sql, new BeanPropertyRowMapper<>(PermProcesso.class));
    }
}
