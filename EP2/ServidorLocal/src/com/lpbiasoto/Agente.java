package com.lpbiasoto;

import com.lpbiasoto.interfacehecate.InterfaceHecate;
import com.lpbiasoto.interfacehecate.JSONSerializer;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Agente
{
    @SerializedName("id")
    int id;

    String nome;
    Sala salaAtual;
    int idSalaInicial;
    int idSalaDestino;
    List<Mensagem> listaMensagens;
    boolean estadoSalaNova;
    Planejamento planejamentoExecucao;

    public Agente(String nomeAgente, Sala sala) throws Exception
    {
        this.id = this.CriarAgente(nomeAgente);
        this.nome = nomeAgente;
        this.salaAtual = sala;
        this.idSalaInicial = sala.id;
        this.idSalaDestino = 0;
        this.listaMensagens = new ArrayList<Mensagem>();
        this.estadoSalaNova = false;
        this.planejamentoExecucao = new Planejamento(this.id);

        this.AlocarSalaInicial(sala);
    }

    private int CriarAgente(String nomeAgente) throws Exception
    {
        String strRetorno = InterfaceHecate.GETConnection("/createAgent", Optional.of("/?name="+nomeAgente));
        int id = 0;
        if (strRetorno != "ERRO")
        {
            Retorno retorno = JSONSerializer.DesserializarRetorno(strRetorno);
            id = retorno.id;
        }
        return id;
    }

    private void AlocarSalaInicial(Sala salaInicial) throws Exception
    {
        salaInicial.AdicionarAgente(this, true);
    }

    public void TrocarSala(Sala salaAntiga, Sala salaNova) throws Exception
    {
        salaAntiga.RemoverAgente(this);
        salaNova.AdicionarAgente(this, false);
        this.salaAtual = salaNova;
        this.estadoSalaNova = (this.salaAtual.id == this.idSalaInicial) ? false : true;

        this.planejamentoExecucao.EnviarListaAcoes();

        Logging.Log(Util.EnumLog.INFO, this.nome+" (ID "+this.id+") saiu da "+salaAntiga.nome+" (ID "+salaAntiga.id+") e entrou na "+salaNova.nome+" (ID "+salaNova.id+").");
        Logging.Log(Util.EnumLog.INFO, salaAntiga.nome+" (ID "+salaAntiga.id+") possui "+salaAntiga.listaAgentes.size()+" agentes.");
        Logging.Log(Util.EnumLog.INFO, salaNova.nome+" (ID "+salaNova.id+") possui "+salaNova.listaAgentes.size()+" agentes.");
    }
}
