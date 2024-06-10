package com.cw.dao;

import com.cw.models.RegistroJPA;

import javax.persistence.EntityManager;

public class RegistroJpaDAO {

    EntityManager em;

    public RegistroJpaDAO(EntityManager em) {
        this.em = em;
    }

    public void registrar(RegistroJPA registro) {
        this.em.persist(registro);
    }
}