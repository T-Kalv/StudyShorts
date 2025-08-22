// Program: JDBC.java
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
package database;

import jdk.jfr.Category;

import java.sql.*;
import java.util.*;

public class JDBC
{
    //MySQL
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/study_gui_db";
    private static final String DB_USERNAME = "study";
    private static final String DB_PASSWORD = "1234";

    public static boolean insertNewQuestion(String question, String module, String[] answers, int correctIndex)
    {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            //insert module to db
            Module moduleObj = fetchModule(module);
            if (moduleObj == null) {
                moduleObj = insertModule(module);
            }
            //insert question to db
            Question questionObj = insertNewQuestion(moduleObj, question);
            return insertAnswers(questionObj, answers, correctIndex);//insert answers to db
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;//failed to insert
    }

    public static Module fetchModule(String module)
    {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement getModuleQuery = connection.prepareStatement("SELECT * FROM category WHERE category_name = ?");
            getModuleQuery.setString(1, module);
            ResultSet resultSet = getModuleQuery.executeQuery();
            if (resultSet.next()) {
                int moduleId = resultSet.getInt("category_id");
                return new Module(moduleId, module);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Module insertModule(String module)
    {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement insertModuleQuery = connection.prepareStatement("INSERT INTO category (category_name)" + "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            insertModuleQuery.setString(1, module);
            insertModuleQuery.executeUpdate();
            ResultSet resultSet = insertModuleQuery.getGeneratedKeys();
            if (resultSet.next()) {
                int moduleId = resultSet.getInt(1);
                return new Module(moduleId, module);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Question insertNewQuestion(Module module, String questionText)
    {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement insertNewQuestionQuery = connection.prepareStatement("INSERT INTO question (category_id, question_text)" + "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertNewQuestionQuery.setInt(1, module.getCategoryId());
            insertNewQuestionQuery.setString(2, questionText);
            insertNewQuestionQuery.executeUpdate();
            ResultSet resultSet = insertNewQuestionQuery.getGeneratedKeys();
            if (resultSet.next()) {
                int questionId = resultSet.getInt(1);
                return new Question(questionId, module.getCategoryId(), questionText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean insertAnswers(Question question, String[] answers, int correctIndex)
    {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement insertAnswerQuery = connection.prepareStatement("INSERT INTO answer (question_id, answer_text, is_correct)" + "VALUES(?, ?, ?)");
            insertAnswerQuery.setInt(1, question.getQuestionId());
            int answerLength = answers.length;
            for (int i = 0; i < answerLength; i++) {
                insertAnswerQuery.setString(2, answers[i]);
                if (i == correctIndex) {
                    insertAnswerQuery.setBoolean(3, true);
                } else {
                    insertAnswerQuery.setBoolean(3, false);
                }
                insertAnswerQuery.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;//failed to insert answers
    }

    public static ArrayList<String> fetchModules()
    {
        ArrayList<String> modulesList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement fetchModulesQuery = connection.createStatement();
            ResultSet resultSet = fetchModulesQuery.executeQuery("SELECT * FROM category");
            while (resultSet.next()) {
                String moduleName = resultSet.getString("category_name");
                modulesList.add(moduleName);
            }
            return modulesList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Question> fetchQuestions(Module module)
    {
        ArrayList<Question> questionsList = new ArrayList<>();
        try
        {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement fetchQuestionsQuery = connection.prepareStatement("SELECT * FROM question JOIN category " + "ON question.category_id = category.category_id WHERE category.category_name = ? ORDER BY RAND()");
            fetchQuestionsQuery.setString(1, module.getCategoryName());
            ResultSet resultSet = fetchQuestionsQuery.executeQuery();
            while (resultSet.next())
            {
                int questionId = resultSet.getInt("question_id");
                int categoryId = resultSet.getInt("category_id");
                String questionText = resultSet.getString("question_text");
                questionsList.add(new Question(questionId, categoryId, questionText));
            }
            return questionsList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Answer> fetchAnswers(Question question)
    {
        ArrayList<Answer> answersList = new ArrayList<>();
        try
        {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement fetchAnswersQuery = connection.prepareStatement("SELECT * FROM question JOIN answer " + "ON question.question_id = answer.question_id WHERE question.question_id = ? ORDER BY RAND()");
            fetchAnswersQuery.setInt(1, question.getQuestionId());
            ResultSet resultSet = fetchAnswersQuery.executeQuery();
            while (resultSet.next())
            {
                int answerId = resultSet.getInt("answer_id");
                String answerText = resultSet.getString("answer_text");
                boolean isCorrect = resultSet.getBoolean("is_correct");
                Answer answer = new Answer(answerId, question.getQuestionId(), answerText, isCorrect);
                answersList.add(answer);
            }
            return answersList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
