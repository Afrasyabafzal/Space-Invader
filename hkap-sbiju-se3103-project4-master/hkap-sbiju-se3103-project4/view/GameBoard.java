package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.KeyController;
import controller.TimerListener;
import model.Enemy;
import model.EnemyComposite;
import model.Shooter;
import model.ShooterElement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import model.builderStrategy.PowerBuilderDirector;
import model.observerStrategy.Observer;
import model.patternStrategy.LevelActivator;
import model.patternStrategy.Level1;
import model.patternStrategy.Level2;

public class GameBoard {
    public static final int WIDTH = 600, HEIGHT  = 400, FPS = 30, DELAY = 1000/FPS;
    public static int score=0;
    private JFrame window;
    private MyCanvas canvas;
    private Shooter shooter;
    private EnemyComposite enemyComposite;
    private Timer timer;
    private TimerListener timerListener;

    public static boolean isGameOver = false;
    public static boolean gameWon = false;

    private LevelActivator levelActivator;
    public static int levelCount = 0;
    public static boolean changingLevel = true;
    private PowerBuilderDirector powerBuilderDirector;

	private JLabel scoreDisplay = new JLabel();

	//public static boolean isGameOver = false;
	//public static boolean gameWon = false;
	private boolean gameOver = true;
	private boolean startGame = false;

	//private TextDraw scoreText = new TextDraw("Score: " + score, 10, GameBoard.HEIGHT + 70, Color.white, 23);
	//JButton startButton;

    public GameBoard(JFrame window) {
        this.window = window;
    }

    public void init() {
        Container cp = window.getContentPane();
        canvas = new MyCanvas(this, WIDTH, HEIGHT);
        cp.add(BorderLayout.CENTER, canvas);
        canvas.addKeyListener(new KeyController(this));
        canvas.requestFocusInWindow();
        canvas.setFocusable(true);

        JButton startButton = new JButton("Start");
        JButton quitButton = new JButton("Quit");
        JButton fixHealth = new JButton("Fix");
        JButton pauseButton = new JButton("Pause");
        JButton resumeButton = new JButton("Resume");
        startButton.setFocusable(false);
        quitButton.setFocusable(false);
        fixHealth.setFocusable(false);
        pauseButton.setFocusable(false);
        resumeButton.setFocusable(false);


        JPanel southPanel = new JPanel();
        southPanel.add(startButton);
        southPanel.add(fixHealth);
        southPanel.add(pauseButton);
        southPanel.add(resumeButton);
        southPanel.add(quitButton);
        cp.add(BorderLayout.SOUTH, southPanel);

        canvas.getGameElements().add(new TextDraw("Click <Start> to Play", 100, 100, Color.yellow, 30));
        timerListener = new TimerListener(this);
        timer = new Timer(DELAY, timerListener);

        startButton.addActionListener(event -> {
			if (isGameOver) {
				gameWon = false;
				isGameOver = false;
			}
			setScore(0);
			getScoreDisplay().setText("0");
			getGameState(startGame);
            shooter = new Shooter(GameBoard.WIDTH / 2, GameBoard.HEIGHT - ShooterElement.SIZE);
            enemyComposite = new EnemyComposite();
            enemyComposite.setGameBoard(this);
            powerBuilderDirector = new PowerBuilderDirector(this);
            canvas.getGameElements().clear();
            canvas.getGameElements().add(shooter);
			//canvas.getGameElements().add(scoreText);
            canvas.getGameElements().add(enemyComposite);
			getScoreDisplay().setText("0");
            timer.start();
            startNextLevel();
        });

        pauseButton.addActionListener(event -> {
            timer.stop();

        });
        resumeButton.addActionListener(event -> {
            timer.start();

        });
        fixHealth.addActionListener(event -> {
            shooter.setHealth();

        });
        quitButton.addActionListener(event -> System.exit(0));
    }

    public MyCanvas getCanvas() {
        return canvas;
    }

    public Timer getTimer() {
        return timer;
    }

	public int getScore() {
        return score;
    }

        public void beginLevel() {
        levelActivator.setLevelSettings();
        levelActivator.startLevel();

        // assigning enemy listeners b/c new ones made each level
       // assignEnemyListeners();
    }
	/*public void addScore(){
		score+=10;
	} */

    public TimerListener getTimerListener() {
        return timerListener;
    }

    public Shooter getShooter() {
        return shooter;
    }

    public EnemyComposite getEnemyComposite() {
        return enemyComposite;
    }
	public void getGameState(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public static void setGameWon(boolean gameWon) {
        GameBoard.gameWon = gameWon;
        GameBoard.isGameOver = true;
    }

	/*public static void setGameWon(boolean gameWon) {
		GameBoard.gameWon = gameWon;
		GameBoard.isGameOver = true;
	}
	public void gameOver() {;
		startButton.setText("Play Again");
		timer.stop();
	}
	public TextDraw getScoreText() {
		return scoreText;
	}
	*/
	public boolean gameOver() {
        return gameOver;
    }

	public void setScore(int score) {
        this.score = score;
    }

	
	public JLabel getScoreDisplay() {
        return scoreDisplay;
    }

    public void pause(){
        timer.stop();
    }

    public void startNextLevel() {
        switch (levelCount) {
            case 0:
                levelActivator = new Level1(this);
                break;
            case 1:
                levelActivator = new Level2(this);
                break;
            default:
                setGameWon(true);
                gameOver();
                break;
        }

        // incrementing for next level if all enemies killed
        levelCount++;
    }

    public void assignEnemyListeners() {
        for (var r: enemyComposite.getRows()) {
            for (var c: r) {
                var enemy = (Enemy) c;
                shooter.addListener((Observer) enemy);
            }
        }
    }


    public PowerBuilderDirector getPowerBuilderDirector() {
        return powerBuilderDirector;
    }
}
