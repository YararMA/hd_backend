package com.example.mailsender.model;

public class EmailTemplate {

    public static String emailSubject="Исторический диктант";
    public static String message(String url, String code) {
        return String.format("Для активации профилья пользователя перейдите по ссылке: %s%s", url, code);
    }

}
