package com.lpbiasoto.interfacehecate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

        HttpURLConnection getConnection = (HttpURLConnection) obj.openConnection();
        getConnection.setRequestMethod("GET");

        int responseCode = getConnection.getResponseCode();
        //System.out.println("GET Response Code :  " + responseCode);
        //System.out.println("GET Response Message : " + postConnection.getResponseMessage());

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getConnection.getInputStream()));
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
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            System.out.println("ERRO NA CHAMADA DA API!");
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String retorno = response.toString();
            return "ERRO";
        }
    }


    public static String POSTConnection(String command, String idAgente, String JSON) throws Exception {
        final String POST_COMMAND = command;
        //System.out.println(POST_COMMAND);

        final String POST_PARAMS = idAgente != "" ? "/?id="+idAgente : "";
        //System.out.println(POST_PARAMS);

        StringBuilder URL_STR = new StringBuilder();

        URL_STR.append(host).append(POST_COMMAND).append(POST_PARAMS);

        URL obj = new URL(URL_STR.toString());

        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json; utf-8");

        postConnection.setDoOutput(true);

        String jsonInputString = JSON;

        try(OutputStream os = postConnection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

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

            return response.toString();
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    postConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            System.out.println("ERRO NA CHAMADA DA API!");
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            //System.out.println(response.toString());

            String retorno = response.toString();
            return "ERRO";
        }
    }
    
}
