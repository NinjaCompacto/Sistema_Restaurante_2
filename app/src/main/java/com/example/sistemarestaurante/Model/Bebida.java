package com.example.sistemarestaurante.Model;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Bebida implements Serializable {
    private String valor;
    private String NomeBebida;
    private String IsDisponivel;
    private String Foto;

    public void salvarBebida (){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference bebidaref = databaseReference.child("bebidas").child(getNomeBebida());
        bebidaref.setValue(this);
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNomeBebida() {
        return NomeBebida;
    }

    public void setNomeBebida(String nomeBebida) {
        NomeBebida = nomeBebida;
    }

    public String getIsDisponivel() {
        return IsDisponivel;
    }

    public void setIsDisponivel(String isDisponivel) {
        IsDisponivel = isDisponivel;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }
}
