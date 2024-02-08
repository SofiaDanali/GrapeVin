package grapevin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner sc = new Scanner(System.in);
        SQLConnection conn = new SQLConnection("jdbc:mysql://localhost:3306/winedb", 
                                            "root", 
                                            "Foolcavetown201");
        System.out.println("Welcome to ...");
        System.out.println("\r\n" + //
                        "█████▀███████████████████████████████████████████\r\n" + //
                        "█─▄▄▄▄█▄─▄▄▀██▀▄─██▄─▄▄─█▄─▄▄─█▄─█─▄█▄─▄█▄─▀█▄─▄█\r\n" + //
                        "█─██▄─██─▄─▄██─▀─███─▄▄▄██─▄█▀██▄▀▄███─███─█▄▀─██\r\n" + //
                        "▀▄▄▄▄▄▀▄▄▀▄▄▀▄▄▀▄▄▀▄▄▄▀▀▀▄▄▄▄▄▀▀▀▄▀▀▀▄▄▄▀▄▄▄▀▀▄▄▀");
        User user = new User();
        System.out.println("Do you have an account? Yes or No?"); // User registration or login
        String yesNo = sc.nextLine().toLowerCase();
        if (yesNo.contains("yes")) {            
            conn.login(user, sc);
        } else if (yesNo.contains("no")) {
            conn.register(user, sc);
        } else {
            System.out.println("Invalid input. Bye bye");
            System.exit(0);
        }
        Questionnaire questionnaire = new Questionnaire();
        List<String> userResponses = questionnaire.collectUserResponses(sc);
        String request1 = "Find 5 wines that are: " + userResponses.get(1) + "and " + userResponses.get(3)
            + ".Please display them ONLY like this: name, color, flavor.";

        // Print user responses
        System.out.println("User " + user.getUsername() + " Responses: " + userResponses);

        String winetable = conn.getStringTable(userResponses.get(3));
        String request2 = "Please select 3 of the wines below that go best with " 
                + userResponses.get(0)
                + ", are ideal " + userResponses.get(2)
                + " and are best for " + userResponses.get(4) + "experienses."
                + "Please provide the response with the following stucture: 1. name1\\n2. name2 \\n3. name3 etc." 
                    + "Here are the wines:" + winetable;

        try {
            // Use GPT-3.5 to analyze responses and get wine recommendations
            ArrayList<String> gptResponse1 = TestAPI.getWineRecommendations(request1);

            // Display the recommended wines
            /*System.out.println("Recommended Wines:"); // Check
            System.out.println(gptResponse1);*/

            // Save the wines in DB
            conn.saveWinesInDB(gptResponse1);
            /*System.out.println("All good");*/ // Check

            // Call GPT again to select the final wines
            ArrayList<String> gptResponse2 = TestAPI.getWineRecommendations(request2);
            System.out.println("GrapeVins recomendations:");
            System.out.println(gptResponse2.get(0));
            System.out.println(gptResponse2.get(1));
            System.out.println(gptResponse2.get(2));

            // Close the connection
            conn.closeConection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
