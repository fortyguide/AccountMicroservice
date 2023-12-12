package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.utils.UserNotLoggedException;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface LoginService {

    Optional<User> getUserFromDbAndVerifyPassword(String id, String password) throws UserNotLoggedException;
    //-> userDao, findById(id), encryptionUtils.decrypt(password)
    //-> UserNotLoggedException

    String createJwt(String subject, String name, String permission, Date datenow) throws UnsupportedEncodingException;
    //-> JasonWebTokenUtils.generateJwt(...) -> UnsupportedEncodingException

    Map<String, Object> verifyJwtAndGetData(HttpServletRequest request) throws UserNotLoggedException, UnsupportedEncodingException;
    //-> JasonWebTokenUtils.getJwtFromHttpRequest(request) -> UserNotLoggedException
    //-> JasonWebTokenUtils.jwt2Map(jwt) -> UnsupportedEncodingException
    //-> ExpiredJwtException
}
