package com.lpbiasoto;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DistribuicaoAgentes {

    //mover os agentes entre as salas, garantindo que cada agente esteja em uma sala de cada vez
    //registrar as salas inicialmente atribuídas a cada agente
    //garantir os limites de ocupação de sala
    private static AtomicInteger nextId = new AtomicInteger(0);

    private static int getNextId() {
        return nextId.incrementAndGet();
    }

    public DistribuicaoAgentes() throws Exception
    {
        while (!this.CondicaoFinal())
        {
            List<Agente> listaAgentesMudanca = ListarAgentesMudanca();
            boolean cenarioValido = false;

            Logging.Log(Util.EnumLog.INFO,"########## Iniciando a realocação dos agentes ##########");
            Logging.Log(Util.EnumLog.INFO,"Sorteio de cenário válido para realocação dos agentes.");

            int i=1;
            while (!cenarioValido) {
                Logging.Log(Util.EnumLog.INFO,i+"º cenário:");
                SortearSalasDestino(listaAgentesMudanca);
                cenarioValido = ValidarCenarioSorteado(listaAgentesMudanca);
                Logging.Log(Util.EnumLog.INFO,"Cenário válido? "+ ((cenarioValido) ? "Sim." : "Não."));
                i++;
            }
            Logging.Log(Util.EnumLog.INFO,"Cenários investigados: "+ i+".");
            RealocarAgentes(listaAgentesMudanca);
            Planejamento.ExecutarPlanos();
        }
        Logging.Log(Util.EnumLog.INFO,"########## Finalizando a realocação dos agentes ##########");
    }

    class SortbyQtdAgentes implements Comparator<Agente>
    {
        public int compare(Agente a, Agente b)
        {
            return a.salaAtual.listaAgentes.size() - b.salaAtual.listaAgentes.size();
        }
    }

    private List<Agente> ListarAgentesMudanca()
    {
        return Mundo.listaAgentesMundo.stream().filter(t -> !t.estadoSalaNova).collect(Collectors.toList());
    }

    private void SortearSalasDestino(List<Agente> listaAgentesMudanca)
    {
        List<Integer> listaSalaIDMundo = Mundo.listaSalasMundo.stream().map(x->x.getID()).collect((Collectors.toList()));

        for (Agente agente : listaAgentesMudanca)
        {
            int salaSorteada = -1;
            while (salaSorteada == agente.idSalaInicial || salaSorteada == -1)
            {
                salaSorteada = Util.randInt(Collections.min(listaSalaIDMundo), Collections.max(listaSalaIDMundo));
            }
            agente.idSalaDestino = salaSorteada;
        }
    }

    private boolean ValidarCenarioSorteado(List<Agente> listaAgentesMudanca)
    {
        List<Integer> listaSalaIDDestino = listaAgentesMudanca.stream().map(x->x.idSalaDestino).collect((Collectors.toList()));
        List<Integer> listaSalaIDMundo = Mundo.listaSalasMundo.stream().map(x->x.getID()).collect((Collectors.toList()));
        List<Long> listaFrequencias = new ArrayList<>();
        for (Integer id : listaSalaIDMundo)
        {
            long freq = listaSalaIDDestino.stream().filter(t -> t.equals(id)).count();
            listaFrequencias.add(freq);
            Logging.Log(Util.EnumLog.INFO,"Agentes na sala de ID "+id+": "+freq);
        }

        if (listaFrequencias.stream().filter(t -> t == 3).count() == 1 &&
                listaFrequencias.stream().noneMatch(t -> t > 4) &&
                listaFrequencias.stream().noneMatch(t -> t < 2))
        {
            return true;
        }
        else return false;
    }

    private void RealocarAgentes(List<Agente> listaAgentesMudanca) throws Exception
    {
        while (listaAgentesMudanca.size()>0) {
            Collections.sort(listaAgentesMudanca, new SortbyQtdAgentes());
            for (Agente agente : listaAgentesMudanca) {
                boolean podeRemover = agente.salaAtual.listaAgentes.size() >= 3;
                boolean podeAlocar = false;
                Optional<Sala> salaDestino = Mundo.listaSalasMundo.stream().filter(t -> t.id == agente.idSalaDestino).findFirst();
                if (salaDestino.isPresent()) {
                    podeAlocar = salaDestino.get().listaAgentes.size() <= 3;
                }
                if (podeRemover && podeAlocar) {
                    agente.planejamentoExecucao.idPlano = getNextId();
                    agente.TrocarSala(agente.salaAtual, salaDestino.get());
                }
            }
            listaAgentesMudanca = listaAgentesMudanca.stream().filter(t -> !t.estadoSalaNova).collect(Collectors.toList());
        }
    }

    private boolean CondicaoFinal()
    {
        //Cada agente deverá se mover para uma nova sala, diferente da sala a que foi originalmente atribuído
        //Todos os agentes devem mudar de sala
        boolean validacao1;
        //se tiver ao menos um agente na sala inicial, validacao1 será false.
        validacao1 = Mundo.listaAgentesMundo.stream().allMatch(t -> t.estadoSalaNova);

        //Uma sala deve possuir 3 agentes. O restante das salas deve possuir 2 ou 4 agentes.
        boolean validacao2;
        int sala3agentes = 0;
        int sala2ou4agentes = 0;

        for(Sala sala : Mundo.listaSalasMundo)
        {
            if (sala.listaAgentes.size() == 3)
            {
                sala3agentes = sala3agentes + 1;
            }
            else if (sala.listaAgentes.size() == 2 || sala.listaAgentes.size() == 4)
            {
                sala2ou4agentes = sala2ou4agentes + 1;
            }
        }
        validacao2 = (sala3agentes==1 && (sala3agentes+sala2ou4agentes== Mundo.listaSalasMundo.size()));

        return validacao1&validacao2;
    }
}
