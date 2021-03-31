package com.company;

import com.company.interfacehecate.InterfaceHecate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Agente
{
    int id;
    String nome;
    Sala salaAtual;
    int idSalaInicial;
    int idSalaDestino;
    List<Mensagem> listaMensagens;
    boolean estadoSalaNova;

    public Agente(String nomeAgente, Sala sala) throws Exception
    {
        this.id = this.CriarAgente(nomeAgente);
        this.nome = nomeAgente;
        this.salaAtual = sala;
        this.idSalaInicial = sala.id;
        this.idSalaDestino = 0;
        this.listaMensagens = new ArrayList<Mensagem>();
        this.estadoSalaNova = false;

        this.AlocarSalaInicial(sala);
    }

    private int CriarAgente(String nomeAgente) throws Exception
    {
        String retorno = InterfaceHecate.GETConnection("/createAgent", Optional.of("/?name="+nomeAgente));
        int id = 0;
        if (retorno != "ERRO")
        {
            String[] parts = retorno.split("ID: ");
            id = Integer.parseInt(parts[1]);
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

        System.out.println(this.nome+" (ID "+this.id+") saiu da "+salaAntiga.nome+" (ID "+salaAntiga.id+") e entrou na "+salaNova.nome+" (ID "+salaNova.id+").");
        System.out.println(salaAntiga.nome+" (ID "+salaAntiga.id+") possui "+salaAntiga.listaAgentes.size()+" agentes.");
        System.out.println(salaNova.nome+" (ID "+salaNova.id+") possui "+salaNova.listaAgentes.size()+" agentes.");
    }

    public void EnviarMensagem(Sala salaDestinataria)
    {}

    public void EnviarMensagem(Agente agenteDestinatario)
    {}

}
