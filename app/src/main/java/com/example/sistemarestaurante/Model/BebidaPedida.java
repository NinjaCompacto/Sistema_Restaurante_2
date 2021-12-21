package com.example.sistemarestaurante.Model;

import java.io.Serializable;

public class BebidaPedida implements Serializable {
    private Bebida bebida;
    private int quantidade;
    private String obs;

    public Bebida getBebida() {
        return bebida;
    }

    public void setBebida(Bebida bebida) {
        this.bebida = bebida;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
