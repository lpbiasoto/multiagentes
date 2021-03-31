package com.company;

import com.company.interfacehecate.InterfaceHecate;

import java.util.Date;
import java.util.Optional;

public class Mensagem<T> {
    Agente remetente;
    T destinatario;
    String conteudo;
    Date timestamp;
    boolean enviada;

    public Mensagem(Agente agenteRemetente, T destinoGenerico, Sala sala, String mensagem, boolean paraSala) throws Exception {
        String retorno = "";
        String mensagemRaw = agenteRemetente.nome + mensagem + sala.nome;
        String mensagemFinal = Util.encodeValue(mensagemRaw);

        this.remetente = agenteRemetente;
        this.destinatario = destinoGenerico;
        this.conteudo = mensagemFinal;
        this.timestamp = new Date();
        this.enviada = false;

        retorno = EnviarMensagem(paraSala);

        this.conteudo = mensagemRaw;
        if (retorno != "ERRO") {
            this.enviada = true;
            if (paraSala)
            {
                ((Sala)destinoGenerico).listaMensagens.add(this);
                for (Agente agente : ((Sala)destinoGenerico).listaAgentes){
                    agente.listaMensagens.add(this);
                }
            }
            else ((Agente) destinoGenerico).listaMensagens.add(this);
        }

    }

    private String EnviarMensagem(boolean paraSala) throws Exception {
        if (paraSala) {
            return InterfaceHecate.GETConnection("/sendMessageToRoom", Optional.of("/?from=" + this.remetente.id + "&to=" +((Sala)this.destinatario).id+ "&message=" + this.conteudo));
        } else
            return InterfaceHecate.GETConnection("/sendMessageToAgent", Optional.of("/?from=" + this.remetente.id + "&to=" +((Agente)this.destinatario).id+ "&message=" + this.conteudo));
    }
}

