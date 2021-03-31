package com.lpbiasoto;

import com.lpbiasoto.interfacehecate.InterfaceHecate;
import com.lpbiasoto.interfacehecate.JSONSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Leilao {

    AuctionType auctionType;
    int maximumValue;
    int bestBid;
    String auctionWinner;

    List<Agente> agentesParticipantes;
    boolean encerrado;

    public enum AuctionType
    {
        SITE,
        FLOORS,
        WALLS,
        ROOF,
        WINDOWS,
        DOORS,
        PLUMBING,
        ELECTRICAL,
        EXTERIOR,
        INTERIOR
    }

    public Leilao(){
    }

    public Leilao(AuctionType auctionType, int valorMaximo){
        this.auctionType = auctionType;
        this.maximumValue = valorMaximo;
        this.agentesParticipantes = new ArrayList<Agente>();
    }

    public void AtualizaVencedor(AuctionType current_auctionType, List<Lance> lances_leilao_atual) throws Exception{

        String vencedor = "1000";
        Lance lance_vencedor = null;
        String json = InterfaceHecate.GETConnection("/finishedAuctions", Optional.of(""));
        List<Leilao> array_json = JSONSerializer.DesserializarFinalizados(json);

        for (Leilao leilao : array_json){
            if (leilao.auctionType == current_auctionType) vencedor = leilao.auctionWinner;
        }

        int idVencedor = Integer.parseInt(vencedor);
        Agente agenteVencedor = Mundo.RetornarAgente(idVencedor);

        if (agenteVencedor != null){
            lance_vencedor = lances_leilao_atual.stream().filter(t -> t.agentID == idVencedor).findFirst().orElse(null);
            agenteVencedor.AdicionarLanceVencedor(lance_vencedor);
        }
        Logging.Log(Util.EnumLog.INFO, "->Empresa vencedora: " + agenteVencedor.name);

        this.auctionWinner = vencedor;
    }

}
