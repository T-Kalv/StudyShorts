// Program: AppGUIScreen.java
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

import database.Answer;
import database.Question;
import database.Module;
import database.JDBC;
import constants.variables;
import javax.swing.*;
import java.awt.*;
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

public class StudyGUIScreen extends JFrame implements ActionListener
{
    private Module module;
    private ArrayList<Question> questions;
    private int questionNum;
    private Question currentQuestion;
    private int currentQuestionNum;
    private int score;
    private boolean firstSelection;
    private JLabel scoreLabel;
    private JTextArea questionTextArea;
    private JButton[] answerButtons;
    private JButton nextQuestionButton;

    public StudyGUIScreen(Module module, int questionNum)
    {
        super("Study Quiz");
        setSize(400, 735); // same as app size
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(variables.BLACK);

        this.module = module;
        answerButtons = new JButton[4];

        // Fetch questions and answers
        questions = JDBC.fetchQuestions(module);
        this.questionNum = Math.min(questionNum, questions.size());
        for (Question question : questions) {
            ArrayList<Answer> answers = JDBC.fetchAnswers(question);
            question.setAnswers(answers);
        }
        currentQuestion = questions.get(currentQuestionNum);

        addGUIScreenComponents();
    }

    private void addGUIScreenComponents()
    {
        // Module label
        JLabel moduleLabel = new JLabel("Module: " + module.getCategoryName());
        moduleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moduleLabel.setBounds(15, 15, 250, 20);
        moduleLabel.setForeground(variables.GREEN);
        add(moduleLabel);

        // Score label
        scoreLabel = new JLabel("Score: " + score + "/" + questionNum);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setBounds(270, 15, 96, 20);
        scoreLabel.setForeground(variables.AMBER);
        add(scoreLabel);

        // Question text
        questionTextArea = new JTextArea(currentQuestion.getQuestionText());
        questionTextArea.setFont(new Font("Arial", Font.BOLD, 32));
        questionTextArea.setBounds(15, 50, 350, 91);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setForeground(variables.WHITE);
        questionTextArea.setBackground(variables.BLACK);
        add(questionTextArea);

        // Answer buttons
        addAnswerComponent();

        // Next question button
        nextQuestionButton = new JButton("Next Question");
        nextQuestionButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextQuestionButton.setBounds(60, 420, 262, 35);
        nextQuestionButton.setBackground(variables.BLACK);
        nextQuestionButton.setForeground(variables.AMBER);
        nextQuestionButton.setVisible(false);
        nextQuestionButton.addActionListener(e -> {
            nextQuestionButton.setVisible(false);
            firstSelection = false;
            currentQuestion = questions.get(++currentQuestionNum);
            questionTextArea.setText(currentQuestion.getQuestionText());
            for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
                Answer answer = currentQuestion.getAnswers().get(i);
                answerButtons[i].setText(answer.getAnswerText());
                answerButtons[i].setBackground(variables.DARK_GREY);
                answerButtons[i].setForeground(variables.WHITE);
            }
        });
        add(nextQuestionButton);

        JButton goBackTitleGUIScreen = new JButton("Return To Main Menu");
        goBackTitleGUIScreen.setFont(new Font("Arial", Font.BOLD, 16));
        goBackTitleGUIScreen.setBounds(60, 470, 262, 35);
        goBackTitleGUIScreen.setBackground(variables.BLACK);
        goBackTitleGUIScreen.setForeground(variables.RED);
        goBackTitleGUIScreen.addActionListener(e -> {
            TitleGUIScreen titleGUIScreen = new TitleGUIScreen();
            titleGUIScreen.setLocationRelativeTo(StudyGUIScreen.this);
            StudyGUIScreen.this.dispose();
            titleGUIScreen.setVisible(true);
        });
        add(goBackTitleGUIScreen);

        int videoPanelY = goBackTitleGUIScreen.getY() + goBackTitleGUIScreen.getHeight() + 10;
        int videoPanelHeight = getHeight() - videoPanelY; // dynamic height to bottom
        JFXPanel videoPanel = new JFXPanel();
        videoPanel.setBounds(0, videoPanelY, getWidth(), videoPanelHeight);
        add(videoPanel);
        Platform.runLater(() -> initialiseFX(videoPanel));
    }

    private void addAnswerComponent()
    {
        int xSpacing = 60;
        for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
            Answer answer = currentQuestion.getAnswers().get(i);
            JButton answerButton = new JButton(answer.getAnswerText());
            answerButton.setFont(new Font("Arial", Font.BOLD, 18));
            answerButton.setBounds(60, 180 + (i * xSpacing), 262, 45);
            answerButton.setForeground(variables.WHITE);
            answerButton.setBackground(variables.DARK_GREY);
            answerButton.setHorizontalAlignment(SwingConstants.CENTER);
            answerButton.addActionListener(this);
            answerButtons[i] = answerButton;
            add(answerButton);
        }
    }

    private void initialiseFX(JFXPanel videoPanel)
    {
        String embeddedVideoPath = "";
        Media media = new Media(new java.io.File(embeddedVideoPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.setFitWidth(videoPanel.getWidth());
        mediaView.setFitHeight(videoPanel.getHeight());
        mediaView.setX((videoPanel.getWidth() - mediaView.getFitWidth()) / 2);
        mediaView.setY((videoPanel.getHeight() - mediaView.getFitHeight()) / 2);
        Group root = new Group(mediaView);
        Scene scene = new Scene(root, videoPanel.getWidth(), videoPanel.getHeight(), javafx.scene.paint.Color.BLACK);
        videoPanel.setScene(scene);
        mediaPlayer.play();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton answerQuestionButton = (JButton) e.getSource();
        Answer correctAnswer = null;
        for (Answer answer : currentQuestion.getAnswers())
        {
            if (answer.isCorrect())
            {
                correctAnswer = answer;
                break;
            }
        }
        if (answerQuestionButton.getText().equals(correctAnswer.getAnswerText()))
        {
            answerQuestionButton.setBackground(variables.GREEN);
            if (!firstSelection) {
                score++;
                scoreLabel.setText("Score: " + score + "/" + questionNum);
            }
            if (currentQuestionNum == questionNum - 1) {
                JOptionPane.showMessageDialog(StudyGUIScreen.this, "Final Score: " + score + "/" + questionNum);
            }
            else
            {
                nextQuestionButton.setVisible(true);
            }
        }
        else
        {
            answerQuestionButton.setBackground(variables.RED);
        }
        firstSelection = true;
    }
}
