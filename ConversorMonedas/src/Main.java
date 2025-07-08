import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String apiUrl = "https://v6.exchangerate-api.com/v6/268c9aaa3aaf0681b1b45725/latest/USD";
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ExchangeRateResponse rates = gson.fromJson(response.body(), ExchangeRateResponse.class);
            Map<String, Double> conversionRates = rates.getConversionRates();

            System.out.println("Monedas disponibles:");
            for (String moneda : conversionRates.keySet()) {
                System.out.print(moneda + " ");
            }

            System.out.print("\n\nIngrese divisa de origen: ");
            String origen = scanner.nextLine().toUpperCase();

            System.out.print("Ingrese divisa de destino: ");
            String destino = scanner.nextLine().toUpperCase();

            System.out.print("Ingrese monto a convertir: ");
            double monto = scanner.nextDouble();

            if (conversionRates.containsKey(origen) && conversionRates.containsKey(destino)) {
                double tasaOrigen = conversionRates.get(origen);
                double tasaDestino = conversionRates.get(destino);
                double convertido = monto / tasaOrigen * tasaDestino;

                System.out.printf("%.2f %s = %.2f %s%n", monto, origen, convertido, destino);
            } else {
                System.out.println("Una o ambas monedas no son válidas.");
            }

        } catch (Exception e) {
            System.out.println("Error al procesar: " + e.getMessage());
        }
    }
}
