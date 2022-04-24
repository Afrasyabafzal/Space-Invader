package model.builderStrategy;

import view.GameBoard;
import model.Power;

public class PowerBuilderDirector {
    private PowerBuilder powerBuilder;
    // reference to gameBoard so changes can be made to shooter
    private GameBoard gameBoard;

    public PowerBuilderDirector(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Power getPower() {
        return powerBuilder.getPower();
    }

    public void setPowerBuilder(PowerBuilder powerBuilder) {
        this.powerBuilder = powerBuilder;
    }

    // x and y passed for spawn position
    public void createPower(int x, int y) {
        powerBuilder.createPower();
        powerBuilder.buildFallSpeed();
        powerBuilder.buildSize();
        powerBuilder.buildPowerType();
        powerBuilder.buildColor();
        powerBuilder.setPosition(x, y);
    }
}
