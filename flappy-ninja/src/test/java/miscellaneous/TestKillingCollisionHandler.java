package miscellaneous;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import org.junit.Before;
import org.junit.Test;
import src.components.Death;
import src.components.EntityShape;
import src.components.Position;
import src.components.Velocity;
import src.entities.EntityFactory;
import src.systems.collision.*;
import src.systems.collision.handlers.BouncingCollision;
import src.systems.collision.handlers.KillingCollision;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 02/03/14
 * Time: 00:16
 */
public class TestKillingCollisionHandler {

    private World world;
    private CollisionSystem collisionSystem;
    private String groupA = "groupA";
    private String groupB = "groupB";
    private MockCollisionListener mockCollisionListener;

    @Before
    public void setUp() {
        world = new World();
        world.initialize();

        GroupManager groupManager = new GroupManager();
        world.setManager(groupManager);

        mockCollisionListener = new MockCollisionListener();
        collisionSystem = new CollisionSystem();
        world.setSystem(collisionSystem);

        CollisionPair collisionPair = new CollisionPair(world, groupA, groupB);
        BouncingCollision bouncingHandler = new BouncingCollision();

        assertThat(collisionPair.addCollisionHandler(bouncingHandler)).isTrue();
        assertThat(bouncingHandler.addCollisionListener(mockCollisionListener)).isTrue();
        assertThat(collisionSystem.addNewCollisionPair(collisionPair)).isTrue();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(0);
    }

    @Test
    public void canKillPlayerWhenItCollidesWithEnvironment() {

        Position origin = new Position();
        Entity player = EntityFactory.createNinja(world, origin, 0);
        Position playerPosition = player.getComponent(Position.class);
        addEntityToGroup(player, groupA);
        createAWall(origin, 100, 0, 10, 10);
        createAWall(origin, 0, 100, 10, 10);
        createAWall(origin, 100, 100, 10, 10);

        CollisionPair collisionPair = new CollisionPair(world, groupA, groupB);
        KillingCollision killingCollision = new KillingCollision();
        MockCollisionListener mockCollisionListener = new MockCollisionListener();

        assertThat(collisionPair.addCollisionHandler(killingCollision)).isTrue();
        assertThat(killingCollision.addCollisionListener(mockCollisionListener)).isTrue();
        assertThat(collisionSystem.addNewCollisionPair(collisionPair)).isTrue();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(0);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(0);
        assertThat(player.getComponent(Death.class)).isNull();

        playerPosition.setLocation(100, 0);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(1);
        assertThat(player.getComponent(Death.class)).isNotNull();
    }

    @Test
    public void canVelocityChangeWhenPlayerCollidesWithEnvironment() {

        Position origin = new Position();
        createAWall(origin, -50, 50, 100, 100);
        Entity player = EntityFactory.createNinja(world, origin, 0);
        addEntityToGroup(player, groupA);

        Position playerPosition = player.getComponent(Position.class);
        Velocity playerVelocity = player.getComponent(Velocity.class);

        playerVelocity.setVelocity(0, -500);
        playerPosition.setLocation(-10, 50);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(1);
        assertVelocityValue(playerVelocity, 0, 500);

        playerVelocity.setVelocity(0, 500);
        playerPosition.setLocation(-10, -50);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(2);
        assertVelocityValue(playerVelocity, 0, -500);

        playerVelocity.setVelocity(500, 0);
        playerPosition.setLocation(-50, 0);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(3);
        assertVelocityValue(playerVelocity, -500, 0);

        playerVelocity.setVelocity(-500, 0);
        playerPosition.setLocation(50, 0);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(4);
        assertVelocityValue(playerVelocity, 500, 0);
    }

    @Test
    public void canVelocityChangeOnlyOnceWhenPlayerCollidesWithTwoElements() {

        Position origin = new Position();
        createAWall(origin, -50, 50, 100, 100);
        createAWall(origin, -25, 50, 50, 100);
        Entity player = EntityFactory.createNinja(world, origin, 0);
        addEntityToGroup(player, groupA);

        Position playerPosition = player.getComponent(Position.class);
        Velocity playerVelocity = player.getComponent(Velocity.class);

        playerVelocity.setVelocity(0, -500);
        playerPosition.setLocation(-10, 50);

        world.process();
        assertThat(mockCollisionListener.collisionCounter).isEqualTo(1);
        assertVelocityValue(playerVelocity, 0, 500);
        assertPositionValue(playerPosition, -10, 90);

    }

    private void assertVelocityValue(Velocity velocity, float expectedX, float expectedY) {
        assertThat(velocity.x).isEqualTo(expectedX);
        assertThat(velocity.y).isEqualTo(expectedY);
    }

    private void assertPositionValue(Position position, float expectedX, float expectedY) {
        assertThat(position.x).isEqualTo(expectedX);
        assertThat(position.y).isEqualTo(expectedY);
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
