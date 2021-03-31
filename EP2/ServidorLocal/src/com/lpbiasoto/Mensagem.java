package com.lpbiasoto;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Mensagem<T> {

    @SerializedName("from")
    int idRemetente;

    @SerializedName("to")
    int idDestinatario;

    @SerializedName("message")
    String conteudo;

    @SerializedName("timestamp")
    String timestamp;

    public Mensagem(Agente agenteRemetente, T destinoGenerico, Sala sala, String mensagem, boolean paraSala) throws Exception {
        String mensagemFinal = agenteRemetente.nome + mensagem + sala.nome;

        this.idRemetente = agenteRemetente.id;
        this.idDestinatario = sala.id;
        this.conteudo = mensagemFinal;
        this.timestamp = LocalDateTime.now().toString();
        /*Formato do timestamp exige um delay para que as mensagens não saiam da ordem
         * ----- LocalDateTime trunca e as mensagens ficam com timestamp iguais ----- */
        TimeUnit.MILLISECONDS.sleep(1);

        ((Sala)destinoGenerico).listaMensagens.add(this);
        for (Agente agente : ((Sala)destinoGenerico).listaAgentes) {
            agente.listaMensagens.add(this);
        }
    }

    public String getTimestamp(){
        return this.timestamp;
    }
    public String getConteudo(){
        return this.conteudo;
    }
}

