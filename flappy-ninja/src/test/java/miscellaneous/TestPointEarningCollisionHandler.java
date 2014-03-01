package miscellaneous;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import src.components.EntityShape;
import src.components.Position;
import src.components.Score;
import src.components.Value;
import src.entities.EntityFactory;
import src.systems.collision.*;
import src.systems.collision.handlers.PointEarning;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 01/03/14
 * Time: 17:10
 */
public class TestPointEarningCollisionHandler {

    private World world;
    private CollisionSystem collisionSystem;
    private GroupManager groupManager;
    private String groupA = "groupA";
    private String groupB = "groupB";

    @Before
    public void setUp() {
        world = new World();
        world.initialize();

        groupManager = new GroupManager();
        world.setManager(groupManager);

        collisionSystem = new CollisionSystem();
        world.setSystem(collisionSystem);
    }

    @Test
    public void canGivePlayerAPointWhenColliding() {

        Position origin = new Position();
        Entity player = EntityFactory.createNinja(world, origin, 0);
        addEntityToGroup(player, groupA);
        Entity point1 = createAPoint(origin, 100, 0, 10, 1);
        Entity point2 = createAPoint(origin, 0, 100, 10, 2);
        addEntityToGroup(point1, groupB);
        addEntityToGroup(point2, groupB);

        Shape playerShape = player.getComponent(EntityShape.class).shape;
        Score score = player.getComponent(Score.class);

        CollisionPair collisionPlayerPoint = CollisionHandlerFactory.getCollisionPlayerPoint(world, groupA, groupB);
        collisionSystem.addNewCollisionPair(collisionPlayerPoint);

        world.process();
        assertThat(score.score).isEqualTo(0);

        playerShape.setLocation(100, 0);

        world.process();
        assertThat(score.score).isEqualTo(1);

        playerShape.setLocation(0, 100);

        world.process();
        assertThat(score.score).isEqualTo(1 + 2);

    }

    @Test
    public void canDeletePointAfterEarning() {

        Position origin = new Position();
        Entity player = EntityFactory.createNinja(world, origin, 0);
        addEntityToGroup(player, groupA);
        Entity point1 = createAPoint(origin, 100, 0, 10, 1);
        addEntityToGroup(point1, groupB);

        Shape playerShape = player.getComponent(EntityShape.class).shape;
        Score score = player.getComponent(Score.class);

        CollisionPair collisionPlayerPoint = CollisionHandlerFactory.getCollisionPlayerPoint(world, groupA, groupB);

        collisionSystem.addNewCollisionPair(collisionPlayerPoint);

        world.process();
        assertThat(score.score).isEqualTo(0);

        playerShape.setLocation(100, 0);

        world.process();
        assertThat(score.score).isEqualTo(1);

        world.process();
        assertThat(score.score).isEqualTo(1);

    }

    private Entity createAPoint(Position origin, float positionX, float positionY, float radius, int value) {

        Entity point = world.createEntity();
        point.addComponent(new Position(positionX, positionY, origin));
        point.addComponent(new EntityShape(new Circle(positionX, positionY, radius), Color.green, 0));
        point.addComponent(new Value(value));
        point.addToWorld();
        return point;
    }

    private void addEntityToGroup(Entity entity, String groupName) {
        groupManager.add(entity, groupName);
    }

    private class MockCollisionListener implements CollisionListener {


        @Override
        public void hasCollide(Entity entityA, Entity entityB) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
