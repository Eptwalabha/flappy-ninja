import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import src.gamestates.FlappyNinja;
import src.gamestates.Pause;

/**
 * User: Eptwalabha
 * Date: 15/02/14
 * Time: 20:56
 */
public class Launcher extends StateBasedGame {

    private AppGameContainer container;

    public Launcher() {
        super("Flappy NINJA");

    }

    public static void main(String[] arg0) {

        try {
            AppGameContainer app = new AppGameContainer(new Launcher());
            app.setTargetFrameRate(120);
            app.setDisplayMode(750, 500, false);
            app.setAlwaysRender(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

        if (gameContainer instanceof AppGameContainer)
            this.container = (AppGameContainer) gameContainer;
        container.setShowFPS(true);
//        container.setTargetFrameRate(60);
        this.addState(new FlappyNinja());
        this.addState(new Pause());

    }
}