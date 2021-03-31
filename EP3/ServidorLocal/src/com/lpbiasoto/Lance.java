package com.lpbiasoto;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Lance {
    int agentID;
    String auctionType;
    int bidValue;
    String timestamp;

    public Lance(int agente_id, String auctionType, int lanceAgente) throws InterruptedException {
        this.agentID = agente_id;
        this.auctionType = auctionType;
        this.timestamp = LocalDateTime.now().toString();
        TimeUnit.MILLISECONDS.sleep(1);
        this.bidValue = lanceAgente;
    }

    public int getBidValue() {
        return this.bidValue;
    }

}
