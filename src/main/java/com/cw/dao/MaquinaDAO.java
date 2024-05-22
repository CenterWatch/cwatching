package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Empresa;
import com.cw.models.Maquina;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class MaquinaDAO extends Conexao {
    public MaquinaDAO() {
    }

    public void inserirMaquina(Maquina m, Empresa e) {
        if (!verificarMaquinaExistePorHostnameEEmpresa(m.getHostname(), e)) {
            String sql = "INSERT INTO maquina (so, cpu_modelo, ram_total, hostname, fk_empresa) VALUES (?, ?, ?, ?, ?)";

            conexaoNuvem.update(sql, m.getSo(), m.getCpu(), m.getRam(), m.getHostname(), m.getFkEmpresa());
            conexaoLocal.update(sql, m.getSo(), m.getCpu(), m.getRam(), m.getHostname(), m.getFkEmpresa());
        }
    }


    public Boolean verificarComponentesMaquinaExistente(Maquina m) {
        String sql = ("SELECT * FROM maquina WHERE " +
                "hostname = '%s' AND " +
                "cpu_modelo = '%s' AND " +
                "ram_total = %d AND" +
                " so = '%s'").formatted(m.getHostname(), m.getCpu(), m.getRam(), m.getSo());

        return !conexaoLocal.query(sql, new BeanPropertyRowMapper<>(Maquina.class)).isEmpty();
    }
    public void atualizarMaquina(Maquina m, Empresa e) {
        String sql = ("UPDATE maquina SET so = '%s', cpu_modelo = '%s', ram_total = %d " +
                "WHERE hostname = '%s' AND fk_empresa = %d").formatted(m.getSo(), m.getCpu(), m.getRam(), m.getHostname(), e.getIdEmpresa());

        conexaoLocal.update(sql);
        conexaoNuvem.update(sql);
}

    public Boolean verificarMaquinaExistePorHostnameEEmpresa(String hostname, Empresa e) {
        String sql = "SELECT hostname FROM maquina WHERE hostname = '%s' AND fk_empresa = %d".formatted(hostname, e.getIdEmpresa());
        return !conexaoLocal.query(sql, new BeanPropertyRowMapper<>(Maquina.class)).isEmpty();


    }

    public Maquina buscarMaquinaPorHostnameEEmpresa(String hostname, Empresa e) {
        String sql = "SELECT * FROM maquina WHERE hostname = '%s' AND fk_empresa = %d".formatted(hostname, e.getIdEmpresa());
        return conexaoLocal.queryForObject(sql, new BeanPropertyRowMapper<>(Maquina.class));
    }
}
