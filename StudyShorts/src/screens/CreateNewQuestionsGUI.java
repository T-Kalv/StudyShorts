// Program: CreateNewQuestionsGUI.java
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
import constants.*;
import database.JDBC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class CreateNewQuestionsGUI extends JFrame
{
    private JTextArea questionTextInputArea;
    private JTextField moduleTextInputField;
    private JRadioButton[] optionsRadioButtons;
    private JTextField[] optionsTextInputField;
    private ButtonGroup buttonGroup;
    public CreateNewQuestionsGUI()
    {
        super("StudyShorts");
        setSize(851, 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(variables.BLACK);
        optionsRadioButtons = new JRadioButton[4];
        optionsTextInputField = new JTextField[4];
        buttonGroup = new ButtonGroup();
        addGUIScreenComponents();
    }

    private  void addGUIScreenComponents()
    {
        //title
        JLabel titleLabel = new JLabel("Create New Questions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        //titleLabel.setBounds(25, 15, 310, 29);
        titleLabel.setBounds(260, 15, 310, 29);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(variables.AMBER);
        add(titleLabel);

        //question
        JLabel questionLabel = new JLabel("Type Question: ");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        //questionLabel.setBounds(50, 60, 93, 20);
        questionLabel.setBounds(50, 60, 133, 20);
        questionLabel.setForeground(variables.WHITE);
        add(questionLabel);

        //question text input area
        questionTextInputArea = new JTextArea();
        questionTextInputArea.setFont(new Font("Arial", Font.BOLD, 16));
        questionTextInputArea.setBounds(50, 90, 310, 110);
        questionTextInputArea.setForeground(variables.WHITE);
        questionTextInputArea.setBackground(variables.DARK_GREY);
        questionTextInputArea.setLineWrap(true);
        questionTextInputArea.setWrapStyleWord(true);
        add(questionTextInputArea);

        //module label
        JLabel moduleLabel = new JLabel("Module: ");
        moduleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moduleLabel.setBounds(50, 250, 93, 20);
        moduleLabel.setForeground(variables.WHITE);
        add(moduleLabel);

        //module text input field
        moduleTextInputField = new JTextField();
        moduleTextInputField.setFont(new Font("Arial", Font.BOLD, 16));
        moduleTextInputField.setBounds(50, 280, 310, 36);
        moduleTextInputField.setForeground(variables.WHITE);
        moduleTextInputField.setBackground(variables.DARK_GREY);
        add(moduleTextInputField);
        addOptionsComponents();

        //back
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBounds(300, 500, 262, 20);
        backButton.setForeground(variables.WHITE);
        backButton.setBackground(variables.RED);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                TitleGUIScreen titleGUIScreen = new TitleGUIScreen();
                titleGUIScreen.setLocationRelativeTo(CreateNewQuestionsGUI.this);
                CreateNewQuestionsGUI.this.dispose();
                titleGUIScreen.setVisible(true);
            }
        });
        add(backButton);


        //save question
        JButton saveQuestionButton = new JButton("Save Question");
        saveQuestionButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveQuestionButton.setBounds(300, 450, 262, 45);
        saveQuestionButton.setForeground(variables.WHITE);
        saveQuestionButton.setBackground(variables.GREEN);
        saveQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateUserInput()) {
                    String question = questionTextInputArea.getText();
                    String module = moduleTextInputField.getText();
                    String[] answers = new String[optionsTextInputField.length];
                    int correctIndex = 0;
                    for (int i = 0; i < optionsTextInputField.length; i++) {
                        answers[i] = optionsTextInputField[i].getText();
                        if (optionsRadioButtons[i].isSelected()) {
                            correctIndex = i;
                        }
                    }
                    if (JDBC.insertNewQuestion(question, module, answers, correctIndex)) {
                        JOptionPane.showMessageDialog(CreateNewQuestionsGUI.this, "New Question Has Been Added To DB!");
                        resetUserFields();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(CreateNewQuestionsGUI.this, "Failed To Add New Question To DB!");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(CreateNewQuestionsGUI.this, "Invalid User Input!");
                }
            }
        });
        add(saveQuestionButton);
    }

    private void addOptionsComponents() {
        //options letters
        int xSpacing = 65;
        for (int i = 0; i < 4; i++) {
            char optionLetter = (char) ('A' + i);
            JLabel optionLabel = new JLabel("Option (" + optionLetter + "): ");
            optionLabel.setFont(new Font("Arial", Font.BOLD, 16));
            optionLabel.setBounds(470, 60 + (i * xSpacing), 93, 20);
            optionLabel.setForeground(variables.WHITE);
            add(optionLabel);

            //option buttons
            optionsRadioButtons[i] = new JRadioButton();
            optionsRadioButtons[i].setBounds(440, 100 + (i * xSpacing), 21, 21);
            optionsRadioButtons[i].setBackground(variables.BLACK);
            optionsRadioButtons[i].setForeground(variables.WHITE);
            buttonGroup.add(optionsRadioButtons[i]);
            add(optionsRadioButtons[i]);

            //options text
            optionsTextInputField[i] = new JTextField();
            optionsTextInputField[i].setFont(new Font("Arial", Font.BOLD, 16));
            optionsTextInputField[i].setBounds(470, 90 + (i * xSpacing), 310, 36);
            optionsTextInputField[i].setForeground(variables.WHITE);
            optionsTextInputField[i].setBackground(variables.DARK_GREY);
            add(optionsTextInputField[i]);
        }
        optionsRadioButtons[0].setSelected(true);

    }
        private boolean validateUserInput()
        {
            if((questionTextInputArea.getText().replaceAll(" ", "").length()) <= 0)
            {
                return false;
            }
            if ((moduleTextInputField.getText().replaceAll(" ", "").length()) <= 0)
            {
                return false;
            }

            for (int i = 0; i < optionsTextInputField.length; i++)
            {
                if ((optionsTextInputField[i].getText().replaceAll(" ", "").length()) <= 0)
                {
                    return false;
                }
            }
            return true;
        }

        private void resetUserFields()
        {
            questionTextInputArea.setText("");
            moduleTextInputField.setText("");
            for (int i = 0; i < optionsTextInputField.length; i++)
            {
                optionsTextInputField[i].setText("");
                optionsRadioButtons[i].setSelected(false);
            }
        }
}
