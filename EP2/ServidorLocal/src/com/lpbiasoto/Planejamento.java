package com.lpbiasoto;

import com.google.gson.annotations.SerializedName;
import com.lpbiasoto.interfacehecate.InterfaceHecate;
import com.lpbiasoto.interfacehecate.JSONSerializer;

import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Planejamento {

    int idPlano;

    @SerializedName("id")
    int idAgente;

    @SerializedName("actions")
    List<Acao> listaAcao;

    boolean executado;

    public Planejamento(int idAgente){
        this.idPlano = 0;
        this.idAgente = idAgente;
        this.listaAcao = new ArrayList<Acao>();
        this.executado = false;
    }

    public <T> void AdicionarAcao(Util.EnumAcao tipoAcao, T conteudoAcao){
        Acao<T> acao = new Acao(tipoAcao, conteudoAcao);
        this.listaAcao.add(acao);
    }

    public String EnviarListaAcoes() throws Exception {

        String envio = JSONSerializer.SerializarPlano(this);
        String strRetorno = InterfaceHecate.POSTConnection("/agent/setActions", ((Integer)this.idAgente).toString(), envio);

        if (strRetorno!="ERRO"){
            FileWriter writer = new FileWriter("plano-"+this.idAgente+".txt");
            writer.write(envio);
            writer.close();
        }

        return strRetorno;
    }

    private String Executar() throws Exception {
        Logging.Log(Util.EnumLog.INFO,"Executando plano do agente de ID "+ this.idAgente+".");
        String strRetorno = InterfaceHecate.GETConnection("/executePlan", Optional.of("/"+this.idAgente));
        return strRetorno;
    }


    public static void ArmazenarContexto(String estado) throws Exception {
        FileWriter writer = new FileWriter("contexto-"+estado+".txt");
        for (Sala sala : Mundo.listaSalasMundo)
        {
            String strRetorno = InterfaceHecate.GETConnection("/room", Optional.of("/"+sala.getID()));

            Logging.Log(Util.EnumLog.INFO,sala.nome+" (ID "+sala.id+") contendo os seguintes agentes:");
            writer.write(strRetorno + System.lineSeparator());
            for (Agente agente : sala.listaAgentes)
            {
                Logging.Log(Util.EnumLog.INFO,agente.nome+" (ID "+agente.id+")");
                /*writer.write(agente.nome+" (ID "+agente.id+")" + System.lineSeparator());*/
            }
        }
        writer.close();
        Logging.Log(Util.EnumLog.INFO,"Contexto "+estado+" salvo no arquivo: contexto-"+estado+".txt");
    }

    public static void ExecutarPlanos() throws Exception {
        List<Planejamento> listaPlanejamentos = Mundo.listaAgentesMundo.stream().map(x->x.planejamentoExecucao).collect((Collectors.toList()));

        Collections.sort(listaPlanejamentos, new Planejamento.SortbyIDPlano());

        for (Planejamento planejamento : listaPlanejamentos) {
            String retorno = planejamento.Executar();
            if (retorno != "ERRO"){
                planejamento.executado = true;
                Logging.Log(Util.EnumLog.INFO,"Plano de ID "+ planejamento.idPlano+" do agente de ID "+ planejamento.idAgente+" executado.");
            }
        }

    }

    static class SortbyIDPlano implements Comparator<Planejamento>
    {
        public int compare(Planejamento a, Planejamento b)
        {
            return a.idPlano - b.idPlano;
        }
    }
}
