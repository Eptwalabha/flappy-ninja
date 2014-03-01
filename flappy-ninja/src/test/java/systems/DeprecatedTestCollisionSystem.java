package systems;

import static org.fest.assertions.Assertions.assertThat;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import src.components.*;
import src.systems.collision.deprecated.DeprecatedCheckCollisionSystem;import src.systems.collision.deprecated.DeprecatedCollisionHandlerSystem;

import java.util.ArrayList;

/**
 * User: eptwalabha
 * Date: 26/02/14
 * Time: 23:29
 */
public class DeprecatedTestCollisionSystem {

    private World world;
    private DeprecatedCheckCollisionSystem deprecatedCheckCollisionSystem;
    private MockDeprecatedCollisionHandlerSystem collisionHandler;

    @Before
    public void setUp() {
        world = new World();
        world.initialize();
        world.setManager(new GroupManager());
        deprecatedCheckCollisionSystem = new DeprecatedCheckCollisionSystem();
    }

    @Test
    public void canCreateACollisionCheckerSystemThatAcceptOnlyValidEntities() {

        world.setSystem(deprecatedCheckCollisionSystem);

        Entity entityA = getValidEntityForCollisionSystem(0, 0, 10, 10);
        Entity entityB = world.createEntity();
        entityB.addComponent(new Collide(new Rectangle(0, 0, 10, 10)));

        world.process();

        assertThat(entityA.getComponent(Collide.class).shape).isNotNull();
        assertThat(deprecatedCheckCollisionSystem.getActives().size()).isEqualTo(0);

        entityA.addToWorld();
        entityB.addToWorld();

        world.process();
        // entities B doesn't have a Position component
        assertThat(deprecatedCheckCollisionSystem.getActives().size()).isEqualTo(1);

    }

    @Test
    public void canCreateAPairOfCollision() {

        world.setSystem(deprecatedCheckCollisionSystem);

        ArrayList<Entity> groupA = new ArrayList<Entity>();
        ArrayList<Entity> groupB = new ArrayList<Entity>();

        Entity entityA1 = getValidEntityForCollisionSystem(0, 0, 10, 10);
        entityA1.addToWorld();
        Entity entityB1 = getValidEntityForCollisionSystem(50, 50, 10, 100);
        Entity entityB2 = getValidEntityForCollisionSystem(150, 50, 10, 100);
        Entity entityB3 = getValidEntityForCollisionSystem(250, 50, 10, 100);
        entityB1.addToWorld();
        entityB2.addToWorld();
        entityB3.addToWorld();

        assertThat(deprecatedCheckCollisionSystem.getNumberOfCollisionPair()).isEqualTo(0);

        deprecatedCheckCollisionSystem.setNewPairOfGroupCollision(groupA, groupB);
        assertThat(deprecatedCheckCollisionSystem.getNumberOfCollisionPair()).isEqualTo(1);

        deprecatedCheckCollisionSystem.removeAllPairOfCollision();
        assertThat(deprecatedCheckCollisionSystem.getNumberOfCollisionPair()).isEqualTo(0);

        deprecatedCheckCollisionSystem.setNewPairOfGroupCollision(groupA, groupB);

        groupA.add(entityA1);
        groupB.add(entityB1);
        groupB.add(entityB2);
        groupB.add(entityB3);

        world.process();

        assertThat(deprecatedCheckCollisionSystem.getActives().size()).isEqualTo(4);
        assertThat(deprecatedCheckCollisionSystem.getNumberOfCollisions()).isEqualTo(0);
        assertThat(deprecatedCheckCollisionSystem.getTotalNumberOfCollisions()).isEqualTo(0);

        Collide collideEntityA1 = entityA1.getComponent(Collide.class);
        collideEntityA1.shape.setLocation(50, 50);

        world.process();

        // entityA1 collides with entityB1
        assertThat(deprecatedCheckCollisionSystem.getNumberOfCollisions()).isEqualTo(1);
        assertThat(deprecatedCheckCollisionSystem.getTotalNumberOfCollisions()).isEqualTo(1);
        assertThat(entityA1.getComponent(Collision.class)).isNotNull();
        assertThat(entityA1.getComponent(Collision.class).collidingEntity).isEqualTo(entityB1);
        assertThat(entityB1.getComponent(Collision.class).collidingEntity).isEqualTo(entityA1);

        world.process();
        assertThat(deprecatedCheckCollisionSystem.getNumberOfCollisions()).isEqualTo(0);
        assertThat(deprecatedCheckCollisionSystem.getTotalNumberOfCollisions()).isEqualTo(1);

        // clear the collision component of entityA1
        entityA1.removeComponent(Collision.class);
        // entityA1 is now on the edge of entityB2
        collideEntityA1.shape.setLocation(140, 50);

        world.process();
        assertThat(deprecatedCheckCollisionSystem.getNumberOfCollisions()).isEqualTo(1);
        assertThat(deprecatedCheckCollisionSystem.getTotalNumberOfCollisions()).isEqualTo(2);
        assertThat(entityA1.getComponent(Collision.class).collidingEntity).isEqualTo(entityB2);
        assertThat(entityB1.getComponent(Collision.class).collidingEntity).isEqualTo(entityA1);
        assertThat(entityB2.getComponent(Collision.class).collidingEntity).isEqualTo(entityA1);

        entityB3.disable();
        entityB3.deleteFromWorld();

        assertThat(deprecatedCheckCollisionSystem.getActives().size()).isEqualTo(3);
        assertThat(groupB.size()).isEqualTo(2);
    }

    @Test
    public void canUpdateGroupsWhenAnEntityIsRemovedFromWorld() {

        world.setSystem(deprecatedCheckCollisionSystem);

        ArrayList<Entity> groupA = generateGroupOfEntitiesForCollision(5);
        ArrayList<Entity> groupB = generateGroupOfEntitiesForCollision(4);

        deprecatedCheckCollisionSystem.setNewPairOfGroupCollision(groupA, groupB);

        world.process();
        assertThat(groupA.size()).isEqualTo(5);
        assertThat(deprecatedCheckCollisionSystem.getActives().size()).isEqualTo(5 + 4);

        Entity entity = groupA.get(0);
        entity.deleteFromWorld();

        world.process();
        assertThat(groupA.size()).isEqualTo(5 - 1);
        assertThat(deprecatedCheckCollisionSystem.getActives().size()).isEqualTo(5 + 4 - 1);

    }

    @Test
    public void canManageAnEntityThatHasCollisionComponent() {

        world.setSystem(deprecatedCheckCollisionSystem);
        collisionHandler = new MockDeprecatedCollisionHandlerSystem();
        world.setSystem(collisionHandler, false);

        Entity entityA = getValidEntityForCollisionSystem(0, 0, 10, 10);
        entityA.addToWorld();

        Entity entityB = getValidEntityForCollisionSystem(20, 20, 100, 100);
        entityB.addToWorld();

        ArrayList<Entity> groupA = new ArrayList<Entity>();
        groupA.add(entityA);
        ArrayList<Entity> groupB = new ArrayList<Entity>();
        groupB.add(entityB);

        deprecatedCheckCollisionSystem.setNewPairOfGroupCollision(groupA, groupB);

        world.process();
        assertThat(deprecatedCheckCollisionSystem.getActives().size()).isEqualTo(2);
        assertThat(collisionHandler.getActives().size()).isEqualTo(0);
        assertThat(collisionHandler.insertedEntity).isEqualTo(0);

        entityA.getComponent(Collide.class).shape.setLocation(20, 20);

        world.process();

        assertThat(deprecatedCheckCollisionSystem.getNumberOfCollisions()).isEqualTo(1);
        assertThat(collisionHandler.insertedEntity).isEqualTo(2);

        collisionHandler.process();

        assertThat(collisionHandler.getActives().size()).isEqualTo(0);
        assertThat(entityA.getComponent(Collision.class)).isNull();
        assertThat(entityB.getComponent(Collision.class)).isNull();

    }

    private Entity getValidEntityForCollisionSystem(float posX, float posY, float width, float height) {
        Entity entityA = world.createEntity();
        entityA.addComponent(new Collide(new Rectangle(posX, posY, width, height)));
        entityA.addComponent(new Position(posX, posY));
        return entityA;
    }

    private ArrayList<Entity> generateGroupOfEntitiesForCollision(int numberOfValidEntityForGroupA) {
        ArrayList<Entity> group = new ArrayList<Entity>();
        for (int i = 0; i < numberOfValidEntityForGroupA; i++) {
            Entity entity = getValidEntityForCollisionSystem(20 * i, 0, 10, 10);
            group.add(entity);
            entity.addToWorld();
        }
        return group;
    }

    public class MockDeprecatedCollisionHandlerSystem extends DeprecatedCollisionHandlerSystem {

        @Mapper
        ComponentMapper<Collision> collisionComponentMapper;
        @Mapper
        ComponentMapper<Velocity> velocityComponentMapper;
        @Mapper
        ComponentMapper<Position> positionComponentMapper;
        @Mapper
        ComponentMapper<Collide> collideComponentMapper;
        public int insertedEntity = 0;

        @Override
        protected void inserted(Entity entity) {
            super.inserted(entity);
            insertedEntity++;

        }

        @Override
        protected void process(Entity entityA) {

            Collision collision = collisionComponentMapper.get(entityA);
            Shape shapeA = collideComponentMapper.get(entityA).shape;
            Velocity velocity = velocityComponentMapper.getSafe(entityA);
            Position positionEntityA = positionComponentMapper.getSafe(entityA);

            Entity entityB = collision.collidingEntity;
            Position positionEntityB = positionComponentMapper.getSafe(entityB);
            Shape shapeB = collideComponentMapper.getSafe(entityB).shape;

            super.manageVelocity(velocity, positionEntityA, positionEntityB, shapeA, shapeB);

            entityA.removeComponent(Collision.class);
            entityA.changedInWorld();
        }
    }

}
