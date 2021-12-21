package com.example.sistemarestaurante.Cadastro_e_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Firebase.UsuarioFireBase;
import com.example.sistemarestaurante.Helper.Permissao;
import com.example.sistemarestaurante.Model.Base64Custom;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.Permission;

public class CadastroActivity extends AppCompatActivity {


    //XML
    private ImageView imageViewCadastro;
    private EditText editTextNome, editTextEmail, editTextSenha;
    private Button buttonCadastrar;
    private Spinner spinnerFuncao;

    //Firebase
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private Bitmap ImagemFoto = null;

    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //configurações iniciais:
            //XML
        editTextEmail = findViewById(R.id.editTextEmailCadastro);
        editTextNome = findViewById(R.id.editTextNomeCadastro);
        editTextSenha = findViewById(R.id.editTextSenhaCadastro);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
        spinnerFuncao = findViewById(R.id.spinner);
            //Firebase
        databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        storageReference = ConfiguracaoFirebase.getStorageReference();
        auth = ConfiguracaoFirebase.getAuth();




        //configura spinner para escolha de função

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.funcao, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFuncao.setAdapter(adapter);
        spinnerFuncao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String funcao = parent.getItemAtPosition(position).toString();
                usuario.setFuncao(funcao);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String funcaopadrao = "Garçom";
                usuario.setFuncao(funcaopadrao);
            }
        });


        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCadastro();
                salvarUsuarioFirebase();
            }
        });
    }

    public void  salvarUsuarioFirebase () {
        auth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i  = new Intent(CadastroActivity.this,FotoCadastroActivity.class);
                    finish();
                    i.putExtra("dadosusuario",usuario);
                    startActivity(i);
                }
                else {
                    String exececao ="";
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        exececao = "Digite uma senha mais forte!";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        exececao = "Digite um e-mail valido!";
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        exececao = "Este usuário já existe!";
                    }
                    catch (Exception e) {
                        exececao = "Erro ao criar usuario: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,exececao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public  void validarCadastro () {
        String nome = editTextNome.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha  = editTextSenha.getText().toString();
        if (!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!senha.isEmpty()){
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    usuario.setId(Base64Custom.codificarBase64(email));
                }
                else {
                    Toast.makeText(CadastroActivity.this,"Preencha a senha !",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(CadastroActivity.this,"Preencha o email !",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(CadastroActivity.this,"Preencha o nome !",Toast.LENGTH_LONG).show();
        }

    }

}