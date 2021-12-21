package com.example.sistemarestaurante.Model;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Mesa implements Serializable {

    private String numeroMesa ;
    private String nomeCliente;
    private List<Pedido> pedidos;
    private DatabaseReference mesaref;

    public void salvarmesa () {
        mesaref = ConfiguracaoFirebase.getDatabaseReference().child("mesas").child(getNumeroMesa());
        mesaref.setValue(this);
    }

    public String getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(String numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}

