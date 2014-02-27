package src.systems;

import com.artemis.*;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import src.components.Collide;
import src.components.Collision;
import src.components.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:48
 */
public class CheckCollisionSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Collide> collideComponentMapper;
    private List<CollisionPair> listOfPairs;
    private int numberOfCollision;
    private int totalOfCollision;

    public CheckCollisionSystem() {
        super(Aspect.getAspectForAll(Collide.class, Position.class));
        listOfPairs = new ArrayList<CollisionPair>();
        totalOfCollision = 0;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        numberOfCollision = 0;

        for (CollisionPair pair : listOfPairs) {
            pair.processCollision();
            numberOfCollision += pair.getNbrOfCollisionForThisPair();
        }
        totalOfCollision += numberOfCollision;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    public void setNewPairOfGroupCollision(ArrayList<Entity> groupA, ArrayList<Entity> groupB) {
        listOfPairs.add(new CollisionPair(groupA, groupB));
    }

    public int getNumberOfCollisionPair() {
        return listOfPairs.size();
    }

    public void removeAllPairOfCollision() {
        listOfPairs = new ArrayList<CollisionPair>();
    }

    public int getNumberOfCollisions() {
        return numberOfCollision;
    }

    public int getTotalNumberOfCollisions() {
        return totalOfCollision;
    }

    public class CollisionPair {

        public ArrayList<Entity> groupA;
        public ArrayList<Entity> groupB;
        private int nbrOfCollisionForThisPair;

        public CollisionPair(ArrayList<Entity> groupA, ArrayList<Entity> groupB) {
            this.groupA = groupA;
            this.groupB = groupB;
        }

        public void processCollision() {

            nbrOfCollisionForThisPair = 0;
            for (Entity entityA : groupA) {

                if (entityHasAlreadyRegistersACollision(entityA))
                    continue;

                checkIfItCollideWithAnyEntityOfGroupB(entityA);
            }

        }

        public int getNbrOfCollisionForThisPair() {
            return nbrOfCollisionForThisPair;
        }

        private boolean entityHasAlreadyRegistersACollision(Entity entityA) {
            return entityA.getComponent(Collision.class) != null;
        }

        private void checkIfItCollideWithAnyEntityOfGroupB(Entity entityA) {
            for (Entity entityB : groupB)
                if (areEntitiesIntersecting(entityA, entityB))
                    registerCollisionForEntities(entityA, entityB);
        }

        private boolean areEntitiesIntersecting(Entity entityA, Entity entityB) {
            return collideComponentMapper.get(entityA).shape.intersects(collideComponentMapper.get(entityB).shape);
        }

        private void registerCollisionForEntities(Entity entityA, Entity entityB) {

            entityA.addComponent(new Collision(entityB));
            entityA.changedInWorld();

            entityB.addComponent(new Collision(entityA));
            entityB.changedInWorld();

            nbrOfCollisionForThisPair++;
        }
    }
}
