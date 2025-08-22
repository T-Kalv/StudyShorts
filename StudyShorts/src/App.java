// Program: App.java
// Author: T-Kalv
// Module:
// Email:
// Student Number:
// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// About:
// A Study Quiz app that allows users to create questions and study those questions engaging with those questions via short form video content at the bottom of the screen

// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// Requirements
// - Implement a MySQL database using MySQL workbench to create a table that has the relevant tables for the quiz such as the module/category, questions and answers with the relevant primary keys, foreign keys, data types
// - Implement basic java swing main menu application that allows the user to either create a new question or study question or exit the app
// - Implement basic java swing application that is used to create a new question where the user enters their question, the module and the options with the correct answer selected and connect that to the MuSQL database to insert the new question to db
// - Implement basic java swing app that user can use to study questions that they selected under module showing the question with options and score getting the data from the db
// - Implement a simple java fx embedded video player on the screen to show the short form video contnt while user is using app

// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// Code
import database.Module;
import screens.CreateNewQuestionsGUI;
import screens.StudyGUIScreen;
import screens.TitleGUIScreen;
import javafx.application.Platform;

import javax.swing.*;

public class App
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Platform.startup(() -> {});

                SwingUtilities.invokeLater(() -> {
                    new TitleGUIScreen().setVisible(true);
                });
                new TitleGUIScreen().setVisible(true);
                //new CreateNewQuestionsGUI().setVisible(true);
               //new StudyGUIScreen(new Module(1, "Test"), 5).setVisible(true);
            }
        });
    }
}
