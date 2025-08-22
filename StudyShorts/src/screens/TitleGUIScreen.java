// Program: TitleGUIScreen.java
// Author: T-Kalv
// Module:
// Email:
// Student Number:
// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// About:

// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// Requirements

// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// Code
package screens;

import javax.swing.*;
import java.awt.*;
import constants.*;
import database.JDBC;
import database.Module;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class TitleGUIScreen extends JFrame
{
    private JComboBox dropDownMenu;
    private JTextArea questionTypeInputTextField;

    public TitleGUIScreen()
    {
        super("StudyShorts");
        setSize(400, 735);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(variables.BLACK);
        addGUIScreenComponents();
    }

    public void addGUIScreenComponents()
    {
        //title
        JLabel titleLabel = new JLabel("StudyShorts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(0, 20, 390, 43);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(variables.WHITE);
        add(titleLabel);

        //select module
        JLabel selectModuleLabel = new JLabel("Select Module:");
        selectModuleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectModuleLabel.setBounds(0, 90, 400, 43);
        selectModuleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectModuleLabel.setForeground(variables.WHITE);
        selectModuleLabel.setBackground(variables.DARK_GREY);
        add(selectModuleLabel);

        //drop-down menu
        ArrayList<String> modulesList = JDBC.fetchModules();
        dropDownMenu = new JComboBox(modulesList.toArray());
        dropDownMenu.setBounds(20, 120, 337, 45);
        dropDownMenu.setForeground(variables.BLACK);
        add(dropDownMenu);

        //questions type
        JLabel questionsTypeLabel = new JLabel("Questions Type: ");
        questionsTypeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionsTypeLabel.setBounds(20, 190, 172, 20);
        questionsTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionsTypeLabel.setForeground(variables.WHITE);
        add(questionsTypeLabel);

        //question type input text field
        questionTypeInputTextField = new JTextArea("10");
        questionTypeInputTextField.setFont(new Font("Arial", Font.BOLD, 16));
        questionTypeInputTextField.setBounds(200, 190, 148, 26);
        questionTypeInputTextField.setForeground(variables.WHITE);
        questionTypeInputTextField.setBackground(variables.DARK_GREY);
        add(questionTypeInputTextField);

        //study
        JButton studyButton = new JButton("Study");
        studyButton.setFont(new Font("Arial", Font.BOLD, 16));
        studyButton.setBounds(65, 290, 262, 45);
        studyButton.setForeground(variables.GREEN);
        studyButton.setBackground(variables.BLACK);
        studyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(inputValidation())
                {
                    Module module = JDBC.fetchModule(dropDownMenu.getSelectedItem().toString());
                    if (module == null) return;
                    int questionNum = Integer.parseInt(questionTypeInputTextField.getText());
                    StudyGUIScreen studyGUIScreen = new StudyGUIScreen(module, questionNum);
                    TitleGUIScreen.this.dispose();
                    studyGUIScreen.setVisible(true);
                }
            }
        });
        add(studyButton);

        //create questions
        JButton createNewQuestionButton = new JButton("Create New Questions");
        createNewQuestionButton.setFont(new Font("Arial", Font.BOLD, 16));
        createNewQuestionButton.setBounds(65, 350, 262, 45);
        createNewQuestionButton.setForeground(variables.AMBER);
        createNewQuestionButton.setBackground(variables.BLACK);
        createNewQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateNewQuestionsGUI createNewQuestionsGUI = new CreateNewQuestionsGUI();
                createNewQuestionsGUI.setLocationRelativeTo(TitleGUIScreen.this);
                TitleGUIScreen.this.dispose();
                createNewQuestionsGUI.setVisible(true);
            }
        });
        add(createNewQuestionButton);

        //exit
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBounds(65, 420, 262, 45);
        exitButton.setForeground(variables.RED);
        exitButton.setBackground(variables.BLACK);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        add(exitButton);

        int videoPanelY = exitButton.getY() + exitButton.getHeight() + 10;
        int videoPanelHeight = getHeight() - videoPanelY - 10;
        JFXPanel videoPanel = new JFXPanel();
        videoPanel.setBounds(0, videoPanelY, getWidth(), videoPanelHeight);
        add(videoPanel);
        Platform.runLater(() -> initialiseFX(videoPanel));
    }

    private void initialiseFX(JFXPanel videoPanel) {
        String embeddedVideoPath = "";

        Media media = new Media(new java.io.File(embeddedVideoPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.setFitWidth(videoPanel.getWidth());
        mediaView.setFitHeight(videoPanel.getHeight());
        mediaView.setY((videoPanel.getHeight() - mediaView.getBoundsInParent().getHeight()) / 2);
        Group root = new Group(mediaView);
        Scene scene = new Scene(root, videoPanel.getWidth(), videoPanel.getHeight(), javafx.scene.paint.Color.BLACK);
        videoPanel.setScene(scene);
        mediaPlayer.play();
    }

    public boolean inputValidation()
    {
        if (questionTypeInputTextField.getText().replaceAll(" ", "").length() <= 0) return false;
        if (dropDownMenu.getSelectedItem() == null) return false;
        return true;
    }
}

