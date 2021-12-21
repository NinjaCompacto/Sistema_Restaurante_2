package com.example.sistemarestaurante.Model;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Prato implements Serializable {
    private String valor;
    private String NomePrato;
    private String IsDisponivel;
    private String Foto;

    public void salvarPrato (){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference pratoref = databaseReference.child("pratos").child(getNomePrato());
        pratoref.setValue(this);
    }

    public String  getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }


    public String getNomePrato() {
        return NomePrato;
    }

    public void setNomePrato(String nomePrato) {
        NomePrato = nomePrato;
    }

    public String getIsDisponivel() {
        return IsDisponivel;
    }

    public void setIsDisponivel(String isDisponivel) {
        IsDisponivel = isDisponivel;
    }
}
