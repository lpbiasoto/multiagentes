package com.company;

import com.company.interfacehecate.InterfaceHecate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Mundo {

    static List<Sala> listaSalasMundo;
    static List<Agente> listaAgentesMundo;

    public Mundo() throws Exception
    {
        System.out.println("########## Inicializando o mundo ##########");

        Inicializar();

        System.out.println("Criando salas...");
        CriarSalas();
        System.out.println("Criando agentes e alocando-os nas salas iniciais...");
        CriarAgentes();

        System.out.println("Mundo criado com "+listaSalasMundo.size()+" salas e "+listaAgentesMundo.size()+" agentes.");
        System.out.println("########## Finalizando inicialização do mundo ##########");
    }

    private void CriarSalas() throws Exception {
        for (int i = 0; i < 5; i++) {
            Sala sala = new Sala("Sala" + (i+1));
            listaSalasMundo.add(sala);
            System.out.println("Sala criada: "+sala.nome+" de ID "+sala.id);
        }
    }

    private void CriarAgentes() throws Exception {
        int i = 0;
        for(Sala sala : listaSalasMundo)
        {
            while (sala.listaAgentes.size() < 3)
            {
                Agente agente = new Agente("Agente"+(i+1), sala);
                //sala.listaAgentes.add(agente);
                listaAgentesMundo.add(agente);
                i++;
                System.out.println("Agente criado: "+agente.nome+" de ID "+agente.id+"; Alocado inicialmente na sala: "+sala.nome);
            }
        }
    }

    private void Inicializar() throws Exception
    {
        InterfaceHecate.GETConnection("/createWorld", Optional.of(""));
        listaSalasMundo = new ArrayList<Sala>();
        listaAgentesMundo = new ArrayList<Agente>();
    }

    private void TestarMundo() throws Exception
    {
        InterfaceHecate.GETConnection("/createWorld", Optional.of("/test"));
    }
}
