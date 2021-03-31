package com.company.interfacehecate;
import com.company.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class InterfaceHecate {

    static String host = "http://localhost:8080";

    public static String GETConnection(String command, Optional<String> Optparams) throws Exception {
        final String GET_COMMAND = command;
        //System.out.println(GET_COMMAND);

        final String GET_PARAMS = Optparams.isPresent() ? Optparams.get() : "";
        //System.out.println(GET_PARAMS);

        StringBuilder URL_STR = new StringBuilder();

        URL_STR.append(host).append(GET_COMMAND).append(GET_PARAMS);

        URL obj = new URL(URL_STR.toString());

        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("GET");

        int responseCode = postConnection.getResponseCode();
        //System.out.println("GET Response Code :  " + responseCode);
        //System.out.println("GET Response Message : " + postConnection.getResponseMessage());

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    postConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            //System.out.println(response.toString());

            return response.toString();
        } else {
            System.out.println("ERRO NA CHAMADA DA API!");
            return "ERRO";
        }
    }
}
