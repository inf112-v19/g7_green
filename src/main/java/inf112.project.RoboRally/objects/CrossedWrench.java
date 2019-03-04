package inf112.project.RoboRally.objects;

import inf112.project.RoboRally.actors.Player;

public class CrossedWrench implements IObjects {
    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public GridDirection getDirection() {
        return null;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public Rotation getRotation() {
        return null;
    }

    @Override
    public void doAction(Player player) {
        player.discardOneDamage();

        //TODO check if backup position is available?
        player.setThisPointAsNewBackup();

    }
}