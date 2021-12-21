package com.example.sistemarestaurante.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFireBase {

    public static FirebaseUser getUsuarioLogado () {
        FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
        FirebaseUser user = auth.getCurrentUser();
        return user;
    }
}
