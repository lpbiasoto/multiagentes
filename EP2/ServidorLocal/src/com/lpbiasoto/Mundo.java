package com.lpbiasoto;

import com.lpbiasoto.interfacehecate.InterfaceHecate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Mundo {

    static List<Sala> listaSalasMundo;
    static List<Agente> listaAgentesMundo;

    public Mundo() throws Exception
    {
        Logging.Log(Util.EnumLog.INFO,"########## Inicializando o mundo ##########");

        Inicializar();

        Logging.Log(Util.EnumLog.INFO,"Criando salas...");
        CriarSalas();

        Logging.Log(Util.EnumLog.INFO,"Criando agentes e alocando-os nas salas iniciais...");
        CriarAgentes();

        Logging.Log(Util.EnumLog.INFO,"Mundo criado com "+listaSalasMundo.size()+" salas e "+listaAgentesMundo.size()+" agentes.");
        Logging.Log(Util.EnumLog.INFO,"########## Finalizando inicialização do mundo ##########");
    }

    private void CriarSalas() throws Exception {
        for (int i = 0; i < 5; i++) {
            Sala sala = new Sala("Sala" + (i+1));
            listaSalasMundo.add(sala);
            Logging.Log(Util.EnumLog.INFO,"Sala criada: "+sala.nome+" de ID "+sala.id);
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
                Logging.Log(Util.EnumLog.INFO,"Agente criado: "+agente.nome+" de ID "+agente.id+"; Alocado inicialmente na sala: "+sala.nome);
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
