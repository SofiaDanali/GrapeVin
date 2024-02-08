package grapevin;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;

public class TestAPI {
    private static String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static String apiKey = "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";

    public String getApiKey(){
        return apiKey;
    }

    public String getApiEndPoint(){
        return API_ENDPOINT;
    }

    public static ArrayList<String> getWineRecommendations(String input) throws Exception {

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_ENDPOINT);

        // Remove accents from request string
        String request = Normalizer.normalize(input, Normalizer.Form.NFKD);

        // Create JSON payload for GPT-3.5 API request
        String jsonPayload = "{ \"model\": \"gpt-3.5-turbo-1106\", \"messages\": ["
        + "{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},"
        + "{\"role\": \"user\", \"content\": \"" + request + "\"}"
        + "] }";

        // Set headers
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + apiKey);

        // Set the JSON payload
        httpPost.setEntity(new StringEntity(jsonPayload));

        // Execute the request
        HttpResponse response = httpClient.execute(httpPost);

        // Process the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        return parseGptResponse(result.toString());
    }
    
    // Parse the JSON response to extract the recommended wines
    private static ArrayList<String> parseGptResponse(String gptResponse) {
        ArrayList<String> recommendedWines = new ArrayList<>();
        
        // Extract the assistant's content from the JSON response
        String content = gptResponse.split("\"content\": \"")[1].split("\"")[0];

       // Check response stracture and get only the wine names
       if (content.contains(":")) {
            content = content.split(":")[1];
        }

        // Split the content into lines
        String[] lines = content.split("\\\\n");

        // Add each line to the ArrayList
        for (String line : lines) {
            // Check if empty
            if (line != "" && line != " ") {
                recommendedWines.add(line.replaceAll("[0-9.]", "").trim());
            }
        }

        return recommendedWines;
    }
}
