package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MonitoramentoMsgs {

    public MonitoramentoMsgs() throws IOException {
        List<Mensagem> listaMensagens = ListarMensagens();
        PersistirMensagens(listaMensagens);
    }

    public List<Mensagem> ListarMensagens(){
        System.out.println("########## Listando e armazenando mensagens ##########");
        List<Mensagem> listaMensagens = new ArrayList<Mensagem>();
        for (Sala sala : Mundo.listaSalasMundo)
        {
            listaMensagens.addAll(sala.listaMensagens);
        }

        listaMensagens.sort(((o1, o2) -> o1.timestamp.compareTo(o2.timestamp)));

        System.out.println("Número total de mensagens enviadas: "+listaMensagens.size());
        return listaMensagens;
    }

    public void PersistirMensagens(List<Mensagem> listaMensagens) throws IOException {

        System.out.println("Gerando arquivo com mensagens enviadas...");
        FileWriter writer = new FileWriter("mensagens.txt");
        for(String str: listaMensagens.stream().map(x->x.conteudo).collect((Collectors.toList()))) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
        System.out.println("Arquivo criado no diretório: pcs5703-2020-exercicio-pratico-1-lpbiasoto/mensagens.txt");
        System.out.println("########## Finalizando módulo de mensagens ##########");
    }

}
