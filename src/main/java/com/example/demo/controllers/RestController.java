package com.example.demo.controllers;

import com.example.demo.entities.Operation;
import com.example.demo.entities.User;
import com.example.demo.services.LoginService;
import com.example.demo.services.OperationService;
import com.example.demo.utils.UserNotLoggedException;;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/* Il RestController è una classe di Spring i cui metodi si occupano
 di gestire tutte le richieste http */
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @Autowired
    LoginService loginService;

    @Autowired
    OperationService operationService;

    /* @RequestMapping serve per mappare tutte le richieste http
    su degli indirizzi URL */
    @RequestMapping("/hello")
    /* @ResponseBody indica che ciò che verrà restituito dal metodo
    * che contrassegna, sarà direttamente il corpo del messaggio */
    public String sayHello(){
        return "Hello everyone!";
    }

    /*L'Inner class JsonResponseBody() è utilizzata come oggetto
    * legato al body della ResponseEntity.
    * È importante avere questo oggetto perché è composto dal codice
    * di risposta del server e dall'oggetto di risposta.
    * Poi la libreria Jackson converte automaticamente questo
    * oggetto JsonResponseBody in una risposta JSON.*/
    @AllArgsConstructor
    public class JsonResponseBody{
        @Getter @Setter
        private int server;

        @Getter @Setter
        private Object response;

        /*Esempio JSON response:
        {
            server: 500,
            response: {...}
        }*/
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResponseBody> loginUser(@RequestParam(value ="id") String id,
                                                      @RequestParam(value="password") String pwd) {
        //verifica se l'utente esiste nel DB -> se esiste, genera il JSON web token e lo invia al client
        try {
            Optional<User> userr = loginService.getUserFromDbAndVerifyPassword(id, pwd);
            if(userr.isPresent()){
                User user = userr.get();
                String jwt = loginService.createJwt(user.getId(), user.getUsername(), user.getPermission(), new Date());
                return ResponseEntity.status(HttpStatus.OK).header("jwt", jwt).body(new JsonResponseBody(HttpStatus.OK.value(),
                                                                                                                    "Success! User logged in!"));
            }
        } catch (UserNotLoggedException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(),
                                                            "Login failed! Wrong credentials " + e1.toString()));
        } catch (UnsupportedEncodingException e2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(),
                                                            "Token Error " + e2.toString()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(),
                                                        "No corrispondence in the database"));
    }

    @PostMapping("/operations/account/{account}")
    public ResponseEntity<JsonResponseBody> fetchAllOperationsPerAccount(HttpServletRequest request,
                                                                         @PathVariable(name = "account") String account) {
        //request -> recupero del JSON web token -> verifica della validità del token
        // -> ottenimento delle operazioni da parte dell'account utente
        try {
            loginService.verifyJwtAndGetData(request);
            //utente verificato
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(),
                                                                                  operationService.getAllOperationPerAccount(account)));
        } catch (UserNotLoggedException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(),
                                                                                 "Unsupported Encoding: " + e1.toString()));
        } catch (UnsupportedEncodingException e2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(),
                                                                                 "User not correctly logged encoding: " + e2.toString()));
        } catch(ExpiredJwtException e3) {
            //il Jason Web Token è scaduto
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(),
                                                                                       "Session Expired!:" + e3.toString()));
        }
    }

    @PostMapping("/accounts/user")
    public ResponseEntity<JsonResponseBody> fetchAllAccountsPerUser(HttpServletRequest request) {
        //request -> recupero del JSON web token -> recupero dei dati dell'utente
        //ottenimento di tutti gli account dell'utente dal DB
        try {
            Map<String, Object> userData = loginService.verifyJwtAndGetData(request);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(),
                                                                operationService.getAllAccountsPerUser((String) userData.get("subject"))));
        } catch (UserNotLoggedException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(),
                                                                                 "User not logged! Login first: " + e1.toString()));
        } catch (UnsupportedEncodingException e2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(),
                                                                                   "Bad Request: " + e2.toString()));
        } catch(ExpiredJwtException e3) {
            //il Jason Web Token è scaduto
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(),
                                                                                       "Session Expired!:" + e3.toString()));
        }
    }

    @PostMapping("/operations/add")
    public ResponseEntity<JsonResponseBody> addOperation(HttpServletRequest request,
                                                         Operation operation,
                                                         BindingResult bindingResult) {
        //request -> recupero del JSON web token -> recupero dei dati dell'utente ->
        //salvataggio delle operazioni nel DB
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(),
                                                                                 "Error! Invalid format of data"));
        }
        try {
            loginService.verifyJwtAndGetData(request);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), operationService.saveOperation(operation)));
        } catch (UserNotLoggedException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(),
                                                                                 "User not logged! Login first: " + e1.toString()));
        } catch (UnsupportedEncodingException e2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(),
                                                                                 "Bad Request: " + e2.toString()));
        } catch(ExpiredJwtException e3) {
            //il Jason Web Token è scaduto
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(),
                                                                                       "Session Expired!:" + e3.toString()));
        }
    }
}