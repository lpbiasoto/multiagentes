package com.company;

public class Main {

    public static void main(String[] args) throws Exception
    {
        Mundo mundo = new Mundo();
        DistribuicaoAgentes distribuidor = new DistribuicaoAgentes();
        MonitoramentoMsgs mensagens = new MonitoramentoMsgs();

        System.out.println("Cenário final:");
        for (Sala sala : Mundo.listaSalasMundo)
        {
            System.out.println(sala.nome+" (ID "+sala.id+"):");
            for (Agente agente : sala.listaAgentes)
            {
                System.out.println(agente.nome+" (ID "+agente.id+")");
            }
        }

    }

}
