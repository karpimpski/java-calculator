package main;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Controller
{
    String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] operands = {"%", "/", "*", "-", "+"};
    String[] special = {"(", ")", "C", "=", "BACK"};
    String decimal = ".";
    boolean resultDisplayed = false;

    public void addCharacter(TextField textField, String character)
    {
        if (resultDisplayed)
        {
            textField.setText("");
            resultDisplayed = false;
        }
        textField.setText(textField.getText() + character);
    }

    public void clearText(TextField textField)
    {
        textField.setText("");
        resultDisplayed = false;
    }

    public void click(MouseEvent event)
    {
        Button button = (Button) event.getSource();
        String text = button.getText();
        handleInput(button.getText(), button.getScene());
    }

    public boolean contains(String[] list, String text)
    {
        for (int i = 0; i < list.length; i++)
        {
            if (list[i].equals(text))
                return true;
        }
        return false;
    }

    public void handleInput(String text, Scene scene)
    {
        TextField textField = (TextField) scene.lookup("#textField");
        String fieldText = textField.getText();

        String lastCharacter = "";
        if (fieldText.length() > 0)
            lastCharacter = textField.getText().substring(textField.getText().length() - 1);

        if (contains(numbers, text))
            addCharacter(textField, text);
        if (contains(operands, text) && !contains(operands, lastCharacter) && textField.getText().length() > 0)
        {
            resultDisplayed = false;
            addCharacter(textField, text);
        }
        if (text.equals(decimal) && !decimal.equals(lastCharacter))
            addCharacter(textField, text);
        if (contains(special, text))
        {
            if (text.equals("C"))
                clearText(textField);
            else if (text.equals("=") && textField.getText().length() > 0)
            {
                double result = new ExpressionBuilder(textField.getText()).build().evaluate();
                textField.setText(String.valueOf(result));
                resultDisplayed = true;
            }
            else if (text.equals("BACK") && textField.getText().length() > 0)
            {
                textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
            }
            else if (text.equals("(") || text.equals(")"))
                addCharacter(textField, text);
        }
    }

    public void keypress(KeyEvent event)
    {
        Node node = (Node) event.getSource();
        Scene scene = node.getScene();
        KeyCode code = event.getCode();

        if (code == KeyCode.ENTER)
            handleInput("=", scene);
        else if (code == KeyCode.BACK_SPACE)
            handleInput("BACK", scene);

        else
            handleInput(event.getText().toUpperCase(), scene);
    }
}
