package com.lpbiasoto;

import com.lpbiasoto.interfacehecate.InterfaceHecate;
import com.lpbiasoto.interfacehecate.JSONSerializer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public class MonitoramentoMsgs {

    public MonitoramentoMsgs() throws Exception {
        List<Mensagem> listaMensagens = ListarMensagens();
        PersistirMensagens(listaMensagens);
    }

    public List<Mensagem> ListarMensagens() throws Exception {
        Logging.Log(Util.EnumLog.INFO,"########## Listando e armazenando mensagens ##########");
        List<Mensagem> listaMensagens = new ArrayList<Mensagem>();
        List<Mensagem> listaMensagensServidor = new ArrayList<Mensagem>();

        listaMensagensServidor = RecuperarMensagens();
        listaMensagensServidor.sort(((o1, o2) -> o1.timestamp.compareTo(o2.timestamp)));

        Logging.Log(Util.EnumLog.INFO,"Número total de mensagens enviadas: "+listaMensagensServidor.size());
        return listaMensagensServidor;
    }

    private List<Mensagem> RecuperarMensagens() throws Exception {
        List<Mensagem> listaMensagensServidor = new ArrayList<>();
        for (Agente agente : Mundo.listaAgentesMundo) {
            String retorno = InterfaceHecate.GETConnection("/agent/messages", Optional.of("/"+agente.id));
            listaMensagensServidor.addAll(JSONSerializer.DesserializarListaMensagens(retorno));
        }
        List<Mensagem> listaMensagensUnicasServidor = listaMensagensServidor.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(Mensagem::getConteudo))), ArrayList::new));
        return listaMensagensUnicasServidor;
    }

    public void PersistirMensagens(List<Mensagem> listaMensagens) throws IOException {
        Logging.Log(Util.EnumLog.INFO,"Gerando arquivo com mensagens enviadas...");
        FileWriter writer = new FileWriter("mensagens.txt");
        for(String str: listaMensagens.stream().map(x->x.conteudo).collect((Collectors.toList()))) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
        Logging.Log(Util.EnumLog.INFO,"Arquivo criado no diretório: pcs5703-2020-exercicio-pratico-2-lpbiasoto/mensagens.txt");
        Logging.Log(Util.EnumLog.INFO,"########## Finalizando módulo de mensagens ##########");
    }

}
