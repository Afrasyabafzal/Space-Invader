package model.patternStrategy;

import model.EnemyComposite;
import view.GameBoard;
import view.MyCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Level2 implements LevelActivator {
    private GameBoard gameBoard;
    private MyCanvas canvas;
    int timerCount = 1300;
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

    public Level2(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        canvas = gameBoard.getCanvas();
        GameBoard.changingLevel = true;
        timer.start();
    }

    @Override
    public void setLevelSettings() {
        EnemyComposite.UNIT_MOVE = 4;
        EnemyComposite.NCOLS = 6;
    }

    @Override
    public void startLevel() {
        gameBoard.getEnemyComposite().initEnemyComposite();
    }
}
