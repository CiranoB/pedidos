package com.fiap.pedidos.exceptions;

public class InvalidEmailAddress extends RuntimeException{
    public InvalidEmailAddress(String message){
        super(message);
    }
}
