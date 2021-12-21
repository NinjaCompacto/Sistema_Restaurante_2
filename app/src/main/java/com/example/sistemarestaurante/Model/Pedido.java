package com.example.sistemarestaurante.Model;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {

    private String nomeGarçom;
    private List<PratoPedido> comida;
    private List<BebidaPedida> bebida;
    private String comidaStauts;
    private String bebidaStauts;
    private String numeroMesa;
    private String id;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBebidaStauts() {
        return bebidaStauts;
    }

    public void setBebidaStauts(String bebidaStauts) {
        this.bebidaStauts = bebidaStauts;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(String numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getComidaStauts() {
        return comidaStauts;
    }

    public void setComidaStauts(String comidaStauts) {
        this.comidaStauts = comidaStauts;
    }

    public List<BebidaPedida> getBebida() {
        return bebida;
    }

    public void setBebida(List<BebidaPedida> bebida) {
        this.bebida = bebida;
    }

    public List<PratoPedido> getComida() {
        return comida;
    }

    public void setComida(List<PratoPedido> comida) {
        this.comida = comida;
    }


    public String getNomeGarçom() {
        return nomeGarçom;
    }

    public void setNomeGarçom(String nomeGarçom) {
        this.nomeGarçom = nomeGarçom;
    }
}
