package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import model.Power.PowerType;
import model.observerStrategy.Subject;
import model.observerStrategy.Observer;

public class Shooter extends GameElement implements Subject {
    public static int UNIT_MOVE = 10, MAX_BULLETS = 3, SPEED_BOOST=0, EXTRA_BULLETS=0;
    private ArrayList<Observer> observers = new ArrayList<>();
    private ArrayList<GameElement> components = new ArrayList<>();
    private ArrayList<GameElement> weapons = new ArrayList<>();

    public enum PowerStatus {
        NONE, SPEED, EXTRA_BULLETS
    }

    private boolean hasPower = false;
    private PowerStatus powerStatus = PowerStatus.NONE;

    public Shooter(int x, int y) {
        super(x, y, 0, 0);

        var size = ShooterElement.SIZE;
        var s1 = new ShooterElement(x - size, y - size, Color.white, false);
        var s2 = new ShooterElement(x, y - size, Color.white, false);
        var s3 = new ShooterElement(x - size, y, Color.white, false);
        var s4 = new ShooterElement(x, y, Color.white, false);
        components.add(s1);
        components.add(s2);
        components.add(s3);
        components.add(s4);
    }

    public void setHealth()
    {
        var size = ShooterElement.SIZE;
       var s1 = new ShooterElement(x - size, y - size, Color.white, false);
       var s2 = new ShooterElement(x, y - size, Color.white, false);
       var s3 = new ShooterElement(x - size, y, Color.white, false);
       var s4 = new ShooterElement(x, y, Color.white, false);
       components.add(s1);
       components.add(s2);
       components.add(s3);
       components.add(s4);
    }
    
    public void moveRight() {
        super.x += UNIT_MOVE;
        for (var c: components) {
            c.x += UNIT_MOVE;
        }
    }

    public void moveLeft() {
        super.x -= UNIT_MOVE;
        for (var c: components) {
            c.x -= UNIT_MOVE;
        }
    }

    public boolean canFireMoreBullets() {
        return weapons.size() < MAX_BULLETS + EXTRA_BULLETS;
    }

    public void removeBulletsOutOfBound() {
        var remove = new ArrayList<GameElement>();
        for (var w: weapons) {
            if (w.y < 0) remove.add(w);
        }
        weapons.removeAll(remove);
    }

    public ArrayList<GameElement> getWeapons() {
        return weapons;
    }

    @Override
    public void render(Graphics2D g2) {
        for (var c: components) c.render(g2);
        for (var w: weapons) w.render(g2);
    }

    public ArrayList<GameElement> getComponents() {
        return components;
    }

    @Override
    public void animate() {
        for (var w: weapons) w.animate();
    }

    public void deactivatePower() {
        hasPower = false;

        switch (powerStatus) {
            case SPEED:
                deactivateSpeed();
                break;
            case EXTRA_BULLETS:
                deactivateExtraBullets();
                break;
            case NONE:
                break;
        }
    }

    public void activatePower(PowerType powerType) {
        deactivatePower();
        hasPower = true;

        switch (powerType) {
            case SPEED:
                activateSpeed();
                break;
            case EXTRA_BULLETS:
                activateExtraBullets();
                break;
        }
    }

    private void activateSpeed() {
        Shooter.SPEED_BOOST = Shooter.UNIT_MOVE;
        powerStatus = PowerStatus.SPEED;
    }

    private void deactivateSpeed() {
        Shooter.SPEED_BOOST = 0;
        powerStatus = PowerStatus.NONE;
    }

    private void activateExtraBullets() {
        Shooter.EXTRA_BULLETS = 1;
        powerStatus = PowerStatus.EXTRA_BULLETS;
    }

    private void deactivateExtraBullets() {
        Shooter.EXTRA_BULLETS = 0;
        powerStatus = PowerStatus.NONE;
    }
    @Override
    public void addListener(Observer observer) {
        observers.add(observer);
    }

    // removes observers to array list
    @Override
    public void removeListener(Observer observer) {
        observers.remove(observer);
    }

    // all custom action performed for each observers
    @Override
    public void notifyListener() {
        for (var o: observers) {
            o.actionPerformed(hasPower);
        }
    }
}
