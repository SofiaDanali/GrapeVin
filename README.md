# GrapeVin – Wine Recommendation System

## General Idea
The "GrapeVin" application is a comprehensive web application aiming to recommend wine labels to users based on their personal preferences. Specifically, utilizing the GPT-3.5 artificial intelligence model, wines are selected to match the responses of a questionnaire that each user is asked to complete. This questionnaire can be filled out multiple times, giving users the opportunity to explore the various recommendations provided by the application.
The following features are provided:
- Account Registration & Login
- Update Personal Profile (Name & Surname)
- Complete/Update Questionnaire
- View Application Recommendations

## Execution Environment
The GrapeVin application operates using any integrated development environment (IDE) such as Visual Studio Code. For the database, any MySQL database can be used. 

## Testing Procedure
To test the application on a local system, the following basic steps need to be followed:
1. Database Creation: Create a MySQL database with which GrapeVin will interact. The name of this database can be arbitrary, but we suggest the existing name. You will find the "winedb" file containing the query for managing the tables. Create the tables by executing the CREATE commands.
2. Program Configuration: GrapeVin requires both connection to the database and an API key for using GPT-3.5. The appropriate details should be filled in the Main and TestAPI classes where specified.
   For creating an API key, refer here: [OpenAI API](https://openai.com/blog/openai-api)
3. Compilation of the application: The command from the terminal for compiling the application is mvn package. You may need to use mvn clean package if there are any changes to the code.
4. Execution of the program: Once the project is saved and compiled, the execution command is java -jar target/gen5-1.0-SNAPSHOT.jar or f5 in an IDE environment.

## UML Diagram
![UML Diagram](./uml_class_diagram_final.png)

