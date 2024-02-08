package grapevin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) // Exclude DataSourceAutoConfiguration
public class Main {
    public static void main( String[] args ) throws SQLException, ClassNotFoundException {
        SQLConnection conn = new SQLConnection("jdbc:mysql://localhost:3306/winedb?user=root&password=Foolcavetown201:3306/winedb", 
                                            "root", 
                                            "Foolcavetown201");
        SpringApplication.run(Main.class, args);
        Questionnaire questionnaire = new Questionnaire();
        List<String> userResponses = questionnaire.collectUserResponses();
        String request1 = "Find 5 wines that are: " + userResponses.get(1) + "and " + userResponses.get(3)
            + ".Please display them ONLY like this: name, color, flavor.";

        // Print user responses
        System.out.println("User Responses: " + userResponses);

        String winetable = conn.getStringTable(userResponses.get(3));
        String request2 = "Please select 3 of the wines below that go best with " + userResponses.get(0)
                + ", are ideal " + userResponses.get(2)
                + " and are best for " + userResponses.get(4) + "experienses."
                + "Please provide the response with thw following stucture: 1. name1\\n2. name2 \\n3. name3 etc." 
                    + "Here are the wines:" + winetable;

        try {
            // Use GPT-3.5 to analyze responses and get wine recommendations
            ArrayList<String> gptResponse1 = TestAPI.getWineRecommendations(request1);
            // Display the recommended wines to the user
            System.out.println("Recommended Wines:");
            System.out.println(gptResponse1);

            // Save the wines in DB
            conn.saveWinesInDB(gptResponse1);
            System.out.println("All good");

            // Call GPT again to select the final wines
            ArrayList<String> gptResponse2 = TestAPI.getWineRecommendations(request2);
            System.out.println(gptResponse2);
            System.out.println(gptResponse2.get(0));

            // Close the connection when done
            conn.closeConection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
        
