package com.company;

import com.company.interfacehecate.InterfaceHecate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sala
{
    int id;
    String nome;
    List<Agente> listaAgentes;
    List<Mensagem> listaMensagens;

    public Sala(String nomeSala) throws Exception
    {
        this.id = this.CriarSala(nomeSala);
        this.nome = nomeSala;
        this.listaAgentes = new ArrayList<Agente>();
        this.listaMensagens = new ArrayList<Mensagem>();
    }

    private int CriarSala(String nomeSala) throws Exception
    {
        String retorno = InterfaceHecate.GETConnection("/createRoom", Optional.of("/?name="+nomeSala));
        int id = 0;
        if (retorno != "ERRO")
        {
            String[] parts = retorno.split("ID: ");
            id = Integer.parseInt(parts[1]);
        }
        return id;
    }

    public void AdicionarAgente(Agente agente, boolean salaInicial) throws Exception
    {
        String retorno = InterfaceHecate.GETConnection("/addAgentsToRoom", Optional.of("/?room="+this.id+"&agents="+agente.id));
        this.listaAgentes.add(agente);

        if (!salaInicial) {
            Mensagem mensagem = new Mensagem(agente, this, this, " ENTROU NA SALA ", true);
        }
    }

    public void AdicionarAgentes(List<Agente> listaAgentes)
    {

    }

    public void RemoverAgente(Agente agente) throws Exception
    {
        String retorno = InterfaceHecate.GETConnection("/removeAgentsFromRoom", Optional.of("/?room="+this.id+"&agents="+agente.id));
        this.listaAgentes.remove(agente);

        Mensagem mensagem = new Mensagem(agente,this, this, " SAIU DA SALA ", true);
    }

    public void RemoverAgentes(List<Agente> listaAgentes)
    {

    }

    public Integer getID()
    {
        return this.id;
    }


}
