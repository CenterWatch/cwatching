package com.cw.dao;

import com.cw.conexao.Conexao;
import com.cw.models.Empresa;
import com.cw.models.Registro;
import com.cw.services.LogsService;
import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class RegistroDAO extends Conexao {

    public RegistroDAO() {
    }

    public Registro buscarUltimoRegistroPorSessao(Empresa emp) {
        Registro r = new Registro();
        String sql = "SELECT TOP 1 * FROM registro WHERE fk_sessao = (SELECT TOP 1 id_sessao FROM sessao JOIN maquina ON id_maquina = fk_maquina WHERE hostname = ? AND fk_empresa = ? ORDER BY dt_hora_sessao DESC) ORDER BY dt_hora DESC";

        try {
            r = conNuvem.queryForObject(sql, new BeanPropertyRowMapper<>(Registro.class), new Looca().getRede().getParametros().getHostName(), emp.getIdEmpresa());
        } catch (Exception e) {
            LogsService.gerarLog("Falha ao buscar último registro da sessão: " + e.getMessage());
        }

        return r;
    }

    public void inserirRegistro(Registro r, Empresa emp) {
        String sql2 = "INSERT INTO registro (uso_cpu, uso_ram, disponivel_ram, uptime, fk_sessao) VALUES (?, ?, ?, ?, (SELECT TOP 1 id_sessao FROM sessao JOIN maquina ON id_maquina = fk_maquina WHERE hostname = ? AND fk_empresa = ? ORDER BY dt_hora_sessao DESC))";

        try {
            insertDiff(sql2, r.getUsoCpu(), r.getUsoRam(), r.getDisponivelRam(), r.getUptime(), new Looca().getRede().getParametros().getHostName(), emp.getIdEmpresa());
        } catch (Exception e) {
            LogsService.gerarLog("Falha ao inserir registro: " + e.getMessage());
        }
    }
}
