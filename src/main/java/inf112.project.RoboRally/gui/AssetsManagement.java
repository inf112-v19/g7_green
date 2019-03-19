package inf112.project.RoboRally.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import javax.xml.soap.Text;

class AssetsManagement {

    private final AssetManager assetManager = new AssetManager();

    void loadTextures() {
        assetManager.load("assets/conveyorbelts/conveyorBelt_east.png", Texture.class);
        assetManager.load("assets/conveyorbelts/conveyorBelt_west.png", Texture.class);
        assetManager.load("assets/conveyorbelts/conveyorBelt_north.png", Texture.class);
        assetManager.load("assets/conveyorbelts/conveyorBelt_south.png", Texture.class);
        assetManager.load("assets/crossedWrench.png", Texture.class);
        assetManager.load("assets/floor_metal.jpg", Texture.class);
        assetManager.load("assets/player_one.png", Texture.class);
        assetManager.load("assets/rotationCog_right.png", Texture.class);
        assetManager.load("assets/rotationCog_left.png", Texture.class);
        assetManager.load("assets/singleWrench.png", Texture.class);
        assetManager.load("assets/flags/flag1.png", Texture.class);
        assetManager.load("assets/flags/flag2.png", Texture.class);
        assetManager.load("assets/flags/flag3.png", Texture.class);
        assetManager.load("assets/flags/flag4.png", Texture.class);
        assetManager.load("assets/flags/flag5.png", Texture.class);
        assetManager.load("assets/flags/flag6.png", Texture.class);
        assetManager.load("assets/flags/flag7.png", Texture.class);
        assetManager.load("assets/flags/flag8.png", Texture.class);
        assetManager.load("assets/flags/flag9.png", Texture.class);
        assetManager.load("assets/flags/flag.png", Texture.class);
        assetManager.load("assets/pit.png", Texture.class);
        assetManager.load("assets/laserHorizontal.png", Texture.class);
        assetManager.load("assets/laserVertical.png", Texture.class);
        assetManager.load("assets/walls/wall_North.png", Texture.class);
        assetManager.load("assets/walls/wall_South.png", Texture.class);
        assetManager.load("assets/walls/wall_East.png", Texture.class);
        assetManager.load("assets/walls/wall_West.png", Texture.class);
        assetManager.load("assets/walls/wall_NorthSouth.png", Texture.class);
        assetManager.load("assets/walls/wall_NorthEast.png", Texture.class);
        assetManager.load("assets/walls/wall_NorthWest.png", Texture.class);
        assetManager.load("assets/walls/wall_SouthEast.png", Texture.class);
        assetManager.load("assets/walls/wall_SouthWest.png", Texture.class);
        assetManager.load("assets/walls/wall_EastWest.png", Texture.class);
        assetManager.load("assets/walls/wall_NorthSouthEast.png", Texture.class);
        assetManager.load("assets/walls/wall_NorthSouthWest.png", Texture.class);
        assetManager.load("assets/walls/wall_NorthEastWest.png", Texture.class);
        assetManager.load("assets/walls/wall_SouthEastWest.png", Texture.class);
        assetManager.load("assets/walls/wall_NorthSouthEastWest.png", Texture.class);
    }

    void finishLoading() {
        while (!assetManager.isFinished()) {
            assetManager.update();
        }
        assetManager.finishLoading();
    }

    Texture getAssetFileName(String s) {
        return assetManager.get(s,Texture.class);
    }

    void dispose() {
        assetManager.dispose();
    }
}
