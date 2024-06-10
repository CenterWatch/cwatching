package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.services.RegistroFuncionario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

public class RegistroFuncionarioDAO extends Conexao {
    public List<RegistroFuncionario> getRegistroFuncionario() {
        List<RegistroFuncionario> registros = conLocal.query("SELECT * FROM registroFuncionario", new BeanPropertyRowMapper<>(RegistroFuncionario.class));

        return registros;
    }
}
