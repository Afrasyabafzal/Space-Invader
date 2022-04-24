package model.builderStrategy;

import java.awt.*;
import model.Power;

public class DoubleBulletPowerBuilder extends PowerBuilder{
    @Override
    public void buildFallSpeed() {
        power.setFallSpeed(5);
    }

    @Override
    public void buildColor() {
        power.setColor(Color.red);
    }

    @Override
    public void buildSize() {
        power.setSize(Power.WIDTH, Power.HEIGHT);
    }

    @Override
    public void buildPowerType() {
        power.setPowerType(Power.PowerType.EXTRA_BULLETS);
    }
}
