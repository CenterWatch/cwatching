package com.cw.models;

public class Registro {
    private Integer idRegistro;
    private String dtHora;
    private Double usoCpu;
    private Long usoRam;
    private Long disponivelRam;
    private Long uptime;
    private Integer fkSessao;

    public Registro(Double usoCpu, Long usoRam, Long disponivelRam, Long uptime) {
        this.usoCpu = usoCpu;
        this.usoRam = usoRam;
        this.disponivelRam = disponivelRam;
        this.uptime = uptime;
    }

    public Registro() {
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getDtHora() {
        return dtHora;
    }

    public void setDtHora(String dtHora) {
        this.dtHora = dtHora;
    }

    public Double getUsoCpu() {
        return usoCpu;
    }

    public void setUsoCpu(Double usoCpu) {
        this.usoCpu = usoCpu;
    }

    public Long getUsoRam() {
        return usoRam;
    }

    public void setUsoRam(Long usoRam) {
        this.usoRam = usoRam;
    }

    public Long getDisponivelRam() {
        return disponivelRam;
    }

    public void setDisponivelRam(Long disponivelRam) {
        this.disponivelRam = disponivelRam;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public Integer getFkSessao() {
        return fkSessao;
    }

    public void setFkSessao(Integer fkSessao) {
        this.fkSessao = fkSessao;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "idRegistro=" + idRegistro +
                ", dtHora='" + dtHora + '\'' +
                ", usoCpu=" + usoCpu +
                ", usoRam=" + usoRam +
                ", disponivelRam=" + disponivelRam +
                ", uptime=" + uptime +
                ", fkSessao=" + fkSessao +
                '}';
    }
}
