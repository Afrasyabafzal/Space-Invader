package model;

import java.awt.*;

public class Power extends GameElement {

    public enum PowerType {
        SPEED, EXTRA_BULLETS
    }
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    char image;

    private int fallSpeed;
    private PowerType powerType;

    public Power() {
        filled = true;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, width, height);
        g2.setColor(Color.WHITE);
        g2.drawString(String.valueOf(image), x + 5, y + 15);
    }

    @Override
    public void animate() {
        super.y += fallSpeed;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void setPowerImage() {
        switch (powerType) {
            case SPEED:
                image = 'S';
                break;
            case EXTRA_BULLETS:
                image = 'B';
                break;
        }
    }

    public void setFallSpeed(int fallSpeed) {
        this.fallSpeed = fallSpeed;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setPowerType(PowerType powerType) {
        this.powerType = powerType;
        setPowerImage();
    }

    public void activatePower(Shooter shooter) {
        // startPowerTimer
        shooter.activatePower(powerType);
    }
}
