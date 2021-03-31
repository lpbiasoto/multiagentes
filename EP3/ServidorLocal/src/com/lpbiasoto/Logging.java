package com.lpbiasoto;

public class Logging {
    public static void Log(Util.EnumLog tipoLog, String texto){
        switch (tipoLog){
            case INFO:
                System.out.println("INFO: "+texto);
                break;
            case ERROR:
                System.out.println("!!!! ERRO !!!! "+texto);
                break;
            case WARNING:
                System.out.println("!!!! WARNING !!!! "+texto);
                break;
            default:
                System.out.println(texto);
                break;
        }
    }
}
