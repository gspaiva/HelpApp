package dcc.ufla.br.helpapp;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static final Pattern ValidEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static String typeErrorLogin(String mensagem){

        String msg = null;
        if(mensagem.equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                msg = "Este email não está cadastrado no nosso sistema, verifique e tente novamente";
        }
        else if (mensagem.equals("The password is invalid or the user does not have a password.")){
            msg = "Esta senha é inválida";
        }

        return msg;
    }

    public static boolean validEmail(String email){

        Matcher matcher  = ValidEmail.matcher(email);
        return matcher.find();
    }

    public static boolean validSenha(String senha){
        if(senha.length() > 5)
            return true;

        return false;
    }
}
