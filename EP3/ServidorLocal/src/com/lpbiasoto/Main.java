package com.lpbiasoto;

public class Main {

    public static void main(String[] args) throws Exception
    {
        Mundo mundo = new Mundo();
        mundo.Inicializar();

        GerenciadorLeiloes leiloes = new GerenciadorLeiloes();
        leiloes.PersistirLeiloes();
        leiloes.IniciarLeiloes();
        leiloes.GerenciarLeiloes();
        leiloes.ConsolidarResultados();

        Logging.Log(Util.EnumLog.INFO,"Finalizando programa...");
    }

}
