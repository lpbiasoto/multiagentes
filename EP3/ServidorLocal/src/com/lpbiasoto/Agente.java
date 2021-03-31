package com.lpbiasoto;

import com.google.gson.annotations.SerializedName;
import com.lpbiasoto.interfacehecate.InterfaceHecate;
import com.lpbiasoto.interfacehecate.JSONSerializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Agente
{
    @SerializedName("id")
    Integer id;
    String name;

    @SerializedName("auctionTypes")
    private List<String> auctionTypes;
    private List<Lance> winningBids;
    private BigDecimal iniciativa;

    private Estrategia estrategia;
    private List<Lance> lances;

    public Agente(String nomeAgente, List<String> auctionTypes) throws Exception
    {
        this.id = this.CriarAgente(nomeAgente);
        this.name = nomeAgente;
        this.auctionTypes = auctionTypes;
        this.winningBids = new ArrayList<Lance>();
        this.iniciativa = new BigDecimal(0.000).setScale(3, RoundingMode.HALF_EVEN);
        this.estrategia = new Estrategia(this, auctionTypes);
        this.lances = new ArrayList<Lance>();
    }

    private int CriarAgente(String nomeAgente) throws Exception
    {
        String strRetorno = InterfaceHecate.GETConnection("/createAgent", Optional.of("/?name="+nomeAgente));
        int id = 0;
        if (strRetorno != "ERRO")
        {
            Retorno retorno = JSONSerializer.DesserializarRetorno(strRetorno);
            id = retorno.id;
        }
        return id;
    }

    public String GetAgentName(List<Agente> agents){
        String name = "";
        for (Agente agent : agents) {
            if (agent.id == id) name = agent.name;
        }
        return name;
    }

    public int ObterValorMinimo(String tipoLeilao) {
        return this.estrategia.limitesMinimos.get(tipoLeilao);
    }

    public void AtualizarIniciativa(BigDecimal iniciativaAtualizada) {
        this.iniciativa = iniciativaAtualizada;
    }

    public Lance DoBid(Leilao leilaoAtual) throws Exception {
        String auctionType = leilaoAtual.auctionType.name();
        Logging.Log(Util.EnumLog.INFO,"Lance do agente: " + this.name);

        int bidValue = 0;
        int max_possible_bid = leilaoAtual.maximumValue;
        int actual_bestBid = leilaoAtual.bestBid;

        if (this.RestricoesEspecificasValidas())
        {
            if (actual_bestBid == 0 && max_possible_bid - 10 > 0)
            {
                bidValue = Util.ObterMaior(max_possible_bid - 10, this.estrategia.limitesMinimos.get(auctionType));
            }
            else if (actual_bestBid == 0)
            {
                bidValue = Util.ObterMaior(max_possible_bid - 5, this.estrategia.limitesMinimos.get(auctionType));
            }
            else if (actual_bestBid > 5)
            {
                bidValue = Util.ObterMaior(actual_bestBid - 5, this.estrategia.limitesMinimos.get(auctionType));
            }
            else
            {
                bidValue = Util.ObterMaior(5, this.estrategia.limitesMinimos.get(auctionType));
            }
        }
        else bidValue = max_possible_bid;

        if (this.name == "Empresa5" && this.winningBids.stream().map(x->x.bidValue)
                .reduce(0,Integer::sum) + bidValue > 80)
        {
            bidValue = max_possible_bid;
        }

        Lance lance = new Lance(this.id,auctionType,bidValue);
        String info_bid = InterfaceHecate.POSTConnection("/placeBid", "", JSONSerializer.SerializarLance(lance));

        this.lances.add(lance);
        return lance;
    }


    public boolean RestricoesEspecificasValidas(){
        boolean valido = false;
        switch (this.name){
            case "Empresa1":
            case "Empresa2":
            case "Empresa3":
                valido = true;
                break;
            case "Empresa4":
                valido = this.winningBids.size() < 4;
                break;
            case "Empresa5":
                valido = this.winningBids.stream().map(x->x.bidValue)
                        .reduce(0,Integer::sum) <= 80;
                break;
        }
        return valido;
    }

    private int SomarLancesGanhadores() {
        return this.winningBids.stream().map(x->x.bidValue)
                .reduce(0,Integer::sum);
    }

    public static List<Agente> ObterAgentesParticipantes(String auctionType) {
        return Mundo.listaAgentesMundo.stream().filter(t -> t.auctionTypes.contains(auctionType)).collect(Collectors.toList());
    }

    private int QtdLeiloesParticipantes(){
        return this.auctionTypes.size();
    }



    public void AtualizarIniciativa(List<Leilao> listaLeiloes)
    {
        BigDecimal iniciativaAtualizada;
        int somaValoresMaximos = 0;
        int somaValoresMaximosEncerrados = 0;
        int somaValoresGanhos = 0;
        int somaValoresMinimos = 0;
        int leiloesTotais = listaLeiloes.size();
        int leiloesAgente = this.QtdLeiloesParticipantes();

        for (String tipoLeilao : this.auctionTypes) {
            List<Leilao> leiloesNaoFinalizados = listaLeiloes.stream().filter(t -> (t.auctionType.name()
                    .contains(tipoLeilao))&&(!t.encerrado)).collect(Collectors.toList());
            somaValoresMaximos = somaValoresMaximos + leiloesNaoFinalizados.stream().map(x -> x.maximumValue)
                    .reduce(0, Integer::sum);

            somaValoresMaximosEncerrados = somaValoresMaximosEncerrados + listaLeiloes.stream()
                    .filter(t -> (t.auctionType.name().contains(tipoLeilao))&&(t.encerrado)).map(x -> x.maximumValue)
                    .reduce(0, Integer::sum);

            if (leiloesNaoFinalizados.size()>0) {
                for (Leilao leilao : leiloesNaoFinalizados) {
                    somaValoresMinimos = somaValoresMinimos + this.ObterValorMinimo(tipoLeilao);
                }
            }
        }

        somaValoresGanhos = this.SomarLancesGanhadores();

        if (somaValoresMaximos == 0) somaValoresMaximos = 1;
        if (somaValoresMaximosEncerrados == 0) somaValoresMaximosEncerrados = 1;

        BigDecimal bdsomaValoresMinimos = new BigDecimal(somaValoresMinimos).setScale(3);
        BigDecimal bdsomaValoresMaximos = new BigDecimal(somaValoresMaximos).setScale(3);
        BigDecimal bdsomaValoresMaximosEncerrados = new BigDecimal(somaValoresMaximosEncerrados).setScale(3);
        BigDecimal bdsomaValoresGanhos = new BigDecimal(somaValoresGanhos).setScale(3);
        BigDecimal bdleiloesTotais = new BigDecimal(leiloesTotais).setScale(3);
        BigDecimal bdleiloesAgente = new BigDecimal(leiloesAgente).setScale(3);


        iniciativaAtualizada = new BigDecimal(String.valueOf(
                (new BigDecimal(1.000).subtract(bdsomaValoresMinimos.divide(bdsomaValoresMaximos, RoundingMode.HALF_UP)))
                .multiply(bdleiloesAgente.divide(bdleiloesTotais, RoundingMode.HALF_UP))
                .divide(new BigDecimal(1.000).subtract(bdsomaValoresGanhos
                        .divide(bdsomaValoresMaximosEncerrados, RoundingMode.HALF_UP)),RoundingMode.HALF_UP)))
                .setScale(3, RoundingMode.HALF_EVEN);

        this.AtualizarIniciativa(iniciativaAtualizada);
    }

    public void AdicionarLanceVencedor(Lance lance_vencedor) {
        this.winningBids.add(lance_vencedor);
    }

    public int getIniciativa(){
        BigDecimal multiplicador = new BigDecimal(1000).setScale(3);
        String numero = this.iniciativa.multiply(multiplicador).toString();
        String[] numero_split = numero.split(Pattern.quote("."));
        return Integer.parseInt(numero_split[0]);
    }
}

