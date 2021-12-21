package com.example.sistemarestaurante.Helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode) {
        //verifica versão
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> listaPermissoes = new ArrayList<>();

            //percorre as permissões
            for (String permissao : permissoes){
                Boolean tempermissao = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;
                if (!tempermissao){
                    //adiciona a lista a ser aceita pelo usuario
                    listaPermissoes.add(permissao);
                }
            }

            //caso a lista a ser solicitada esteja vazia
            if (listaPermissoes.isEmpty()) {
                return true;
            }

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //solicita permissão
            ActivityCompat.requestPermissions(activity,novasPermissoes,requestCode);

        }

        return true;
    }
}
