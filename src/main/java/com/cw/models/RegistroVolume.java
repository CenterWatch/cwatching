package com.cw.models;

public class RegistroVolume {
    private Integer idRegistroVolume;
    private Long volumeDisponivel;
    private String dtHora;
    private Integer fkSessao;
    private String fkVolume;

    public RegistroVolume(Long volumeDisponivel, String fkVolume) {
        this.volumeDisponivel = volumeDisponivel;
        this.fkVolume = fkVolume;
    }

    public RegistroVolume() {
    }

    public Integer getIdRegistroVolume() {
        return idRegistroVolume;
    }

    public void setIdRegistroVolume(Integer idRegistroVolume) {
        this.idRegistroVolume = idRegistroVolume;
    }

    public Long getVolumeDisponivel() {
        return volumeDisponivel;
    }

    public void setVolumeDisponivel(Long volumeDisponivel) {
        this.volumeDisponivel = volumeDisponivel;
    }

    public String getDtHora() {
        return dtHora;
    }

    public void setDtHora(String dtHora) {
        this.dtHora = dtHora;
    }

    public Integer getFkSessao() {
        return fkSessao;
    }

    public void setFkSessao(Integer fkSessao) {
        this.fkSessao = fkSessao;
    }

    public String getFkVolume() {
        return fkVolume;
    }

    public void setFkVolume(String fkVolume) {
        this.fkVolume = fkVolume;
    }

    @Override
    public String toString() {
        return "RegistroVolume{" +
                "idRegistroVolume=" + idRegistroVolume +
                ", volumeDisponivel=" + volumeDisponivel +
                ", dtHora='" + dtHora + '\'' +
                ", fkSessao=" + fkSessao +
                ", fkVolume='" + fkVolume + '\'' +
                '}';
    }
}
