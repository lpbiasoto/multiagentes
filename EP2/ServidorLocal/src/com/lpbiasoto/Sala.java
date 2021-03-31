package com.lpbiasoto;

import com.lpbiasoto.interfacehecate.InterfaceHecate;
import com.lpbiasoto.interfacehecate.JSONSerializer;

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
        String strRetorno = InterfaceHecate.GETConnection("/createRoom", Optional.of("/?name="+nomeSala));
        int id = 0;
        if (strRetorno != "ERRO")
        {
            Retorno retorno = JSONSerializer.DesserializarRetorno(strRetorno);
            id = retorno.id;
        }
        return id;
    }

    public void AdicionarAgente(Agente agente, boolean salaInicial) throws Exception
    {
        if (salaInicial){
            String retorno = InterfaceHecate.GETConnection("/addAgentsToRoom", Optional.of("/?room="+this.id+"&agents="+agente.id));
            this.listaAgentes.add(agente);
        }
        else {
            this.listaAgentes.add(agente);

            Mensagem mensagem = new Mensagem(agente, this, this, " ENTROU NA SALA ", true);
            agente.planejamentoExecucao.AdicionarAcao(Util.EnumAcao.move, ((Integer) this.id).toString());
            agente.planejamentoExecucao.AdicionarAcao(Util.EnumAcao.message, mensagem);
        }
    }

    public void AdicionarAgentes(List<Agente> listaAgentes)
    {

    }

    public void RemoverAgente(Agente agente) throws Exception
    {
        Mensagem mensagem = new Mensagem(agente,this, this, " SAIU DA SALA ", true);
        agente.planejamentoExecucao.AdicionarAcao(Util.EnumAcao.leave, ((Integer)this.id).toString());
        agente.planejamentoExecucao.AdicionarAcao(Util.EnumAcao.message, mensagem);

        this.listaAgentes.remove(agente);
    }

    public void RemoverAgentes(List<Agente> listaAgentes)
    {

    }

    public Integer getID()
    {
        return this.id;
    }


}
