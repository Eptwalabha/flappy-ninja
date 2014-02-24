package src.gamestate;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import src.Constant;
import src.components.Position;
import src.entity.EntityFactory;
import src.entity.GuiFactory;
import src.systems.*;
import src.systems.graphic.*;
import src.systems.gui.ButtonHandler;
import src.systems.gui.ButtonSystem;
import src.systems.interfaces.CollisionListener;
import src.utils.SpriteGUI;

/**
 * User: Eptwalabha
 * Date: 07/02/14
 * Time: 09:03
 */
public class FlappyNinja extends BasicGameState implements InputListener, CollisionListener {

    private World world;
    public long timeStart = 0;
    public float bestTime = 0;
    public long bestScore = 0;
    private StateBasedGame parent;

    @Override
    public int getID() {
        return Constant.BasicGameStateID.GAME;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

        SpriteGUI spriteGUI = new SpriteGUI("images/all_tiles.png", 12, 1);
        parent = stateBasedGame;
        world = new World();
        world.initialize();

        // ajout des systèmes.
        world.setSystem(new GravitySystem());
        world.setSystem(new VelocitySystem());
        world.setSystem(new BoundCollisionSystem());
        world.setSystem(new InputSystem(gameContainer));
        world.setSystem(new CollisionSystem());
        world.setSystem(new SpawnPipeSystem(800, spriteGUI));
        world.setSystem(new DeleteEntityOutOfLimitSystem());

        world.setSystem(new DrawDepthImageSystem(), false);
        world.setSystem(new ButtonSystem(gameContainer));
        world.setSystem(new DrawEntityShapeSystem(gameContainer), false);
        world.setSystem(new DebugDrawEntityShapeSystem(gameContainer), false);
        world.setSystem(new DebugDrawVelocitySystem(gameContainer), false);
        world.setSystem(new DrawButtonSystem(gameContainer), false);


        Entity floor = EntityFactory.createBox(world, 0, 30, 500, 30, Color.orange);
        EntityFactory.makeThatEntitySolid(floor);
        floor.addToWorld();

        Entity roof = EntityFactory.createBox(world, 0, 530, 500, 30, Color.transparent);
        EntityFactory.makeThatEntitySolid(roof);
        roof.addToWorld();

        Entity ninja = EntityFactory.createNinja(world, spriteGUI);
        ninja.addToWorld();
        world.getSystem(CollisionSystem.class).setPlayer(ninja);
        world.getSystem(CollisionSystem.class).addNewCollisionListener(this);

        if (bestTime > 0)
            EntityFactory.createRecordBoard(world, spriteGUI, bestTime);

        timeStart = System.currentTimeMillis();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

        graphics.setBackground(Color.cyan);
//        world.getSystem(DrawEntityShapeSystem.class).process();
        world.getSystem(DrawDepthImageSystem.class).process();
        world.getSystem(DrawButtonSystem.class).process();

        if (gameContainer.getInput().isKeyDown(Input.KEY_TAB)) {

            graphics.setColor(Color.black);

            long pipesCount = world.getManager(GroupManager.class).getEntities("deleteWhenOutOfScreen").size();
            long active = world.getEntityManager().getActiveEntityCount();
            long created = world.getEntityManager().getTotalCreated();
            long deleted = world.getEntityManager().getTotalDeleted();
            long added = world.getEntityManager().getTotalAdded();

            graphics.drawString("pipes = " + pipesCount, 10, 5);
            graphics.drawString("active  = " + active, 10, 25);
            graphics.drawString("add-del = " + (added - deleted), 10, 45);

            graphics.drawString("created = " + created, 160, 5);
            graphics.drawString("added   = " + added, 160, 25);
            graphics.drawString("deleted = " + deleted, 160, 45);
            graphics.drawString("cre-del = " + (created - deleted), 310, 5);
            world.getSystem(DebugDrawEntityShapeSystem.class).process();
            world.getSystem(DebugDrawVelocitySystem.class).process();
        }

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

        world.setDelta(i);
        world.process();

        if (gameContainer.getInput().isKeyPressed(Input.KEY_ESCAPE))
            this.init(gameContainer, stateBasedGame);
    }

    @Override
    public void hasCollide(Entity entity) {

        world.getSystem(SpawnPipeSystem.class).setEnabled(false);
        Position position = entity.getComponent(Position.class);

        if (position == null)
            return;

        final BasicGameState basicGameState = this;
        ButtonHandler buttonHandler = new ButtonHandler() {
            @Override
            public void pressed() {
                try {
                    basicGameState.init(parent.getContainer(), parent);
                } catch (SlickException e) {
                    e.printStackTrace();
                }
            }
        };
        Entity button = GuiFactory.createButton(world, "restart", 250, 300, buttonHandler);
        button.addToWorld();

        float time = System.currentTimeMillis() - timeStart;

        if (time > bestTime) {
            bestTime = time;
            System.out.println("new best score!");
        }
    }
}
