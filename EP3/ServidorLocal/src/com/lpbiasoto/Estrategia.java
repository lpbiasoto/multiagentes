package com.lpbiasoto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Estrategia {

    int idAgente;
    Map<String, Integer> limitesMinimos;
    boolean restricaoEspecifica;

    public Estrategia(Agente agente, List<String> auctionTypes) {
        this.idAgente = agente.id;
        this.limitesMinimos = new HashMap<String, Integer>();
        switch (agente.name) {
            case "Empresa1":
            {
                for (String auctionType : auctionTypes)
                {
                    this.limitesMinimos.put(auctionType, 5);
                }
                break;
            }
            case "Empresa2":
            {
                for (String auctionType : auctionTypes)
                {
                    this.limitesMinimos.put(auctionType, 10);
                }
                break;
            }
            case "Empresa3":
            {
                for (String auctionType : auctionTypes) {
                    int lanceMinimo = 50;
                    switch (auctionType) {
                        case "SITE":
                        case "ROOF":
                            lanceMinimo = 15;
                            break;
                        case "FLOORS":
                        case "ELECTRICAL":
                            lanceMinimo = 5;
                            break;
                        case "WALLS":
                        case "DOORS":
                        case "EXTERIOR":
                            lanceMinimo = 10;
                            break;
                        case "PLUMBING":
                            lanceMinimo = 20;
                            break;
                    }
                    this.limitesMinimos.put(auctionType, lanceMinimo);
                }
                break;
            }
            case "Empresa4":
            case "Empresa5":
            {
                for (String auctionType : auctionTypes)
                {
                    this.limitesMinimos.put(auctionType, 5);
                }
                this.restricaoEspecifica = true;

                break;
            }
        }
    }
}
