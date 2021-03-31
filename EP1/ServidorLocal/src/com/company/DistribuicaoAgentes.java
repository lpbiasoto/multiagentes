package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DistribuicaoAgentes {

    //mover os agentes entre as salas, garantindo que cada agente esteja em uma sala de cada vez
    //registrar as salas inicialmente atribuídas a cada agente
    //garantir os limites de ocupação de sala

    public DistribuicaoAgentes() throws Exception
    {
        while (!this.CondicaoFinal())
        {
            List<Agente> listaAgentesMudanca = ListarAgentesMudanca();
            boolean cenarioValido = false;

            System.out.println("########## Iniciando a realocação dos agentes ##########");
            System.out.println("Sorteio de cenário válido para realocação dos agentes.");
            int i=1;
            while (!cenarioValido) {
                System.out.println(i+"º cenário:");
                SortearSalasDestino(listaAgentesMudanca);
                cenarioValido = ValidarCenarioSorteado(listaAgentesMudanca);
                System.out.println("Cenário válido? "+ ((cenarioValido) ? "Sim." : "Não."));
                i++;
            }
            System.out.println("Cenários investigados: "+ i+".");
            RealocarAgentes(listaAgentesMudanca);
        }
        System.out.println("########## Finalizando a realocação dos agentes ##########");
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

            //System.out.println("Agente "+agente.nome+" de ID "+agente.id+": ID sala inicial "+agente.idSalaInicial+" | ID sala destino "+agente.idSalaDestino);
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

            System.out.println("Agentes na sala de ID "+id+": "+freq);
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
            for (Agente agente : listaAgentesMudanca) {
                boolean podeRemover = agente.salaAtual.listaAgentes.size() >= 3;
                boolean podeAlocar = false;
                Optional<Sala> salaDestino = Mundo.listaSalasMundo.stream().filter(t -> t.id == agente.idSalaDestino).findFirst();
                if (salaDestino.isPresent()) {
                    podeAlocar = salaDestino.get().listaAgentes.size() <= 3;
                }
                if (podeRemover && podeAlocar) {
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
