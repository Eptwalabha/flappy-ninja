package miscellaneous;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import src.components.Death;
import src.components.EntityShape;
import src.components.Position;
import src.entities.EntityFactory;
import src.systems.collision.CollisionHandlerFactory;
import src.systems.collision.CollisionPair;
import src.systems.collision.CollisionSystem;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 02/03/14
 * Time: 00:16
 */
public class TestKillingAndBouncingCollisionHandler {

    private World world;
    private CollisionSystem collisionSystem;
    private String groupA = "groupA";
    private String groupB = "groupB";

    @Before
    public void setUp() {
        world = new World();
        world.initialize();

        GroupManager groupManager = new GroupManager();
        world.setManager(groupManager);

        collisionSystem = new CollisionSystem();
        world.setSystem(collisionSystem);
    }

    @Test
    public void canKillPlayerWhenItCollidesWithEnvironment() {

        Position origin = new Position();
        Entity player = EntityFactory.createNinja(world, origin, 0);
        Shape playerShape = player.getComponent(EntityShape.class).shape;
        addEntityToGroup(player, groupA);
        createAWall(origin, 100, 0, 10, 10);
        createAWall(origin, 0, 100, 10, 10);
        createAWall(origin, 100, 100, 10, 10);

        CollisionPair bouncing = CollisionHandlerFactory.getKillingHandler(world, groupA, groupB);
        collisionSystem.addNewCollisionPair(bouncing);

        world.process();
        assertThat(player.getComponent(Death.class)).isNull();

        playerShape.setLocation(100, 0);

        world.process();
        assertThat(player.getComponent(Death.class)).isNotNull();
    }

    @Test
    public void canPlayerBounceWhenItCollidesWithEnvironment() {

        Position origin = new Position();
        Entity player = EntityFactory.createNinja(world, origin, 0);
        Shape playerShape = player.getComponent(EntityShape.class).shape;
        addEntityToGroup(player, groupA);
        createAWall(origin, 100, 0, 10, 10);
        createAWall(origin, 0, 100, 10, 10);
        createAWall(origin, 100, 100, 10, 10);

        CollisionPair bouncing = CollisionHandlerFactory.getBouncingHandler(world, groupA, groupB);
        collisionSystem.addNewCollisionPair(bouncing);
    }

    private Entity createAWall(Position origin, float positionX, float positionY, float width, float height) {
        Entity wall = world.createEntity();
        wall.addComponent(new EntityShape(new Rectangle(positionX, positionY, width, height)));
        addEntityToGroup(wall, groupB);
        wall.addToWorld();
        return wall;
    }

    private void addEntityToGroup(Entity player, String group) {
        world.getManager(GroupManager.class).add(player, group);
    }
}
