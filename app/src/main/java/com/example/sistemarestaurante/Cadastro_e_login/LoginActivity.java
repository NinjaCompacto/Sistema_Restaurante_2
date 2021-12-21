package com.example.sistemarestaurante.Cadastro_e_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.MainActivity;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    //Firebase
    FirebaseAuth auth = ConfiguracaoFirebase.getAuth();

    //XML
    private TextView textViewCadastrase;
    private EditText editTextEmail, editTextSenha;
    private Button buttonEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //configurações iniciais
        textViewCadastrase = findViewById(R.id.textViewCadastrese);
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextSenha = findViewById(R.id.editTextSenhaLogin);
        buttonEntrar = findViewById(R.id.buttonEntrar);

        //abre a tela de cadastro
        textViewCadastrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CadastroActivity.class);
                startActivity(i);
            }
        });
        //auth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( auth.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void validarUsuario (View view) {
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();

        //validação de campos
        if (!email.isEmpty()){
            if(!senha.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);
                logarUsuario(usuario);
            }
            else {
                Toast.makeText(LoginActivity.this,"Preencha a senha!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(LoginActivity.this,"Preencha o email!",Toast.LENGTH_LONG).show();
        }
    }

    //abri tela principal e loga usuario
    public void logarUsuario (Usuario usuario) {
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Sucesso ao Entrar", Toast.LENGTH_LONG).show();
                    finish();
                    abrirTelaPrincipal();
                }
                else {
                    String execao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        execao = "Email ou senha não correspondem !";
                    }catch (FirebaseAuthInvalidUserException e){
                        execao = "Usuário invalido !";
                    }catch (Exception e){
                        execao = "erro ao logar usuario";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,execao, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void abrirTelaPrincipal () {
        Intent i  = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}