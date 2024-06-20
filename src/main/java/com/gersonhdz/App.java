package com.gersonhdz;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        String apiKey = "1220287cbbf4517397a1a07e";
        String filePath = "src//main//java//com//gersonhdz//monedas.json";
        // https://v6.exchangerate-api.com/v6/YOUR-API-KEY/pair/EUR/GBP/AMOUNT
        int opcion = 0;

        StringBuilder menuBuilder = new StringBuilder();
        String menuInicio = """
        |------------------------------------------------|\n
         Conversor de monedas ->:\n
          1). Convertir
          2). Salir\n
        Ingrese el número de la opción que desea realizar: """;

        // Leer el archivo JSON como una cadena
        String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));

        // Parsear la cadena JSON
        JSONArray monedas = new JSONArray(jsonString);

            // Iterar sobre cada objeto JSON en el array
            // Construir el menú con la lista de monedas
            menuBuilder.append("|------------------------------------------------|\n");
            menuBuilder.append("Conversor de monedas ->:\n\n");

            for (int i = 0; i < monedas.length(); i++) {
                JSONObject currency = monedas.getJSONObject(i);
                String currencyCode = currency.getString("CurrencyCode");             
                

                // Agregar cada línea al menú
                menuBuilder.append((i + 1) + "). " + currencyCode + "\n");                
            }            
            menuBuilder.append("Ingrese el número de TAG de la moneda base: ");

        String menuConvertir = menuBuilder.toString();
        

        try (Scanner scanner = new Scanner(System.in)) {
            while (opcion != 2) { 
                
                System.out.println(menuInicio);
                

                opcion = scanner.nextInt();
                

                // Imprimir el menú completo
                if (opcion == 1) {

                    System.out.println(menuConvertir);                    
                    Integer monedaBase = scanner.nextInt();
                    System.out.println("Ingrese el valor a cambiar:");
                    float valor = scanner.nextFloat();                    
                    System.out.println("Ingrese el número de TAG para la moneda de cambio");
                    Integer monedaCambio = scanner.nextInt();

                    JSONObject monedaBaseJson = monedas.getJSONObject(monedaBase-1);
                    String monedaBaseString = monedaBaseJson.getString("CurrencyCode");
                    

                    JSONObject monedaCambioJson  = monedas.getJSONObject(monedaCambio-1);
                    String monedaCambioString = monedaCambioJson.getString("CurrencyCode"); 
                    



                    // Petición API

                    String url = "https://v6.exchangerate-api.com/v6/"+apiKey+"/pair/"+monedaBaseString+"/"+ monedaCambioString+"/"+valor;                    

                    // Crear cliente y solicitud
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .build();

                    // Crear respuesta
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    JSONObject jsonResponse = new JSONObject(response.body());
                    double conversionResult = jsonResponse.getDouble("conversion_result");
                    System.out.println(valor +" "+monedaBaseString+ " -> " + conversionResult + " " + monedaCambioString);
                    
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
}
