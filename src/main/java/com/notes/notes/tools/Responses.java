package com.notes.notes.tools;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Responses {
    /**
     * Este metodo retorna un ResponseEntity con un status code 409 y un mensaje personalizado
     * @param message es el mensaje que se anade al body del ResponseEntity
     * @return un ResponseEntity con el statusCode y el mensaje indicado
     */
    public static ResponseEntity<String> conflict(String message){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }
    /**
     * Este metodo retorna un ResponseEntity con un status code 201 y un mensaje personalizado
     * @param message es el mensaje que se anade al body del ResponseEntity
     * @return un ResponseEntity con el statusCode y el mensaje indicado
     */
    public static ResponseEntity<String> created(String message){
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
    /**
     * Este metodo retorna un ResponseEntity con un status code 400 y un mensaje personalizado
     * @param message es el mensaje que se anade al body del ResponseEntity
     * @return un ResponseEntity con el statusCode y el mensaje indicado
     */
    public static ResponseEntity<String> badRequest(String message){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    /**
     * Este metodo retorna un ResponseEntity con un status code 404 y un mensaje personalizado
     * @param message es el mensaje que se anade al body del ResponseEntity
     * @return un ResponseEntity con el statusCode y el mensaje indicado
     */
    public static ResponseEntity<String> notFound(String message){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    public static ResponseEntity<String> forbidden(String message){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }


}
