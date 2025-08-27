package com.sicredi.pautachallenge.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class BcryptUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String password) {
        log.debug("Criptografando senha");
        String encryptedPassword = encoder.encode(password);
        log.trace("Senha criptografada com sucesso");
        return encryptedPassword;
    }

    public static boolean comparePasswords(String password, String hashedPassword) {
        log.debug("Comparando senhas");
        boolean matches = encoder.matches(password, hashedPassword);
        log.trace("Comparação de senhas concluída. Resultado: {}", matches);
        return matches;
    }
}
