package com.fiap.pedidos.exceptions;

public class InvalidProductQuantity extends RuntimeException{
    public InvalidProductQuantity(String message){
        super(message);
    }

}
