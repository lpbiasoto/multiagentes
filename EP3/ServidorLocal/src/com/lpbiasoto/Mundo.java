package com.lpbiasoto;

import com.lpbiasoto.interfacehecate.InterfaceHecate;
import com.lpbiasoto.interfacehecate.JSONSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Mundo {

    static List<Agente> listaAgentesMundo;

    //static List<String> listaIdsAgentesGanhadores;
    //List<Lance> listaLances;

    public Mundo()
    {
        this.listaAgentesMundo = new ArrayList<Agente>();
    }

    public void Inicializar() throws Exception
    {
        Logging.Log(Util.EnumLog.INFO,"########## Inicializando o mundo ##########");
        CriarMundo();

        Logging.Log(Util.EnumLog.INFO,"Criando agentes...");
        CriarAgentes();
        DefinirEspecialidades();

        Logging.Log(Util.EnumLog.INFO,"Mundo criado com "+listaAgentesMundo.size()+" agentes.");// e "+listaLeiloesMundo.size()+" leilões.");
        Logging.Log(Util.EnumLog.INFO,"########## Finalizando inicialização do mundo ##########");

        Logging.Log(Util.EnumLog.INFO,"########## Iniciando o processo dos leilões ##########");

    }

    private void DefinirEspecialidades() throws Exception {
        for (Agente agente : listaAgentesMundo) {
            String strRetorno = InterfaceHecate.POSTConnection("/agent/setAuctionTypes", ((Integer) agente.id).toString(), JSONSerializer.SerializarAgente(agente));
        }
    }

    private void CriarAgentes() throws Exception {

        CriarAgente("Empresa1",
                Arrays.asList(Leilao.AuctionType.PLUMBING.toString(),
                            Leilao.AuctionType.INTERIOR.toString(),
                            Leilao.AuctionType.DOORS.toString()
                )
        );

        CriarAgente("Empresa2",
                Arrays.asList(
                        Leilao.AuctionType.SITE.toString(),
                        Leilao.AuctionType.WINDOWS.toString()
                )
        );

        CriarAgente("Empresa3",
                Arrays.asList(
                        Leilao.AuctionType.SITE.toString(),
                        Leilao.AuctionType.FLOORS.toString(),
                        Leilao.AuctionType.WALLS.toString(),
                        Leilao.AuctionType.ROOF.toString(),
                        Leilao.AuctionType.DOORS.toString(),
                        Leilao.AuctionType.PLUMBING.toString(),
                        Leilao.AuctionType.ELECTRICAL.toString(),
                        Leilao.AuctionType.EXTERIOR.toString()
                )
        );

        CriarAgente("Empresa4",
                Arrays.asList(
                        Leilao.AuctionType.SITE.toString(),
                        Leilao.AuctionType.FLOORS.toString(),
                        Leilao.AuctionType.WALLS.toString(),
                        Leilao.AuctionType.ROOF.toString(),
                        Leilao.AuctionType.DOORS.toString(),
                        Leilao.AuctionType.PLUMBING.toString(),
                        Leilao.AuctionType.ELECTRICAL.toString(),
                        Leilao.AuctionType.EXTERIOR.toString(),
                        Leilao.AuctionType.INTERIOR.toString()
                )
        );

        CriarAgente("Empresa5",
                Arrays.asList(
                        Leilao.AuctionType.SITE.toString(),
                        Leilao.AuctionType.FLOORS.toString(),
                        Leilao.AuctionType.WALLS.toString(),
                        Leilao.AuctionType.ROOF.toString(),
                        Leilao.AuctionType.WINDOWS.toString(),
                        Leilao.AuctionType.DOORS.toString(),
                        Leilao.AuctionType.PLUMBING.toString(),
                        Leilao.AuctionType.ELECTRICAL.toString(),
                        Leilao.AuctionType.EXTERIOR.toString(),
                        Leilao.AuctionType.INTERIOR.toString()
                )
        );
    }

    private void CriarAgente(String name, List<String> auctionTypes) throws Exception {
        Agente agente = new Agente(name, auctionTypes);
        listaAgentesMundo.add(agente);
        Logging.Log(Util.EnumLog.INFO,"Agente criado: "+agente.name+" de ID "+agente.id);
    }

    private void CriarMundo() throws Exception {
        InterfaceHecate.GETConnection("/createWorld", Optional.of(""));
    }

    public static Agente RetornarAgente(Integer idAgente)
    {
        return Mundo.listaAgentesMundo.stream().filter(t -> t.id == idAgente).findFirst().orElse(null);
    }

}
