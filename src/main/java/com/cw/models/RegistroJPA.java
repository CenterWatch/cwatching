package com.cw.models;

import javax.persistence.*;

@Entity
@Table(name = "registro")
public class RegistroJPA {

    @Id
    @Column(name = "id_registro")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uso_cpu")
    private Double usoCpu;
    @Column(name = "uso_ram")
    private Long usoRam;
    @Column(name = "disponivel_ram")
    private Long disponivelRam;
    @Column(name = "uptime")
    private Long uptime;

    public RegistroJPA(Double usoCpu, Long usoRam, Long disponivelRam, Long uptime) {
        this.usoCpu = usoCpu;
        this.usoRam = usoRam;
        this.disponivelRam = disponivelRam;
        this.uptime = uptime;
    }

}


