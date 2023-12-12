package com.example.demo.utils;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtils {

    /*Il metodo textEncryptor() serve per istanziare un BasicTextEncryptor
     * per settare un chiave di criptaggio sulle password di tutti gli UserDao*/
    @Bean
    public BasicTextEncryptor textEncryptor(){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("mySecretEncryptionKeyBlaBla1234");
        return textEncryptor;
    }

    /*Il metodo encrypt() provvede a criptare una stringa passata in ingresso*/
    public String encrypt(String data){
        return textEncryptor().encrypt(data);
    }

    /*Il metodo encrypt() provvede a criptare una stringa passata in ingresso*/
    public String decrypt(String encryptedData){
        return textEncryptor().decrypt(encryptedData);
    }
}
