package com.example.sistemarestaurante.Firebase;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {
    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;

    //recupera a instancia do FirebaseAuth
    public static FirebaseAuth getAuth (){
        if (auth == null){
            auth =FirebaseAuth.getInstance();
        }
        return auth;
    }
    //recupera a referencia do Firebase Database
    public  static DatabaseReference getDatabaseReference () {
        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
    //recupera a referencia do Storage
    public static  StorageReference getStorageReference () {
        if (storageReference == null){
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }

}
