package com.lpbiasoto;

import com.google.gson.annotations.SerializedName;

public class Acao<T> {

    @SerializedName("actionType")
    Util.EnumAcao tipoAcao;

    @SerializedName("actionParameter")
    T conteudoAcao;

    public Acao(Util.EnumAcao tipo, T conteudo){
        setTipo(tipo);
        setConteudo(conteudo);
    }
    private void setConteudo(T conteudo) {
        this.conteudoAcao = conteudo;
    }

    private void setTipo(Util.EnumAcao tipo) {
        this.tipoAcao = tipo;
    }

    public T getConteudo(){
        return conteudoAcao;
    }

}
