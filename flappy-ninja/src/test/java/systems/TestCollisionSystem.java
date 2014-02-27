package systems;

import static org.fest.assertions.Assertions.assertThat;

import com.artemis.Entity;
import com.artemis.World;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;
import src.components.*;
import src.systems.collision.CheckCollisionSystem;import src.systems.collision.CollisionHandlerSystem;

import java.util.ArrayList;

/**
 * User: eptwalabha
 * Date: 26/02/14
 * Time: 23:29
 */
public class TestCollisionSystem {

    private World world;
    private CheckCollisionSystem checkCollisionSystem;
    private MockCollisionHandlerSystem collisionHandler;

    @Before
    public void setUp() {
        world = new World();
        world.initialize();
        checkCollisionSystem = new CheckCollisionSystem();
    }

    @Test
    public void canCreateACollisionCheckerSystemThatAcceptOnlyValidEntities() {

        world.setSystem(checkCollisionSystem);

        Entity entityA = getValidEntityForCollisionSystem(0, 0, 10, 10);
        Entity entityB = world.createEntity();
        entityB.addComponent(new Collide(new Rectangle(0, 0, 10, 10)));

        world.process();

        assertThat(entityA.getComponent(Collide.class).shape).isNotNull();
        assertThat(checkCollisionSystem.getActives().size()).isEqualTo(0);

        entityA.addToWorld();
        entityB.addToWorld();

        world.process();
        // entity B doesn't have a Position component
        assertThat(checkCollisionSystem.getActives().size()).isEqualTo(1);

    }

    @Test
    public void canCreateAPairOfCollision() {

        world.setSystem(checkCollisionSystem);

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

        assertThat(checkCollisionSystem.getNumberOfCollisionPair()).isEqualTo(0);

        checkCollisionSystem.setNewPairOfGroupCollision(groupA, groupB);
        assertThat(checkCollisionSystem.getNumberOfCollisionPair()).isEqualTo(1);

        checkCollisionSystem.removeAllPairOfCollision();
        assertThat(checkCollisionSystem.getNumberOfCollisionPair()).isEqualTo(0);

        checkCollisionSystem.setNewPairOfGroupCollision(groupA, groupB);

        groupA.add(entityA1);
        groupB.add(entityB1);
        groupB.add(entityB2);
        groupB.add(entityB3);

        world.process();

        assertThat(checkCollisionSystem.getActives().size()).isEqualTo(4);
        assertThat(checkCollisionSystem.getNumberOfCollisions()).isEqualTo(0);
        assertThat(checkCollisionSystem.getTotalNumberOfCollisions()).isEqualTo(0);

        Collide collideEntityA1 = entityA1.getComponent(Collide.class);
        collideEntityA1.shape.setLocation(50, 50);

        world.process();

        // entityA1 collides with entityB1
        assertThat(checkCollisionSystem.getNumberOfCollisions()).isEqualTo(1);
        assertThat(checkCollisionSystem.getTotalNumberOfCollisions()).isEqualTo(1);
        assertThat(entityA1.getComponent(Collision.class)).isNotNull();
        assertThat(entityA1.getComponent(Collision.class).collidingEntity).isEqualTo(entityB1);
        assertThat(entityB1.getComponent(Collision.class).collidingEntity).isEqualTo(entityA1);

        world.process();
        assertThat(checkCollisionSystem.getNumberOfCollisions()).isEqualTo(0);
        assertThat(checkCollisionSystem.getTotalNumberOfCollisions()).isEqualTo(1);

        // clear the collision component of entityA1
        entityA1.removeComponent(Collision.class);
        // entityA1 is now on the edge of entityB2
        collideEntityA1.shape.setLocation(140, 50);

        world.process();
        assertThat(checkCollisionSystem.getNumberOfCollisions()).isEqualTo(1);
        assertThat(checkCollisionSystem.getTotalNumberOfCollisions()).isEqualTo(2);
        assertThat(entityA1.getComponent(Collision.class).collidingEntity).isEqualTo(entityB2);
        assertThat(entityB1.getComponent(Collision.class).collidingEntity).isEqualTo(entityA1);
        assertThat(entityB2.getComponent(Collision.class).collidingEntity).isEqualTo(entityA1);

        entityB3.disable();
        entityB3.deleteFromWorld();

        assertThat(checkCollisionSystem.getActives().size()).isEqualTo(3);
        assertThat(groupB.size()).isEqualTo(2);
    }

    @Test
    public void canUpdateGroupsWhenAnEntityIsRemovedFromWorld() {

        world.setSystem(checkCollisionSystem);

        ArrayList<Entity> groupA = generateGroupOfEntitiesForCollision(5);
        ArrayList<Entity> groupB = generateGroupOfEntitiesForCollision(4);

        checkCollisionSystem.setNewPairOfGroupCollision(groupA, groupB);

        world.process();
        assertThat(groupA.size()).isEqualTo(5);
        assertThat(checkCollisionSystem.getActives().size()).isEqualTo(5 + 4);

        Entity entity = groupA.get(0);
        entity.deleteFromWorld();

        world.process();
        assertThat(groupA.size()).isEqualTo(5 - 1);
        assertThat(checkCollisionSystem.getActives().size()).isEqualTo(5 + 4 - 1);

    }

    @Test
    public void canManageAnEntityThatHasCollisionComponent() {

        world.setSystem(checkCollisionSystem);
        collisionHandler = new MockCollisionHandlerSystem();
        world.setSystem(collisionHandler, false);

        Entity entityA = getValidEntityForCollisionSystem(0, 0, 10, 10);
        entityA.addToWorld();

        Entity entityB = getValidEntityForCollisionSystem(20, 20, 100, 100);
        entityB.addToWorld();

        ArrayList<Entity> groupA = new ArrayList<Entity>();
        groupA.add(entityA);
        ArrayList<Entity> groupB = new ArrayList<Entity>();
        groupB.add(entityB);

        checkCollisionSystem.setNewPairOfGroupCollision(groupA, groupB);

        world.process();
        assertThat(checkCollisionSystem.getActives().size()).isEqualTo(2);
        assertThat(collisionHandler.getActives().size()).isEqualTo(0);
        assertThat(collisionHandler.insertedEntity).isEqualTo(0);

        entityA.getComponent(Collide.class).shape.setLocation(20, 20);

        world.process();

        assertThat(checkCollisionSystem.getNumberOfCollisions()).isEqualTo(1);
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

    public class MockCollisionHandlerSystem extends CollisionHandlerSystem {

        public int insertedEntity = 0;

        @Override
        protected void inserted(Entity entity) {
            super.inserted(entity);
            insertedEntity++;

        }
    }

}
