package grapevin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Questionnaire {

    private List<String> questions;
    private Map<String, List<String>> choices;
    private List<String> answers;

    public Questionnaire() {
        questions = new ArrayList<>();
        choices = new HashMap<>();
        answers = new ArrayList<>();
        initializeQuestions();
        initializeChoices();
        initializeAnswers();
    }

    // Method to initialize questions
    private void initializeQuestions() {
        questions.add("Which meal do you prefer?");
        questions.add("Which flavor do you mostly prefer?");
        questions.add("In which occasions do you enjoy wine the most?");
        questions.add("Do you prefer red, white, or rose wines?");
        questions.add("What is your favorite wine experience?");
    }

    // Method to initialize choices
    private void initializeChoices() {
        choices.put("Which meal do you prefer?", List.of("Chicken", "Red meat", "A plate of cheeses"));
        choices.put("Which flavor do you mostly prefer?", List.of("Fruity", "Dry", "Very sweet"));
        choices.put("In which occasions do you enjoy wine the most?", List.of("During meals", "During social gatherings", "During relaxation"));
        choices.put("Do you prefer red, white, or rose wines?", List.of("Red", "White", "Rose"));
        choices.put("What is your favorite wine experience?", List.of("Summer vacations", "Social evening", "Romantic"));
    }

    // Method to initialize answers
    private void initializeAnswers() {
        answers.add("I choose a white wine that pairs well with chicken and salad.");
        answers.add("I prefer wines with intense fruity aroma, such as Pinot Noir.");
        answers.add("I enjoy wine mainly during social evenings.");
        answers.add("I prefer white wines.");
        answers.add("My favorite wine experience was during summer vacations in Napa.");
    }

    // Getter for questions
    public List<String> getQuestions() {
        return questions;
    }

    // Getter for choices
    public Map<String, List<String>> getChoices() {
        return choices;
    }

    // Getter for answers
    public List<String> getAnswers() {
        return answers;
    }

    public List<String> collectUserResponses(Scanner scanner) {
        List<String> userResponses = new ArrayList<>();
        for (String question : questions) {
            List<String> options = choices.get(question);
            while (true) {
                System.out.println(question);
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i));
                }

                System.out.print("Enter your choice (1-" + options.size() + "): ");
                int choice = scanner.nextInt();

                if (choice >= 1 && choice <= options.size()) {
                    // Valid choice, add to userResponses and break out of the loop
                    userResponses.add(options.get(choice - 1));
                    break;
                } else {
                    // Invalid choice, prompt the user again
                    System.out.println("Invalid choice. Please enter a number between 1 - " + options.size() + ".");
                }
            }
        }
        return userResponses;
    }
}
