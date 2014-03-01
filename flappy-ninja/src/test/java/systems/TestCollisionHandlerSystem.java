package systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import src.components.EntityShape;
import src.systems.collision.CollisionHandler;
import src.systems.collision.CollisionPair;import src.systems.collision.CollisionSystem;
import src.systems.collision.handlers.BasicCollisionHandler;
import src.systems.collision.CollisionListener;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 26/02/14
 * Time: 23:29
 */
public class TestCollisionHandlerSystem {

    private World world;
    private MockCollisionSystem collisionSystem;
    private GroupManager groupManager;
    private String groupA = "groupA";
    private String groupB = "groupB";

    @Before
    public void setUp() {
        world = new World();
        world.initialize();
        groupManager = new GroupManager();
        world.setManager(groupManager);
        collisionSystem = new MockCollisionSystem();

        world.setSystem(collisionSystem);
    }

    @Test
    public void canCreateACollisionSystem() {

        assertThat(collisionSystem.getActives().size()).isEqualTo(0);

        Entity entityA = createValidEntityForCollisionSystem(0, 0, 10, 10);
        Entity entityB = createNonValidEntity();
        createValidEntityForCollisionSystem(0, 0, 10, 10);
        createValidEntityForCollisionSystem(0, 0, 10, 10);

        assertThat(collisionSystem.getActives().size()).isEqualTo(3);

        entityA.disable();
        assertThat(collisionSystem.getActives().size()).isEqualTo(2);

        makeThisEntityValidForCollisionSystem(entityB, 0, 0, 10, 10);

        assertThat(collisionSystem.getActives().size()).isEqualTo(3);
    }

    @Test
    public void canCreateACollisionPairAndAddItToTheCollisionSystem() {

        MockCollisionPair mockCollisionPair = new MockCollisionPair(world, groupA, groupB);

        assertThat(collisionSystem.listOfCollisionPairs.size()).isEqualTo(0);
        assertThat(mockCollisionPair.assign).isFalse();

        collisionSystem.addNewCollisionPair(mockCollisionPair);

        assertThat(collisionSystem.listOfCollisionPairs.size()).isEqualTo(1);
        assertThat(mockCollisionPair.assign).isTrue();
    }

    @Test
    public void canCollisionPairAutomaticallyUpdatesWhenWorldChanges() {

        MockCollisionPair mockCollisionPair = new MockCollisionPair(world, groupA, groupB);

        assertThat(mockCollisionPair.getEntityGroupA.size()).isEqualTo(0);
        assertThat(mockCollisionPair.getEntityGroupB.size()).isEqualTo(0);

        Entity entityA = createValidEntityForCollisionSystem(0, 0, 10, 10);
        Entity entityB = createValidEntityForCollisionSystem(0, 0, 10, 10);
        Entity entityC = createValidEntityForCollisionSystem(0, 0, 10, 10);
        Entity entityD = createValidEntityForCollisionSystem(0, 0, 10, 10);

        addEntityToGroup(entityA, groupA);
        addEntityToGroup(entityB, groupB);
        addEntityToGroup(entityC, groupB);

        assertThat(collisionSystem.getActives().size()).isEqualTo(4);
        assertThat(mockCollisionPair.getEntityGroupA.size()).isEqualTo(1);
        assertThat(mockCollisionPair.getEntityGroupB.size()).isEqualTo(2);

        entityB.deleteFromWorld();
        assertThat(mockCollisionPair.getEntityGroupB.size()).isEqualTo(1);

        addEntityToGroup(entityD, groupA);
        assertThat(mockCollisionPair.getEntityGroupA.size()).isEqualTo(2);

    }

    @Test
    public void canTriggerCollisionHandlerWhenTwoEntitiesCollide() {

        CollisionHandler collisionHandler = new CollisionHandler() {

            CollisionListener collisionListener;
            @Override
            public void collide(Entity entityA, Entity entityB) {
                if (collisionListener != null)
                    collisionListener.hasCollide(entityA, entityB);
            }

            @Override
            public void clearAllCollisionListener() {
            }

            @Override
            public boolean addCollisionListener(CollisionListener collisionListener) {
                this.collisionListener = collisionListener;
                return true;
            }

            @Override
            public boolean removeCollisionListener(CollisionListener collisionListener) {
                return false;
            }
        };

        MockCollisionListener collisionListener = new MockCollisionListener();
        collisionHandler.addCollisionListener(collisionListener);

        MockCollisionPair mockCollisionPair = new MockCollisionPair(world, groupA, groupB, collisionHandler);
        collisionSystem.addNewCollisionPair(mockCollisionPair);

        Entity entityA = createValidEntityForCollisionSystem(0, 0, 10, 10);
        Entity entityB = createValidEntityForCollisionSystem(100, 0, 10, 10);
        Entity entityC = createValidEntityForCollisionSystem(110, -50, 100, 100);

        Shape shapeA = entityA.getComponent(EntityShape.class).shape;

        addEntityToGroup(entityA, groupA);
        addEntityToGroup(entityB, groupB);
        addEntityToGroup(entityC, groupB);

        assertThat(collisionSystem.listOfCollisionPairs.size()).isEqualTo(1);
        testTheGroupSize(groupA, 1);
        testTheGroupSize(groupB, 2);

        assertThat(collisionSystem.getActives().size()).isEqualTo(3);

        world.process();

        assertThat(collisionSystem.numberOfProcess).isEqualTo(1);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(collisionListener, 0);

        // A intersects with B
        shapeA.setLocation(90, 0);

        world.process();
        assertThat(collisionSystem.numberOfProcess).isEqualTo(2);
        assertThat(collisionSystem.getNumberOfCollision()).isEqualTo(1);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(collisionListener, 1);

        // A doesn't collide
        shapeA.setLocation(0, 0);

        world.process();
        assertThat(collisionSystem.getNumberOfCollision()).isEqualTo(0);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(collisionListener, 1);

        // A intersects with B and C
        shapeA.setLocation(105, 0);

        world.process();
        assertThat(collisionSystem.getNumberOfCollision()).isEqualTo(2);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(collisionListener, 3);

        // A intersects only with C (B isn't in groupB anymore)
        groupManager.remove(entityB, groupB);

        world.process();
        assertThat(collisionSystem.getNumberOfCollision()).isEqualTo(1);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(collisionListener, 4);

        // A doesn't intersects C, but C contains A, so the listener should be triggered
        shapeA.setLocation(150, 0);

        world.process();
        assertThat(collisionSystem.getNumberOfCollision()).isEqualTo(1);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(collisionListener, 5);

    }

    @Test
    public void canAddCollisionHandlerToCollisionPair() {

        CollisionHandler collisionHandlerA = new BasicCollisionHandler();
        CollisionHandler collisionHandlerB = new BasicCollisionHandler();

        MockCollisionListener mockCollisionListenerA1 = new MockCollisionListener();
        MockCollisionListener mockCollisionListenerA2 = new MockCollisionListener();
        MockCollisionListener mockCollisionListenerB1 = new MockCollisionListener();

        collisionHandlerA.addCollisionListener(mockCollisionListenerA1);
        collisionHandlerA.addCollisionListener(mockCollisionListenerA2);
        collisionHandlerB.addCollisionListener(mockCollisionListenerB1);

        CollisionPair collisionPair = new CollisionPair(world, groupA, groupB);
        collisionPair.addCollisionHandler(collisionHandlerA);
        collisionPair.addCollisionHandler(collisionHandlerB);

        collisionSystem.addNewCollisionPair(collisionPair);
        Entity entityA = createValidEntityForCollisionSystem(0, 0, 10, 10);
        Entity entityB = createValidEntityForCollisionSystem(100, 0, 10, 10);

        addEntityToGroup(entityA, groupA);
        addEntityToGroup(entityB, groupB);

        Shape shapeA = entityA.getComponent(EntityShape.class).shape;

        world.process();

        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerA1, 0);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerA2, 0);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerB1, 0);

        // shapeA collides with shapeB
        shapeA.setLocation(90, 0);

        world.process();

        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerA1, 1);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerA2, 1);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerB1, 1);

        assertThat(collisionHandlerA.removeCollisionListener(mockCollisionListenerA2)).isTrue();
        world.process();

        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerA1, 2);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerA2, 1);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerB1, 2);

        collisionHandlerA.clearAllCollisionListener();

        world.process();

        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerA1, 2);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerA2, 1);
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerB1, 3);

        // entityA is now both in groupA and groupB
        groupManager.removeFromAllGroups(entityA);
        groupManager.removeFromAllGroups(entityB);
        addEntityToGroup(entityA, groupA);
        addEntityToGroup(entityA, groupB);

        world.process();
        // shapeA shouldn't collide with itself, so nothing to trigger
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerB1, 3);

        addEntityToGroup(entityB, groupB);

        world.process();
        // shapeA shouldn't collide with itself, but, will collide with shapeB
        testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(mockCollisionListenerB1, 4);

    }

    private void testIfCollisionListenerHasBeenTriggeredACertainNumberOfTime(MockCollisionListener collisionListener, int numberOfTimeExpected) {
        assertThat(collisionListener.numberOfCollisions).isEqualTo(numberOfTimeExpected);
    }

    private void testTheGroupSize(String group, int expectedSize) {
        assertThat(groupManager.getEntities(group).size()).isEqualTo(expectedSize);
    }

    private Entity createNonValidEntity() {
        Entity entityD = world.createEntity();
        entityD.addToWorld();
        return entityD;
    }

    private void makeThisEntityValidForCollisionSystem(Entity entity, float positionX, float positionY, float width, float height) {
        entity.addComponent(new EntityShape(new Rectangle(positionX, positionY, width, height), Color.green, 0));
        entity.changedInWorld();
    }

    private Entity createValidEntityForCollisionSystem(float positionX, float positionY, float width, float height) {
        Entity entity = createNonValidEntity();
        makeThisEntityValidForCollisionSystem(entity, positionX, positionY, width, height);
        return entity;
    }

    private void addEntityToGroup(Entity entity, String groupName) {
        groupManager.add(entity, groupName);
    }

    private class MockCollisionSystem extends CollisionSystem {

        @Mapper
        ComponentMapper<EntityShape> entityShapeComponentMapper;

        public int numberOfProcess = 0;
        public List<CollisionPair> listOfCollisionPairs = new ArrayList<CollisionPair>();

        @Override
        protected void processEntities(ImmutableBag<Entity> entities) {
            super.processEntities(entities);
            numberOfProcess++;
        }

        @Override
        public void addNewCollisionPair(CollisionPair collisionPair) {
            super.addNewCollisionPair(collisionPair);
            collisionPair.setCollisionSystem(this);
            listOfCollisionPairs.add(collisionPair);
        }

        @Override
        public EntityShape getEntityShapeFor(Entity entity) {
            return entityShapeComponentMapper.getSafe(entity);
        }
    }

    private class MockCollisionPair extends CollisionPair {

        public boolean assign = false;
        public ImmutableBag<Entity> getEntityGroupA;
        public ImmutableBag<Entity> getEntityGroupB;

        public MockCollisionPair(World world, String groupA, String groupB, CollisionHandler... collisionHandlers) {
            super(world, groupA, groupB, collisionHandlers);
            getEntityGroupA = TestCollisionHandlerSystem.this.world.getManager(GroupManager.class).getEntities(groupA);
            getEntityGroupB = TestCollisionHandlerSystem.this.world.getManager(GroupManager.class).getEntities(groupB);
        }

        @Override
        public void setCollisionSystem(CollisionSystem collisionSystem) {
            super.setCollisionSystem(collisionSystem);
            assign = true;
        }
    }

    private class MockCollisionListener implements CollisionListener {

        public int numberOfCollisions = 0;

        @Override
        public void hasCollide(Entity entityA, Entity entityB) {
            numberOfCollisions++;
        }
    }
}
