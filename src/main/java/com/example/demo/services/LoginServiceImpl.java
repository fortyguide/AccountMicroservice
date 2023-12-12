package com.example.demo.services;

import com.example.demo.daos.UserDao;
import com.example.demo.entities.User;
import com.example.demo.utils.EncryptionUtils;
import com.example.demo.utils.JasonWebTokenUtils;
import com.example.demo.utils.UserNotLoggedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Servizio di login
 */
@Service
public class LoginServiceImpl implements LoginService{

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Autowired
    EncryptionUtils encryptionUtils;

    /**
     * Il metodo getUserFromDbAndVerifyPassword() verifica che l'utente sia effettivamente
     * presente nel database, sia trovandone l'id, sia verificando la password.
     *
     * @param id
     * @param password
     * @return
     * @throws UserNotLoggedException
     */
    @Override
    public Optional<User> getUserFromDbAndVerifyPassword(String id, String password) throws UserNotLoggedException {
        //-> userDao, findById(id), encryptionUtils.decrypt(password)
        //-> UserNotLoggedException

        Optional<User> userr = userDao.findById(id);
        if(userr.isPresent()){
            User user = userr.get();
            if(encryptionUtils.decrypt(user.getPassword()).equals(password)) {
                log.info("Username and Password verified");
            }else{
                log.info("Username verified. Password not");
                throw new UserNotLoggedException("User not correctly logged in");
            }
        }

        return userr;
    }

    /**
     * Qualora l'utente esista nel database, il metodo createJwt() crea un nuovo Jason Web Token
     * alla prima richiesta http, utilizzando i dati dell'utente stesso recuperati dal metodo
     * getUserFromDbAndVerifyPassword()
     *
     * @param subject
     * @param name
     * @param permission
     * @param datenow
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public String createJwt(String subject, String name, String permission, Date datenow) throws UnsupportedEncodingException {
        //-> JasonWebTokenUtils.generateJwt(...) -> UnsupportedEncodingException

        Date expDate = datenow;

        //Imposto un periodo di validità del token
        expDate.setTime(datenow.getTime() + (300*1000));
        log.info("Jason Web Token creation. Expiration time: " + expDate.getTime());

        String token = JasonWebTokenUtils.generateJwt(subject, expDate, name, permission);

        return token;
    }

    /**
     * IL metodo verifyJwtAndGetData() recupera il Jason Web Token e, se è valido,
     * vengono recuperati i dati dell'utente da utilizzare per successive operazioni.
     *
     * @param request
     * @return
     * @throws UserNotLoggedException
     * @throws UnsupportedEncodingException
     */
    @Override
    public Map<String, Object> verifyJwtAndGetData(HttpServletRequest request) throws UserNotLoggedException, UnsupportedEncodingException {
        //-> JasonWebTokenUtils.getJwtFromHttpRequest(request) -> UserNotLoggedException
        //-> JasonWebTokenUtils.jwt2Map(jwt) -> UnsupportedEncodingException
        //-> ExpiredJwtException

        //Recupero il Jason Web Token e, se ancora valido, permetto una serie di operazioni
        String token = JasonWebTokenUtils.getJwtFromHttpRequest(request);
        if(token == null){
            throw new UserNotLoggedException("Authentication token not found in the request");
        }

        Map<String, Object> userData = JasonWebTokenUtils.jwt2Map(token);
        return userData;
    }
}
