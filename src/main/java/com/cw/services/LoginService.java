package com.cw.services;

import com.cw.conexao.Node;
import com.cw.dao.*;
import com.cw.models.*;
import com.github.britooo.looca.api.core.Looca;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;

public class LoginService {
    private String user;
    private String senha;

    public static Timer atualizarRegistro;
    public static Timer atualizarVolume;
    public static Timer monitorarProcesso;
    public static Timer monitorarOciosidade;
    public static Timer monitorarRegistroFuncionario;

    public static OciosidadeService ociosidadeService;
    static String hostname = new Looca().getRede().getParametros().getHostName();
    static UsuarioDAO userDao = new UsuarioDAO();
    static MaquinaDAO maquinaDAO = new MaquinaDAO();
    static SessaoDAO sessaoDAO = new SessaoDAO();
    static ConfigDAO configDAO = new ConfigDAO();

    PermProcessoDAO permProcessoDAO = new PermProcessoDAO();

    public static void logar(Boolean loginNode, Usuario u, Boolean monitorarMouse) {

        Usuario user = u;

        if (loginNode) {
            user = Node.autenticar();
            if (user == null) return;
        }

        String username = user.getUsername();
        String senha = user.getSenha();

        Boolean autenticado = userDao.autenticarLogin(username, senha);
        System.out.println(autenticado);

        if (!autenticado) return;

        System.out.println("\nLogin com sucesso. Registrando sessão...");

        // Busca a empresa pelo usuário logado
        Empresa empresa = userDao.buscarEmpresaPorUsername(username);
        System.out.println(empresa);

        // Busca os parâmetros definidos pela empresa
        Config configAtual = configDAO.buscarConfigPorEmpresa(empresa);
        configAtual.setPermProcessos(configDAO.buscarPermProcessosPorConfig(configAtual));
        System.out.println(configAtual);

        // Cadastra a máquina atual caso ela não esteja no banco
        MaquinaService maquinaService = new MaquinaService();
        maquinaService.registrarMaquinaSeNaoExiste(empresa);

        // Busca objetos usuário e máquina para ser registrada a sessão criada
        Usuario usuario = userDao.buscarUsuarioPorUsername(username);

        // Registra a sessão criada ao logar
        sessaoDAO.registrarSessao(usuario, empresa);

//        if (loginNode) Node.listenLogout(sessaoAtual.getIdSessao());

        AlertaService alerta = new AlertaService(configAtual);
        System.out.println(alerta);

        // Inicializa timer para coleta de dados de CPU e RAM
        System.out.println("\nIniciando coleta de dados...\n");

        atualizarRegistro = new Timer();
        atualizarVolume = new Timer();
        monitorarProcesso = new Timer();
        monitorarOciosidade = new Timer();
        monitorarRegistroFuncionario = new Timer();
        SlackService slack = new SlackService();

        atualizarRegistro.schedule(new RegistroService(empresa, alerta), 0, configAtual.getIntervaloRegistroMs());

        monitorarRegistroFuncionario.schedule(new RegistroFuncionario(), 0, 5000);

        // Inicializa timer para coleta de dados de volumes
        atualizarVolume.schedule(new RegistroVolumeService(empresa, alerta), 0, configAtual.getIntervaloVolumeMs());

        // Inicializa timer para monitoramento de processos
        monitorarProcesso.schedule(new ProcessoService(configAtual), 2500, 500);

        // Inicializa o monitoramento de ociosidade de mouse do usuário
        if (monitorarMouse) {
            ociosidadeService = new OciosidadeService(usuario, configAtual.getTimerMouseMs(), configAtual.getSensibilidadeMouse());
            monitorarOciosidade.schedule(ociosidadeService, 0, 500);
        }
    }
}
