package com.lpbiasoto;

import com.lpbiasoto.interfacehecate.InterfaceHecate;
import com.lpbiasoto.interfacehecate.JSONSerializer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GerenciadorLeiloes {

    static List<Leilao> listaLeiloes;
    static List<Leilao> listaLeiloesFinalizados;
    static Leilao leilaoAtual;
    static List<Lance> lancesEnviados;


    public GerenciadorLeiloes() throws Exception {
        listaLeiloes = new ArrayList<Leilao>();
        listaLeiloesFinalizados = new ArrayList<Leilao>();
        leilaoAtual = new Leilao();
        lancesEnviados = new ArrayList<Lance>();
    }

    public void PersistirLeiloes() throws Exception {
        String strRetorno = InterfaceHecate.GETConnection("/maxAuctionValues", Optional.of(""));

        LimitesLances retorno = new LimitesLances();
        if (strRetorno != "ERRO")
        {
            retorno = JSONSerializer.DesserializarMaxAuctionValues(strRetorno);
        }

        int limiteMaximo = 0;
        for (Leilao.AuctionType tipoLeilao : Leilao.AuctionType.values())
        {
            switch (tipoLeilao){
                case FLOORS:
                    limiteMaximo = retorno.FLOORS;
                    break;
                case SITE:
                    limiteMaximo = retorno.SITE;
                    break;
                case WALLS:
                    limiteMaximo = retorno.WALLS;
                    break;
                case EXTERIOR:
                    limiteMaximo = retorno.EXTERIOR;
                    break;
                case ROOF:
                    limiteMaximo = retorno.ROOF;
                    break;
                case ELECTRICAL:
                    limiteMaximo = retorno.ELECTRICAL;
                    break;
                case INTERIOR:
                    limiteMaximo = retorno.INTERIOR;
                    break;
                case DOORS:
                    limiteMaximo = retorno.DOORS;
                    break;
                case WINDOWS:
                    limiteMaximo = retorno.WINDOWS;
                    break;
                case PLUMBING:
                    limiteMaximo = retorno.PLUMBING;
                    break;
            }
            Leilao leilao = new Leilao(tipoLeilao,limiteMaximo);
            listaLeiloes.add(leilao);
        }
    }

    static class SortbyIniciativa implements Comparator<Agente>
    {
        public int compare(Agente a, Agente b)
        {
            int iniciativaA = a.getIniciativa();
            int iniciativaB = b.getIniciativa();
            return iniciativaA - iniciativaB;
        }
    }

    public void GerenciarLeiloes() throws Exception {

        while (ExisteLeilaoAtivo()) {
            AtualizarLeiloesEncerrados();

            List<Agente> agentes_participantes = Agente.ObterAgentesParticipantes(leilaoAtual.auctionType.name());
            List<Lance> lances_leilao_atual;

            for (Agente agente : agentes_participantes) {
                agente.AtualizarIniciativa(listaLeiloes);
            }

            Collections.sort(agentes_participantes, new SortbyIniciativa());

            lances_leilao_atual = ProcessarLeilao(agentes_participantes);
            leilaoAtual.AtualizaVencedor(leilaoAtual.auctionType, lances_leilao_atual);
            Logging.Log(Util.EnumLog.INFO, "_________________________________________________________________________");

        }

        AtualizarLeiloesEncerrados();
        Logging.Log(Util.EnumLog.INFO, "... leiloes encerrados...");
    }

    private List<Lance> ProcessarLeilao(List<Agente> remainingAgents) throws Exception {

        Logging.Log(Util.EnumLog.INFO,"***Leilao sendo executado:"+leilaoAtual.auctionType);

        Lance lance_agente_atual;

        List<Lance> lances = new ArrayList<Lance>();

        for (Agente agente : remainingAgents){
            AtualizarLeilaoAtual();
            lance_agente_atual = agente.DoBid(leilaoAtual);
            lancesEnviados.add(lance_agente_atual);
            if (lance_agente_atual.bidValue < leilaoAtual.bestBid && lance_agente_atual.bidValue > 0) leilaoAtual.bestBid = lance_agente_atual.bidValue;
            lances.add(lance_agente_atual);
            Logging.Log(Util.EnumLog.INFO,"...valor lance:"+lance_agente_atual.bidValue);
        }

        return lances;
    }

    private void AtualizarLeiloesEncerrados() throws Exception {
        String info_leiloes_encerrados = InterfaceHecate.GETConnection("/finishedAuctions", Optional.of(""));
        List<Leilao> retorno_leiloes_encerrados = JSONSerializer.DesserializarListaLeiloes(info_leiloes_encerrados);
        listaLeiloesFinalizados = retorno_leiloes_encerrados;

        for (Leilao leilao : listaLeiloes)
        {
            boolean finalizado = false;
            List<Leilao> leilaoFinalizado = listaLeiloesFinalizados.stream().filter(t -> (t.auctionType.name().contains(leilao.auctionType.name()))).collect(Collectors.toList());
            finalizado = leilaoFinalizado.size() > 0;
            if (finalizado) {
                leilao.encerrado = true;
            }
        }

    }

    private boolean ExisteLeilaoAtivo() throws Exception {
        AtualizarLeilaoAtual();

        return leilaoAtual.maximumValue > 0;
    }

    private void AtualizarLeilaoAtual() throws Exception {
        String info_leilao_andamento = InterfaceHecate.GETConnection("/currentAuction", Optional.of(""));
        Retorno retorno_leilao_andamento = JSONSerializer.DesserializarRetorno(info_leilao_andamento);

        leilaoAtual.maximumValue = retorno_leilao_andamento.maximumValue;
        leilaoAtual.auctionType = retorno_leilao_andamento.auctionType;
        leilaoAtual.auctionWinner = retorno_leilao_andamento.auctionWinner;
        leilaoAtual.bestBid = retorno_leilao_andamento.bestBid;

        String retorno = InterfaceHecate.GETConnection("/currentAuction", Optional.of(""));
        Retorno agentesParticipantes = JSONSerializer.DesserializarRetorno(info_leilao_andamento);

        leilaoAtual.agentesParticipantes = agentesParticipantes.agentes;
    }

    public void IniciarLeiloes() throws Exception {
        InterfaceHecate.GETConnection("/startAuctions", Optional.of(""));
    }


    public void ConsolidarResultados() throws IOException {
        int gastoGiacomo = 0;

        for (Leilao leilao : listaLeiloesFinalizados)
        {
            gastoGiacomo = gastoGiacomo + leilao.bestBid;
        }

        Logging.Log(Util.EnumLog.INFO,"Giacomo gastou "+gastoGiacomo+" em "+listaLeiloesFinalizados.size()+" leilões.");

        SalvarArquivoLances();
        SalvarArquivoLeiloes();
    }

    private void SalvarArquivoLances() throws IOException {
        Logging.Log(Util.EnumLog.INFO,"Gerando arquivo com lances...");
        FileWriter writer = new FileWriter("lances.txt");

        String lances = JSONSerializer.SerializarListaLances(lancesEnviados);
        writer.write(lances);
        writer.close();
        Logging.Log(Util.EnumLog.INFO,"Arquivo criado no diretório: pcs5703-2020-exercicio-pratico-3-g1/lances.txt");
        Logging.Log(Util.EnumLog.INFO,"########## Finalizando módulo de lances ##########");
    }
    private void SalvarArquivoLeiloes() throws IOException {
        Logging.Log(Util.EnumLog.INFO,"Gerando arquivo com leilões...");
        FileWriter writer = new FileWriter("leiloes.txt");

        String leiloes = JSONSerializer.SerializarListaLeiloes(listaLeiloesFinalizados);
        writer.write(leiloes);
        writer.close();
        Logging.Log(Util.EnumLog.INFO,"Arquivo criado no diretório: pcs5703-2020-exercicio-pratico-3-g1/leiloes.txt");
        Logging.Log(Util.EnumLog.INFO,"########## Finalizando módulo de leilões ##########");
    }
}
