package com.lpbiasoto;

public class Main {

    public static void main(String[] args) throws Exception
    {
        Mundo mundo = new Mundo();

        Logging.Log(Util.EnumLog.INFO,"Contexto inicial:");
        Planejamento.ArmazenarContexto("inicial");

        DistribuicaoAgentes distribuidor = new DistribuicaoAgentes();
        MonitoramentoMsgs mensagens = new MonitoramentoMsgs();

        Logging.Log(Util.EnumLog.INFO,"Contexto final:");
        Planejamento.ArmazenarContexto("final");
    }

}
