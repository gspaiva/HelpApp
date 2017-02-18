package dcc.ufla.br.helpapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String nome;
    public String email;
    public String senha;
    public String endereco;

    public User(){

    }

    public User(String nome, String email, String senha, String endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;

    }


}
