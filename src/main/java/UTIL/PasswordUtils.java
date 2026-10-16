package UTIL;

public class PasswordUtils {
    public static boolean verificarSenha(String senhaDigitada, String senhaBanco) {
        return senhaDigitada != null && senhaDigitada.equals(senhaBanco);
    }
}