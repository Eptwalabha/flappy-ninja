package src.gamestate;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import src.Constant;

/**
 * User: Eptwalabha
 * Date: 16/02/14
 * Time: 12:21
 */
public class Pause extends BasicGameState {
    @Override
    public int getID() {
        return Constant.BasicGameStateID.PAUSE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        if (gameContainer.getInput().isKeyPressed(Input.KEY_ESCAPE))
            stateBasedGame.enterState(Constant.BasicGameStateID.GAME, new FadeOutTransition(), new FadeInTransition());
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        gameContainer.getGraphics().setColor(Color.blue);
        gameContainer.getGraphics().fillRect(200, 200, 500, 50);
        gameContainer.getGraphics().setColor(Color.white);
        gameContainer.getGraphics().drawString("PAUSE", 210, 210);
    }
}
