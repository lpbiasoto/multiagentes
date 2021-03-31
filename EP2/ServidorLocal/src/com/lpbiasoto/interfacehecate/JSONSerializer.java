package com.lpbiasoto.interfacehecate;

import com.lpbiasoto.Acao;
import com.lpbiasoto.Mensagem;
import com.lpbiasoto.Planejamento;
import com.lpbiasoto.Retorno;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONSerializer {

    public static Retorno DesserializarRetorno(String strRetorno) {
        Retorno retorno = new Gson().fromJson(strRetorno, Retorno.class);
        return retorno;
    }

    public static List<Mensagem> DesserializarListaMensagens(String strRetorno) {
        Type listType = new TypeToken<ArrayList<Mensagem>>(){}.getType();
        List<Mensagem> retorno = new Gson().fromJson(strRetorno, listType);
        
        return retorno;
    }

    public static String SerializarPlano(Planejamento planejamento) {
        Gson gson = new Gson();
        String retorno = gson.toJson(planejamento, Planejamento.class);
        return retorno;
    }
    public static String SerializarListaAcoes(List<Acao> listaAcoes) {

        Gson gson = new Gson();
        for (Acao acao: listaAcoes) {
            String retorno = gson.toJson(acao, Acao.class);
        }
        return "";
    }
}
