package src.gamestates;

import com.artemis.ComponentType;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import src.Constant;
import src.components.Camera;
import src.components.Position;
import src.components.Score;
import src.components.Velocity;
import src.entities.EntityFactory;
import src.entities.GuiFactory;
import src.systems.*;
import src.systems.collision.CollisionHandlerFactory;
import src.systems.collision.CollisionPair;
import src.systems.collision.CollisionSystem;
import src.systems.graphic.*;
import src.systems.gui.ButtonHandler;
import src.systems.gui.ButtonSystem;
import src.systems.collision.CollisionListener;

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
    private Entity camera;
    private Score scorePoint;

    @Override
    public int getID() {
        return Constant.BasicGameStateID.GAME;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

        // l'origine du jeu toutes les autres positions l'ont en référence
        Position worldOrigin = new Position(0, 0);
        float speed = 300;

//        SpriteGUI spriteGUI = new SpriteGUI("images/all_tiles.png", 12, 1);
        parent = stateBasedGame;
        world = new World();
        world.initialize();

        world.setManager(new GroupManager());

        camera = EntityFactory.createCamera(world, worldOrigin, gameContainer.getWidth(), gameContainer.getHeight());
        camera.addComponent(new Velocity(speed, 0));
//        Position cameraPosition = (Position) camera.getComponent(ComponentType.getTypeFor(Position.class));
        Camera cameraInformation = (Camera) camera.getComponent(ComponentType.getTypeFor(Camera.class));
        camera.addToWorld();

        // ajout des systèmes.
        CollisionSystem collisionSystem = prepareCollisionSystem();

        world.setSystem(collisionSystem);

        world.setSystem(new GravitySystem());
        world.setSystem(new VelocitySystem());

        world.setSystem(new InputSystem(gameContainer));

        world.setSystem(new SpawnPipeSystem(800, cameraInformation));
        world.setSystem(new SpawnFloorSystem(cameraInformation));
        world.setSystem(new DeleteEntityOutOfLimitSystem());

        world.setSystem(new DrawDepthImageSystem(cameraInformation), false);
        world.setSystem(new ButtonSystem(gameContainer));
        world.setSystem(new DebugDrawEntityShapeSystem(gameContainer, cameraInformation), false);
        world.setSystem(new DebugDrawVelocitySystem(gameContainer, cameraInformation), false);
        world.setSystem(new DebugDrawPositionSystem(gameContainer, cameraInformation), false);
        world.setSystem(new DrawButtonSystem(gameContainer), false);
        world.setSystem(new DeleteEntitySystem());

        Entity ninja = EntityFactory.createNinja(world, worldOrigin, speed);
        scorePoint = ninja.getComponent(Score.class);
        ninja.addToWorld();

        if (bestTime > 0)
            EntityFactory.createRecordBoard(world, worldOrigin, bestTime);

        timeStart = System.currentTimeMillis();
    }

    private CollisionSystem prepareCollisionSystem() {
        CollisionSystem collisionSystem = new CollisionSystem();

        // collision player-points
        CollisionPair collisionPlayerPoint = CollisionHandlerFactory.getCollisionPlayerPoint(world, Constant.Collision.PLAYER, Constant.Collision.POINT);
        collisionSystem.addNewCollisionPair(collisionPlayerPoint);

        // collision player-environment (death)
        CollisionPair collisionKilling = CollisionHandlerFactory.getKillingHandler(world, Constant.Collision.PLAYER, Constant.Collision.ENVIRONMENT);
        collisionSystem.addNewCollisionPair(collisionKilling);

        // collision player-environment (bouncing)
        CollisionPair collisionBouncing = CollisionHandlerFactory.getBouncingHandler(world, Constant.Collision.PLAYER, Constant.Collision.ENVIRONMENT);
        collisionSystem.addNewCollisionPair(collisionBouncing);

        return collisionSystem;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

        world.getSystem(DrawDepthImageSystem.class).process();
        world.getSystem(DrawButtonSystem.class).process();

        long pipesCount = scorePoint.score;
        long active = world.getEntityManager().getActiveEntityCount();
        long created = world.getEntityManager().getTotalCreated();
        long deleted = world.getEntityManager().getTotalDeleted();
        long added = world.getEntityManager().getTotalAdded();

        world.getSystem(DebugDrawEntityShapeSystem.class).process();
        world.getSystem(DebugDrawVelocitySystem.class).process();
        world.getSystem(DebugDrawPositionSystem.class).process();

        graphics.setColor(Color.white);

        graphics.drawString("score = " + pipesCount, 10, 5);
        graphics.drawString("active  = " + active, 10, 25);
        graphics.drawString("add-del = " + (added - deleted), 10, 45);

        graphics.drawString("created = " + created, 160, 5);
        graphics.drawString("added   = " + added, 160, 25);
        graphics.drawString("deleted = " + deleted, 160, 45);
        graphics.drawString("cre-del = " + (created - deleted), 310, 5);

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

        world.setDelta(i);
        world.process();

        if (gameContainer.getInput().isKeyPressed(Input.KEY_ESCAPE))
            this.init(gameContainer, stateBasedGame);
    }

    @Override
    public void hasCollide(Entity entityA, Entity entityB) {

        world.getSystem(SpawnPipeSystem.class).setEnabled(false);
        Position position = entityA.getComponent(Position.class);
        Velocity velocityCamera = camera.getComponent(Velocity.class);
        velocityCamera.x = 0;
        velocityCamera.y = 0;

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
