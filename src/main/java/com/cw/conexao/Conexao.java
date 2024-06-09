package com.cw.conexao;

import com.cw.services.LogsService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Conexao {
    public static final JdbcTemplate conLocal = setConexaoLocal();
    public static final JdbcTemplate conNuvem = setConexaoNuvem();
    public JdbcTemplate conexao;

    public Conexao() {

    }

    private static JdbcTemplate setConexaoLocal() {

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://mysql:3306/cwdb");
            dataSource.setUsername("root");
            dataSource.setPassword("root");

            return new JdbcTemplate(dataSource);
    }

    private static JdbcTemplate setConexaoNuvem() {

         BasicDataSource dataSource = new BasicDataSource();
         dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         dataSource.setUrl("jdbc:sqlserver://54.198.160.133:1433;database=cwdb;trustServerCertificate=true");
         dataSource.setUsername("sa");
         dataSource.setPassword("cwc@2024");

         return new JdbcTemplate(dataSource);
    }

    public static Boolean testarConexoes() {
        try {
            conLocal.queryForObject("SELECT 1", Integer.class);
            conNuvem.queryForObject("SELECT 1", Integer.class);

            System.out.println("Conexão estabelecida...");
            return true;
        } catch (Exception e) {
            LogsService.gerarLog("Falha ao estabelecer conexão JDBC: " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        }

        return false;
    }

    public void insert(String sql, Object ... args) {
        insertFuture(sql, conLocal, args);
        insertFuture(sql, conNuvem, args);
    }

    public void insertDiff(String sqlL, String sqlN, Object ... args) {
        insertFuture(sqlL, conLocal, args);
        insertFuture(sqlN, conNuvem, args);
    }

    public Map<String, Integer> keyInsert(String sql, Object ... args) {
        Map<String, Integer> keyMap = new HashMap<>();

//        keyMap.put("local", keyInsertFuture(sql, conLocal, args));
        keyMap.put("nuvem", keyInsertFuture(sql, conNuvem, args));

        return keyMap;
    }

    public void insertFuture(String sql, JdbcTemplate con, Object ... args) {
        CompletableFuture.runAsync(() -> {
            con.update(sql, args);
        });
    }

    public Integer keyInsertFuture(String sql, JdbcTemplate con, Object ... args) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Integer key = null;

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                con.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    for (int i = 0; i < args.length; i++) {
                        ps.setObject(i + 1, args[i]);
                    }
                    return ps;
                }, keyHolder);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return keyHolder.getKey().intValue();
        });

        try {
            key = future.get();
        } catch (Exception e) {
            LogsService.gerarLog("Erro ao inserir: " + e.getMessage());
        }

        return key;
    }
}

