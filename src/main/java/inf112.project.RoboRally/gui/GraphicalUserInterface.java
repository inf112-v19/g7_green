package inf112.project.RoboRally.gui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.project.RoboRally.actors.IPlayer;
import inf112.project.RoboRally.actors.Player;
import inf112.project.RoboRally.cards.ICard;
import inf112.project.RoboRally.cards.IDeck;
import inf112.project.RoboRally.game.Game;
import inf112.project.RoboRally.game.GameStatus;
import inf112.project.RoboRally.game.IGame;
import inf112.project.RoboRally.objects.*;

import java.util.List;

public class GraphicalUserInterface extends ApplicationAdapter{
    private IGame game;
    private Grid boardScreen;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final int CARD_SCREEN_WIDTH = 200;
    private static final int CARD_SCREEN_HEIGHT = HEIGHT;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Viewport viewport;
    private AssetsManagement assetsManager = new AssetsManagement();

    private int[] xPositionDrawer;
    private int[] yPositionDrawer;

    CardGui cardGui;

    // to be moved
    //private IDeck[] selectedCards;

    @Override
    public void create () {
        createNewGame();
        setupScreens();
        cardGui = new CardGui(game,CARD_SCREEN_WIDTH,CARD_SCREEN_HEIGHT);
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        viewport = new FillViewport(WIDTH, HEIGHT, camera);
        viewport.apply();
        viewport.update(WIDTH, HEIGHT, true);
        Input input = new Input(camera);
        Gdx.input.setInputProcessor(input);
        loadTextures();

    }

    private void loadTextures() {
        batch = new SpriteBatch();
        assetsManager.loadTextures();
        assetsManager.finishLoading();
    }

    private void setupScreens() {
        boardScreen = new Grid(
                new Tile(CARD_SCREEN_WIDTH,WIDTH,0,HEIGHT)
                ,game.getBoard().getColumns(),game.getBoard().getRows());
    }

    private void createNewGame() {
        game = new Game();
        game.initializeGame();
        game.dealOutProgramCards();
        game.setGameStatus(GameStatus.SELECT_CARDS);
        xPositionDrawer = new int[game.getPlayers().size()];
        yPositionDrawer = new int[game.getPlayers().size()];
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        userInputs();
        drawBoard();
        drawPlayers();
        batch.end();

        cardGui.draw();

    }


    private void userInputs() {
        if (Gdx.input.justTouched() && game.getTheCurrentGameStatus() == GameStatus.SELECT_CARDS) {
            int x = Gdx.input.getX();
            int y = HEIGHT-Gdx.input.getY();
            cardGui.userInputs(x,y);
        } else if (Gdx.input.justTouched()) {
            game.doTurn();
        }
    }



    private void drawPlayers() {
        List<IPlayer> players = game.getPlayers();
        int animationSpeed = 9;
        for (int i = 0; i < players.size(); i++) {
            IPlayer player = players.get(i);
            if (!game.getBoard().moveValid(player.getX(),player.getY())) {
                continue;
            }
            int xPosPlayer = boardScreen.getStartX(player.getX());
            if (xPositionDrawer[i] != xPosPlayer) {
                if (xPositionDrawer[i] < xPosPlayer) {
                    xPositionDrawer[i] = xPositionDrawer[i]+animationSpeed > xPosPlayer ?
                            xPosPlayer : xPositionDrawer[i]+animationSpeed;
                } else {
                    xPositionDrawer[i] = xPositionDrawer[i]-animationSpeed < xPosPlayer ?
                            xPosPlayer : xPositionDrawer[i]-animationSpeed;
                }
            }
            int yPosPlayer = boardScreen.getStartY(player.getY());
            if (yPositionDrawer[i] != yPosPlayer) {
                if (yPositionDrawer[i] < yPosPlayer) {
                    yPositionDrawer[i] = yPositionDrawer[i]+animationSpeed > yPosPlayer ?
                            yPosPlayer : yPositionDrawer[i]+animationSpeed;
                } else {
                    yPositionDrawer[i] = yPositionDrawer[i]-animationSpeed < yPosPlayer ?
                            yPosPlayer : yPositionDrawer[i]-animationSpeed;
                }
            }
            batch.setColor(player.getColor());
            batch.draw(assetsManager.getAssetFileName("assets/player_color.png")
                    ,xPositionDrawer[i],yPositionDrawer[i],
                    boardScreen.getTileWidth(), boardScreen.getTileHeight());
            batch.setColor(Color.WHITE);
            batch.draw(assetsManager.getAssetFileName(player.getTexture()),
                    xPositionDrawer[i], yPositionDrawer[i],
                    boardScreen.getTileWidth(), boardScreen.getTileHeight());

        }
    }

    private void drawBoard() {
        int offset = 1;
        for (int j = 0; j < boardScreen.getHeight(); j++) {
            for (int i = 0; i < boardScreen.getWidth(); i++) {
                IObjects object = game.getBoard().getObject(i,j);
                String texture = object.getTexture();
                batch.draw(assetsManager.getAssetFileName("assets/floor_metal.jpg"),
                        boardScreen.getStartX(i)+offset, boardScreen.getStartY(j)+offset,
                        boardScreen.getTileWidth()-offset*2, boardScreen.getTileHeight()-offset*2);
                if (texture != null) {
                    batch.draw(assetsManager.getAssetFileName(object.getTexture()),
                            boardScreen.getStartX(i) + offset, boardScreen.getStartY(j) + offset,
                            boardScreen.getTileWidth() - offset * 2, boardScreen.getTileHeight() - offset * 2);
                }
            }
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
        assetsManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        // viewport.update(width, height, true);
    }

}
