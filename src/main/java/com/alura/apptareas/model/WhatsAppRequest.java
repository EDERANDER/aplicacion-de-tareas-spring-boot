package com.alura.apptareas.model;

public class WhatsAppRequest {
    private String number;
    private String text;

    public WhatsAppRequest(String numero, String texto){
        this.number = numero;
        this.text = texto;
    }

    public WhatsAppRequest(){

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
