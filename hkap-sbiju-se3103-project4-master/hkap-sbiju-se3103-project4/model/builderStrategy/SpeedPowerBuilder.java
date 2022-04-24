package model.builderStrategy;

import model.Power;

import java.awt.*;

public class SpeedPowerBuilder extends PowerBuilder {

    @Override
    public void buildFallSpeed() {
        power.setFallSpeed(5);
    }

    @Override
    public void buildColor() {
        power.setColor(Color.yellow);
    }

    @Override
    public void buildSize() {
        power.setSize(Power.WIDTH, Power.HEIGHT);
    }

    @Override
    public void buildPowerType() {
        power.setPowerType(Power.PowerType.SPEED);
    }
}
