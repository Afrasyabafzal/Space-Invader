package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.GameElement;

public class MyCanvas extends JPanel {
    private GameBoard gameBoard;
    private ArrayList<GameElement> gameElements = new ArrayList<>();
	
    public MyCanvas(GameBoard gameBoard, int width, int height) {
        this.gameBoard = gameBoard;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;	
		/*if (GameBoard.isGameOver) {
			String gameOutcomeText = GameBoard.gameWon ? "You Won!" : "You Lost!";
			Color gameOutcomeColor = GameBoard.gameWon ? Color.blue : Color.red;
			TextDraw gameOverText = new TextDraw("Game Over, " + gameOutcomeText, 115, 270, gameOutcomeColor, 50);
			TextDraw scoreText = new TextDraw("Score: " + GameBoard.score, gameOverText.x + 190, gameOverText.y + 50, Color.white, 30);
			gameOverText.render(g2);
			scoreText.render(g2);
			return;
		} 
		gameBoard.getScoreText().setText("Score: " + GameBoard.score); */
		
        for (var e: gameElements) {
            e.render(g2);
        }
    }
	

    public ArrayList<GameElement> getGameElements() {
        return gameElements;
    }
}
