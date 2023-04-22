package dev.elotonsotilas;

import dev.elotonsotilas.exceptions.IllegalMoveException;
import dev.elotonsotilas.exceptions.InvalidMoveException;
import dev.elotonsotilas.models.Game;
import dev.elotonsotilas.models.Move;
import jexer.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;

public class ChessApp extends TApplication implements KeyListener {
    static TText text;
    static TText notificationBar;
    static TField input;
    static TPanel panel;
    static TLabel moveFieldLabel;
    static TWindow gameWindow;

    // Poll for keypress using KeyListener
    static boolean wasEnterPressed = false;

    public ChessApp() throws UnsupportedEncodingException {
        super(BackendType.SWING, 90, 32, 14);

        // Create a new TWindow object to hold the game screen
        gameWindow = new TWindow(this, "Chess", 90, 24);


        // Add the game screen to the TWindow object
        panel = gameWindow.addPanel(0, 0, 90, 32);

        // Get the board
        text = panel.addText(Game.currentGame.toString(), 0, 0, 90,16);

        // Put it in the centre
        text.centerJustify();

        moveFieldLabel = panel.addLabel("Input Move >", 20, 18);
        input = panel.addField(34, 18, 28,true);
    }

    public static void main(String[] args) throws Exception {
        ChessApp app = new ChessApp();
        app.showWindow(gameWindow);
        app.run();

        // loop until the currentGame is over: (permanent loop for now)
        while (Game.currentGame != null) {
            if (wasEnterPressed) {
                try {
                    // I could have made this a method, but I preferred to use the constructor
                    // as a simple method
                    new Move(input.getText());
                } catch (InvalidMoveException e) {
                    notificationBar.setText("Invalid move, try again!");
                } catch (IllegalMoveException e) {
                    notificationBar.setText("You cannot move there. Enter another move!");
                } finally {
                    // clear text box
                    input.setText("");
                }

                // unset
                wasEnterPressed = false;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if Enter was pressed, set value to true
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            wasEnterPressed = true;
            e.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
