package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
//import javax.swing.JOptionPane;

import view.GameBoard;
import model.builderStrategy.SpeedPowerBuilder;
import model.builderStrategy.DoubleBulletPowerBuilder;
import model.builderStrategy.PowerBuilderDirector;
//import view.TextDraw;


public class EnemyComposite extends GameElement {
    public static final int NROWS = 2;
    public static int NCOLS=10 ;
    public static final int ENEMY_SIZE = 20;
    public static int UNIT_MOVE=3;
    int no_of_enemy;

    public GameBoard gameBoard;

    private ArrayList<ArrayList<GameElement>> rows;
    private ArrayList<GameElement> bombs;
    private ArrayList<GameElement> powers;
    private boolean movingToRight = true;
    private Random random = new Random();


    public EnemyComposite() {
        rows = new ArrayList<>();
        bombs = new ArrayList<>();
        powers = new ArrayList<>();

//        for (int r = 0; r < NROWS; r++) {
//            var oneRow = new ArrayList<GameElement>();
//            rows.add(oneRow);
//            for (int c = 0; c < NCOLS; c++) {
//                oneRow.add(new Enemy(c * ENEMY_SIZE * 2, r * ENEMY_SIZE * 2, ENEMY_SIZE, Color.yellow, true));
//                no_of_enemy++;
//            }
//        }
    }
    public void initEnemyComposite() {
        for (var r: rows) {
            r.clear();
        }
        System.out.println("INIT called");
        for (int r = 0; r < NROWS; r++) {
            var oneRow = new ArrayList<GameElement>();
            rows.add(oneRow);
            for (int c = 0; c < NCOLS; c++) {
                oneRow.add(new Enemy(
                        c * ENEMY_SIZE * 2, r * ENEMY_SIZE * 2, ENEMY_SIZE, Color.yellow, true));
            }
        }
    }

    @Override
    public void render(Graphics2D g2) {
        for (var r: rows) for (var e: r) e.render(g2);
        for (var b: bombs) b.render(g2);
        for (var p: powers) {
            p.render(g2);
        }
    }

    public ArrayList<ArrayList<GameElement>> getRows() {
        return rows;
    }

    @Override
    public void animate() {
        int dx = UNIT_MOVE;
		int dy = 0;
		int enemySize = ENEMY_SIZE;
        if (movingToRight) {
            if (rightEnd() >= GameBoard.WIDTH) {
                dx = -dx;
				dy = enemySize;
                movingToRight = false;
        //when ever the collison occur y is incremented by one
    	/*for(var row:rows)
            for(var e: row){
                e.y +=20;
            if(e.y>=GameBoard.HEIGHT){
            ////code Added by me to check the collision of the enemy with side wall increases the y by 20 and if it is greater than the height of the game then it will end the game
			gameBoard.getGameState(true);
            gameBoard.getCanvas().getGameElements().add(new TextDraw("You lose! Score: " + gameBoard.getScore(),50, 50, Color.red, 30));
                        } 
                    } */
            }
        } else {
            dx = -dx;
            if (leftEnd() <= 0) {
                dx = -dx;
				dy = enemySize;
                movingToRight = true;
                //collison occurs and increment in y by 5
               /* for(var row:rows)
                for(var e: row){
                //code Added by Hrang to check the collision of the enemy with side wall increases the y by 20 and if it is greater than the height of the game then it will end the game
                e.y +=20;
               if(e.y>=GameBoard.HEIGHT){
                //end of the game
                gameBoard.getGameState(true);
           		gameBoard.getCanvas().getGameElements().add(new TextDraw("You lose! Score: " + gameBoard.getScore(),50, 50, Color.red, 30));
                    } 
                }*/
            }
        }

        for (var row: rows) {
            for (var e: row) {
                e.x += dx;
				e.y += dy;
            }
        }

        for (var b: bombs) b.animate();

        for (var p: powers)
            p.animate();
    }
    
    private int rightEnd() {
        int xEnd = -100;
        for (var row: rows) {
            if (row.size() == 0) continue;
            int x = row.get(row.size() - 1).x + ENEMY_SIZE;
            if (x > xEnd) xEnd = x;
        }
        return xEnd;
    }

    private int leftEnd() {
        int xEnd = 9000;
        for (var row: rows) {
            if (row.size() == 0) continue;
            int x = row.get(0).x;
            if (x < xEnd) xEnd = x;
        }
        return xEnd;
    }
	
	public int bottomEnd() {
		int yEnd = -100;
		for (var row: rows) {
			if (row.size() == 0)
				continue;
			
			int y = row.get(row.size() - 1).y + ENEMY_SIZE;
			if (y > yEnd)
				yEnd = y;
		}

		return yEnd;
	}

    public void dropBombs() {
        for (var row: rows) {
            for (var e: row) {
                if (random.nextFloat() < 0.1F) {
                    bombs.add(new Bomb(e.x, e.y));
                }
            }
        }
    }
	
	//I used direct call method in timelistener to call enemycomposite.bottomEnd without using this getter
	/*public boolean reachedBottom() {
		return bottomEnd() >= GameBoard.HEIGHT;
	} */

    public void removeBombsOutOfBound() {
        var remove = new ArrayList<GameElement>();
        for (var b: bombs) if (b.y >= GameBoard.HEIGHT) remove.add(b);
        bombs.removeAll(remove);
    }

    private void powerUpChance(int spawnX, int spawnY) {
        float powerUpChance = random.nextFloat();
        float powerUpChanceOdds = GameBoard.levelCount / 10F;
        if (powerUpChance < powerUpChanceOdds) {
            PowerBuilderDirector pbDirector = gameBoard.getPowerBuilderDirector();
            float randomPowerUp = random.nextFloat();

            if (randomPowerUp < 0.1F)
                pbDirector.setPowerBuilder(new DoubleBulletPowerBuilder());
            else
                pbDirector.setPowerBuilder(new SpeedPowerBuilder());


            pbDirector.createPower(spawnX, spawnY);
            powers.add(pbDirector.getPower());
        }
    }

    public void processCollision(Shooter shooter) {
        int num;
        var removeBullets = new ArrayList<GameElement>();
        for (var row: rows) {
            var removeEnemies = new ArrayList<GameElement>();
            for (var enemy: row) {
                for (var bullet: shooter.getWeapons()) {
                    if (enemy.collideWith(bullet)) {
                        removeBullets.add(bullet);
                        removeEnemies.add(enemy);
						num=0;
                        //code added for enemy killed and game score incremented
                        num+=removeEnemies.size();
                        //no_of_enemy--;
						no_of_enemy-=num;
						GameBoard.score+=10;
                        powerUpChance(bullet.x, bullet.y);
                        /*if(no_of_enemy==0){
							JOptionPane.showMessageDialog(null,"Game Over, Your Win with score: "+GameBoard.score);
							gameBoard.getGameState(true);
							gameBoard.getCanvas().getGameElements().add(new TextDraw("You Win! Score: " + gameBoard.getScore(),50, 50, Color.red, 30));
                        } */
                    }
                }
            }
            row.removeAll(removeEnemies);
        }

        shooter.getWeapons().removeAll(removeBullets);

		// start of remove bomb collides with bullet shoot
        var removeBombs = new ArrayList<GameElement>();
        //var removeComponent = new ArrayList<GameElement>();
        removeBullets.clear();

        for (var b: bombs) {
            for (var bullet: shooter.getWeapons()) {
                if (b.collideWith(bullet)) {
                    removeBombs.add(b);
                    removeBullets.add(bullet);
                }
            }
			/*
            // code added collision with the shooter with the bomb
            for(var component:shooter.getComponents()){
                if(b.collideWith(component)){{
                    removeBombs.add(b);
                    removeComponent.add(component);
                }}
            }*/
		} 
		shooter.getWeapons().removeAll(removeBullets);
		bombs.removeAll(removeBombs);

        var removePowers = new ArrayList<GameElement>(); // to remove power ups once collided with shooter

        for (var p: powers) {
            for (var c: shooter.getComponents()) {
                if (p.collideWith(c)) {
                    removePowers.add(p);
                    // type cast GameElemnt p to Power p so we can access activatePower(shooter)
                    Power power = (Power) p;
                    // start new power up
                    power.activatePower(shooter);
                    shooter.notifyListener();
                }
                else if (p.y >= GameBoard.HEIGHT)
                    removePowers.add(p);
            }
        }
        powers.removeAll(removePowers);
		//end of bomb vs bullet collision check

		var removeComponents = new ArrayList<GameElement>();
		removeBombs.clear();

		// shooter box components removed on collide
		for (var b: bombs) {
			for (var c: shooter.getComponents()) {
				if (b.collideWith(c)) {
					removeBombs.add(b);
					removeComponents.add(c);
				}
			}
		}
		/*if(shooter.getComponents().size()==0){
			{
				gameBoard.getGameState(true);
				gameBoard.getCanvas().getGameElements().add(new TextDraw("You lose! Score: " + gameBoard.getScore(),50, 50, Color.red, 30));
			}
		}	*/
		shooter.getComponents().removeAll(removeComponents);
        shooter.getWeapons().removeAll(removeBullets);
		bombs.removeAll(removeBombs);
        //code added for player lives and score
		/*
        if(shooter.getComponents().size()==0){
        {
		JOptionPane.showMessageDialog(null,"Game Over, Your Lose with score: "+GameBoard.score);
        System.exit(0);
        }
        bombs.removeAll(removeBombs);
    	} */

	}

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void checkAllEnemiesKilled() {
        boolean enemiesGone = true;
        for (var r: rows) {
            if (!r.isEmpty())
                enemiesGone = false;
        }
        if (enemiesGone && GameBoard.levelCount == 5)
            GameBoard.setGameWon(true);
        else if (enemiesGone)
            gameBoard.startNextLevel();
    }

	public int numEnemy(){
		return no_of_enemy;
	}
}
