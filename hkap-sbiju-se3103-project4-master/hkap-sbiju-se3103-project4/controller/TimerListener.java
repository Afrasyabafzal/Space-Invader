package controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import model.Bullet;
import model.Shooter;
import view.GameBoard;
import view.TextDraw;

public class TimerListener implements ActionListener {
    public enum EventType {
        KEY_RIGHT, KEY_LEFT, KEY_SPACE, KEY_P
    }

    private GameBoard gameBoard;
    private LinkedList<EventType> eventQueue;
    private final int BOMB_DROP_FREQ = 20;
    private int frameCounter = 0;
	private boolean gameOver = true;

    public TimerListener(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        eventQueue = new LinkedList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
		//if (gameOver) {
		if (!gameBoard.gameOver()) {
		//gameplay
		++frameCounter;
        update();
        processEventQueue();
        processCollision();
        gameBoard.getCanvas().repaint();
		}
	}
		/*if (GameBoard.isGameOver)
			gameBoard.gameOver(); */
		
    private void processEventQueue() {
        while (!eventQueue.isEmpty()) {
            var e = eventQueue.getFirst();
            eventQueue.removeFirst();
            Shooter shooter = gameBoard.getShooter();
            if (shooter == null) return;

            switch (e) {
                case KEY_LEFT:
                    shooter.moveLeft();
                    break;
                case KEY_RIGHT:
                    shooter.moveRight();
                    break;
                case KEY_SPACE:
                    if (shooter.canFireMoreBullets()) shooter.getWeapons().add(new Bullet(shooter.x, shooter.y));
                    break;
                case KEY_P:
                    gameBoard.pause();
            }
        }

        if (frameCounter == BOMB_DROP_FREQ) {
            gameBoard.getEnemyComposite().dropBombs();
            frameCounter = 0;
        }
    }

    private void processCollision() {
        var shooter = gameBoard.getShooter();
        var enemyComposite = gameBoard.getEnemyComposite();
        
        shooter.removeBulletsOutOfBound();
        enemyComposite.removeBombsOutOfBound();
        enemyComposite.processCollision(shooter);
        //Code Added to Display Score
		//instance of removes previous score
        gameBoard.getCanvas().getGameElements().removeIf(e -> e instanceof TextDraw);
        gameBoard.getCanvas().getGameElements().add(new TextDraw("Score: "+gameBoard.getScore(), 10, 20, Color.yellow, 20));
		
		//for gameover message when character box is zero
		if(shooter.getComponents().size()==0){
			//JOptionPane.showMessageDialog(null,"Game Over, Your Lose with score: "+GameBoard.score);
			gameBoard.getGameState(gameOver);
            gameBoard.getCanvas().getGameElements().add(new TextDraw("You lose! Score: " + gameBoard.getScore(),90, 90, Color.red, 30));
		} 
		// You win when number of Enemy is 0
		if(GameBoard.levelCount>2){
			//JOptionPane.showMessageDialog(null,"You Win with score: "+GameBoard.score);
			gameBoard.getGameState(gameOver);
			gameBoard.getCanvas().getGameElements().add(new TextDraw("You Win! Score: " + gameBoard.getScore(),90, 90, Color.red, 30));
		}
		/*using a getter would make this :
		if(enemyComposite.hitBottom){ */
		if(enemyComposite.bottomEnd() >= GameBoard.HEIGHT){
			//end of the game
			gameBoard.getGameState(true);
			gameBoard.getCanvas().getGameElements().add(new TextDraw("You lose! Score: " + gameBoard.getScore(),90, 50, Color.red, 30));
    }
}
    private void update() {
        var enemyComposite = gameBoard.getEnemyComposite();
        if(!GameBoard.changingLevel)
        {
            enemyComposite.checkAllEnemiesKilled();
        }
        for (var e: gameBoard.getCanvas().getGameElements()) e.animate();
    }

    public LinkedList<EventType> getEventQueue() {
        return eventQueue;
    }
}
