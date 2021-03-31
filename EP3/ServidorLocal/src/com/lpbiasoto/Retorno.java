package com.lpbiasoto;

import java.util.List;

public class Retorno {

    public int id;
    public String name;

    Leilao.AuctionType auctionType;
    int maximumValue;
    int bestBid;
    String auctionWinner;

    List<Leilao> leiloes;

    List<Agente> agentes;

    int agentID;
    int bidValue;
}
