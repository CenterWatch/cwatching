package com.cw.services;

import com.cw.conexao.Node;
import com.cw.dao.*;
import com.cw.models.*;
import com.github.britooo.looca.api.core.Looca;

import java.io.IOException;
import java.util.Timer;

public class LoginService {
    private String user;
    private String senha;

    public static Timer atualizarRegistro;
    public static Timer atualizarVolume;
    public static Timer monitorarProcesso;
    public static Timer monitorarOciosidade;

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

        // Busca os parâmetros definidos pela empresa
        Config configAtual = configDAO.buscarConfigPorEmpresa(empresa);
        configAtual.setPermProcessos(configDAO.buscarPermProcessosPorConfig(configAtual));

        // Cadastra a máquina atual caso ela não esteja no banco
        MaquinaService maquinaService = new MaquinaService();
        maquinaService.registrarMaquinaSeNaoExiste(empresa);

        // Busca objetos usuário e máquina para ser registrada a sessão criada
        Usuario usuario = userDao.buscarUsuarioPorUsername(username);
        Maquina maquina = maquinaDAO.buscarMaquinaPorHostnameEEmpresa(hostname, empresa);

        // Registra a sessão criada ao logar
        Integer idSessao = sessaoDAO.registrarSessao(maquina.getIdMaquina(), usuario.getIdUsuario());
        Sessao sessaoAtual = sessaoDAO.buscarSessao(idSessao);

        if (loginNode) Node.listenLogout(sessaoAtual.getIdSessao());

        AlertaService alerta = new AlertaService(configAtual);

        // Inicializa timer para coleta de dados de CPU e RAM
        System.out.println("\nIniciando coleta de dados...\n");

        atualizarRegistro = new Timer();
        atualizarVolume = new Timer();
        monitorarProcesso = new Timer();
        monitorarOciosidade = new Timer();
        SlackService slack = new SlackService();

        atualizarRegistro.schedule(new RegistroService(sessaoAtual, alerta), 0, configAtual.getIntervaloRegistroMs());

        // Inicializa timer para coleta de dados de volumes
        atualizarVolume.schedule(new RegistroVolumeService(sessaoAtual, alerta), 0, configAtual.getIntervaloVolumeMs());

        // Inicializa timer para monitoramento de processos
        monitorarProcesso.schedule(new ProcessoService(configAtual), 2500, 500);

        // Inicializa o monitoramento de ociosidade de mouse do usuário
        ociosidadeService = new OciosidadeService(usuario, configAtual.getTimerMouseMs(), configAtual.getSensibilidadeMouse());
        if (monitorarMouse) monitorarOciosidade.schedule(ociosidadeService, 0, 500);
    }
}
