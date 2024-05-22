package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Empresa;
import com.cw.models.Funcionario;
import com.cw.models.Usuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UsuarioDAO extends Conexao {
    public UsuarioDAO() {

    }

    public Usuario buscarUsuarioPorUsername(String username) {

        Usuario usuario = conexaoLocal.queryForObject("SELECT * FROM usuario WHERE username = '%s'".formatted(username), new BeanPropertyRowMapper<>(Usuario.class));

        return usuario;
    }

    public Boolean autenticarLogin(String username, String senha) {

        List<Usuario> usuario = conexaoNuvem.query("SELECT * FROM usuario WHERE username = '%s' AND senha = '%s'".formatted(username, senha), new BeanPropertyRowMapper<>(Usuario.class));

        return usuario.size() == 1;
    }

    public Empresa buscarEmpresaPorUsername(String username) {
        String sql = "SELECT * FROM empresa JOIN funcionario ON fk_empresa = id_empresa JOIN usuario ON id_usuario = id_funcionario WHERE username = '%s'".formatted(username);

        return conexaoLocal.queryForObject(sql, new BeanPropertyRowMapper<>(Empresa.class));
    }

    public Funcionario buscarFuncionarioPorUsername(String username) {
        String sql = "SELECT * FROM funcionario JOIN usuario ON id_usuario = id_funcionario WHERE username = '%s'".formatted(username);

        return conexaoLocal.queryForObject(sql, new BeanPropertyRowMapper<>(Funcionario.class));
    }
}
