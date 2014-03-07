package miscellaneous;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import org.junit.Before;
import org.junit.Test;
import src.components.EntityShape;
import src.components.Position;
import src.components.Velocity;
import src.entities.EntityFactory;
import src.systems.collision.CollisionPair;
import src.systems.collision.CollisionSystem;
import src.systems.collision.handlers.BouncingCollision;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 07/03/14
 * Time: 10:09
 */
public class TestBouncingCollisionHandler {

    private World world;
    private CollisionSystem collisionSystem;
    private String groupB = "groupB";
    private Entity wallA;
    private CollisionPair collisionPair;
    private BouncingCollision bouncingHandler;
    private MockCollisionListener mockCollisionListener;
    private GroupManager groupManager;
    private Velocity playerVelocity;
    private Position playerPosition;

    @Before
    public void setUp() {

        world = new World();
        world.initialize();

        groupManager = new GroupManager();
        collisionSystem = new CollisionSystem();

        world.setManager(groupManager);
        world.setSystem(collisionSystem);

        mockCollisionListener = new MockCollisionListener();

        bouncingHandler = new BouncingCollision();
        bouncingHandler.addCollisionListener(mockCollisionListener);

        String groupA = "groupA";

        collisionPair = new CollisionPair(world, groupA, groupB);
        collisionPair.addCollisionHandler(bouncingHandler);

        collisionSystem.addNewCollisionPair(collisionPair);

        Position position = new Position();
        Entity player = EntityFactory.createNinja(world, position, 200);
        playerPosition = player.getComponent(Position.class);
        playerVelocity = player.getComponent(Velocity.class);
        addEntityToGroup(player, groupA);

        wallA = createAWall(position, -50, 50, 100, 100);
    }

    @Test
    public void canBounceToTheRightAfterCollisionFromLeft() {

        playerPosition.setLocation(40, 10);
        playerVelocity.setVelocity(-10, -10);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(1);
        assertThat(playerVelocity.x).isEqualTo(10);
        assertThat(playerVelocity.y).isEqualTo(-10);
        assertThat(playerPosition.x).isEqualTo(60);
        assertThat(playerPosition.y).isEqualTo(10);

    }

    @Test
    public void canBounceToTheLeftAfterCollisionFromRight() {

        playerPosition.setLocation(-60, 10);
        playerVelocity.setVelocity(10, -10);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(1);
        assertThat(playerVelocity.x).isEqualTo(-10);
        assertThat(playerVelocity.y).isEqualTo(-10);
        assertThat(playerPosition.x).isEqualTo(-80);
        assertThat(playerPosition.y).isEqualTo(10);
    }

    @Test
    public void canBounceDownCollisionFromTop() {

        playerPosition.setLocation(-10, -40);
        playerVelocity.setVelocity(-10, 10);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(1);
        assertThat(playerVelocity.x).isEqualTo(-10);
        assertThat(playerVelocity.y).isEqualTo(-10);
        assertThat(playerPosition.x).isEqualTo(-10);
        assertThat(playerPosition.y).isEqualTo(-60);
    }

    @Test
    public void canBounceUpAfterCollisionFromBottom() {

        playerPosition.setLocation(-10, 60);
        playerVelocity.setVelocity(-10, -10);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(1);
        assertThat(playerVelocity.x).isEqualTo(-10);
        assertThat(playerVelocity.y).isEqualTo(10);
        assertThat(playerPosition.x).isEqualTo(-10);
        assertThat(playerPosition.y).isEqualTo(80);
    }

    @Test
    public void canDetermineCoefficients() {

        Entity boxA = createAWall(new Position(0, 0), 0, 0, 200, 500);
        EntityShape entityShapeA = boxA.getComponent(EntityShape.class);

        Entity boxB = createAWall(new Position(0, 0), 0, 0, 500, 500);
        EntityShape entityShapeB = boxB.getComponent(EntityShape.class);
        Position positionB = boxB.getComponent(Position.class);

        Entity boxC = createAWall(new Position(0, 0), 0, 0, 500, 500);
        EntityShape entityShapeC = boxC.getComponent(EntityShape.class);
        Position positionC = boxC.getComponent(Position.class);

        assertThat(entityShapeA.getCoefficient()).isEqualTo(2.5f);
        assertThat(entityShapeB.getCoefficient()).isEqualTo(1);
        assertThat(entityShapeC.getCoefficient()).isEqualTo(1);

        positionB.setLocation(-100, 0);
        positionC.setLocation(100, 0);

        assertThat(entityShapeB.getCoefficient(entityShapeC)).isEqualTo(0);

        positionB.setLocation(-100, -50);
        positionC.setLocation(100, 50);

        assertThat(entityShapeB.getCoefficient(entityShapeC)).isEqualTo(0.5f);

        // when coefficient is infinity, the method returns 10;
        positionB.setLocation(100, -50);
        positionC.setLocation(100, 50);

        assertThat(entityShapeB.getCoefficient(entityShapeC)).isEqualTo(10f);
    }

    private Entity createAWall(Position origin, float positionX, float positionY, float width, float height) {
        Entity wall = world.createEntity();
        Position position = new Position(positionX, positionY);
        wall.addComponent(new EntityShape(position, width, height));
        wall.addComponent(position);
        addEntityToGroup(wall, groupB);
        wall.addToWorld();
        return wall;
    }

    private void addEntityToGroup(Entity player, String group) {
        world.getManager(GroupManager.class).add(player, group);
    }
}
