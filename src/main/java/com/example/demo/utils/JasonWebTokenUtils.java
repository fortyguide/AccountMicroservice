package com.example.demo.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Questa classe fornisce un metodo per generare e convalidare i JSON token web e
 * verrà automaticamente istanziata e iniettata da Spring
 */
public class JasonWebTokenUtils {
    /**
     * Il metodo generateJwt() genera il JSON web token da inviare al client
     */
    public static String generateJwt(String subject, Date date, String name, String scope) throws java.io.UnsupportedEncodingException{

        String jwt = Jwts.builder()
                .setSubject(subject)
                .setExpiration(date)
                .claim("name", name)
                .claim("scope", scope)
                .signWith(
                        SignatureAlgorithm.HS256,
                        "myPersonalSecretKey12345".getBytes("UTF-8")
                )
                .compact();

        return jwt;
    }

    /**
     * Il metodo jwt2Map() converte il token in una mappa di Userdata verificandone la validità
     */
    public static Map<String, Object> jwt2Map(String jwt) throws java.io.UnsupportedEncodingException, ExpiredJwtException{

        Jws<Claims> claim = Jwts.parser()
                .setSigningKey("myPersonalSecretKey12345".getBytes("UTF-8"))
                .parseClaimsJws(jwt);

        String name = claim.getBody().get("name", String.class);
        String scope = (String) claim.getBody().get("scope");

        Date expDate = claim.getBody().getExpiration();
        String subj = claim.getBody().getSubject();

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("scope", scope);
        userData.put("exp_date", expDate);
        userData.put("subject", subj);

        Date now = new Date();
        if(now.after(expDate)){
            throw new ExpiredJwtException(null, null, "Session expired!");
        }

        return userData;
    }


    /**
     * Il metodo getJwtFromHttpRequest estrae il token dall'intestazione
     * o dal cookie nella richiesta Http
     */
    public static String getJwtFromHttpRequest(HttpServletRequest request){
        String jwt = null;
        if(request.getHeader("jwt") != null){
            jwt = request.getHeader("jwt");     //token presente nell'header
        }else if(request.getCookies() != null){
            Cookie[] cookies = request.getCookies();   //token presente nel cookie
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    jwt = cookie.getValue();
                }
            }
        }
        return jwt;
    }

}
