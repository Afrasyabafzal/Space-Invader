package model.patternStrategy;

import model.EnemyComposite;
import view.GameBoard;
import view.MyCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Level1 implements LevelActivator {
    private GameBoard gameBoard;
    private MyCanvas canvas;
    int timerCount = 100;
    Timer timer = new Timer(100, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {


            // starts level once animation is complete
            if (timerCount == 0) {
                timerCount = 1000;
                GameBoard.changingLevel = false;
                gameBoard.beginLevel();
                timer.stop();
            }

            timerCount -= 50;
        }
    });

    public Level1(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.canvas = gameBoard.getCanvas();
        GameBoard.changingLevel = true;
        timer.start();

    }

    @Override
    public void setLevelSettings() {
        EnemyComposite.UNIT_MOVE = 3;
        EnemyComposite.NCOLS = 5;

    }

    @Override
    public void startLevel() {
        gameBoard.getEnemyComposite().initEnemyComposite();
    }
}
