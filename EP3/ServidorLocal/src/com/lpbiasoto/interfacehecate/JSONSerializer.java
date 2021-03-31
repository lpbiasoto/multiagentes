package com.lpbiasoto.interfacehecate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lpbiasoto.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class JSONSerializer {

    public static Retorno DesserializarRetorno(String strRetorno) {
        Retorno retorno = new Gson().fromJson(strRetorno, Retorno.class);
        return retorno;
    }

    public static ArrayList<Leilao> DesserializarFinalizados(String strRetorno) {

        ArrayList<Leilao> jsonArray = new Gson().fromJson(strRetorno, new TypeToken<ArrayList<Leilao>>() {}.getType());

        return jsonArray;
    }

    public static String SerializarAgente(Agente agente) {
        Gson gson = new Gson();
        String retorno = gson.toJson(agente, Agente.class);
        return retorno;
    }

    public static String SerializarLance(Lance lance) {
        Gson gson = new Gson();
        String retorno = gson.toJson(lance, Lance.class);
        return retorno;
    }

    public static LimitesLances DesserializarMaxAuctionValues(String strRetorno) {
        LimitesLances maxAuctionValues;
        maxAuctionValues = new Gson().fromJson(strRetorno, LimitesLances.class);
        return maxAuctionValues;
    }

    public static List<Leilao> DesserializarListaLeiloes(String info_leiloes_encerrados) {
        Type listType = new TypeToken<ArrayList<Leilao>>(){}.getType();
        List<Leilao> retorno = new Gson().fromJson(info_leiloes_encerrados, listType);

        return retorno;
    }

    public static String SerializarListaLances(List<Lance> listaLances) {
        Type listType = new TypeToken<ArrayList<Lance>>(){}.getType();
        String json = new Gson().toJson(listaLances, listType);

        return json;
    }

    public static String SerializarListaLeiloes(List<Leilao> listaLeiloes) {
        Type listType = new TypeToken<ArrayList<Leilao>>(){}.getType();
        String json = new Gson().toJson(listaLeiloes, listType);

        return json;
    }
}
