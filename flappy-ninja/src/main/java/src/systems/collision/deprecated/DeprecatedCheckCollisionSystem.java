package src.systems.collision.deprecated;

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
 * @deprecated
 */
public class DeprecatedCheckCollisionSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Collide> collideComponentMapper;
    private List<DeprecatedCollisionPair> listOfPairs;
    private int numberOfCollision;
    private int totalOfCollision;

    public DeprecatedCheckCollisionSystem() {
        super(Aspect.getAspectForAll(Collide.class, Position.class));
        listOfPairs = new ArrayList<DeprecatedCollisionPair>();
        totalOfCollision = 0;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        numberOfCollision = 0;

        for (DeprecatedCollisionPair pair : listOfPairs) {
            pair.processCollision();
            numberOfCollision += pair.getNbrOfCollisionForThisPair();
        }
        totalOfCollision += numberOfCollision;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void removed(Entity entity) {
        super.removed(entity);

        for (DeprecatedCollisionPair pair : listOfPairs)
            pair.removed(entity);
    }

    public void setNewPairOfGroupCollision(ArrayList<Entity> groupA, ArrayList<Entity> groupB) {
        listOfPairs.add(new DeprecatedCollisionPair(groupA, groupB));
    }

    public int getNumberOfCollisionPair() {
        return listOfPairs.size();
    }

    public void removeAllPairOfCollision() {
        listOfPairs = new ArrayList<DeprecatedCollisionPair>();
    }

    public int getNumberOfCollisions() {
        return numberOfCollision;
    }

    public int getTotalNumberOfCollisions() {
        return totalOfCollision;
    }

    public class DeprecatedCollisionPair {

        public ArrayList<Entity> groupA;
        public ArrayList<Entity> groupB;
        private int nbrOfCollisionForThisPair;

        public DeprecatedCollisionPair(ArrayList<Entity> groupA, ArrayList<Entity> groupB) {
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

        public void removed(Entity entity) {
            removeEntityFromGroup(entity, groupA);
            removeEntityFromGroup(entity, groupB);
        }

        private void removeEntityFromGroup(Entity entity, ArrayList<Entity> group) {
            if (group.contains(entity))
                group.remove(entity);
        }
    }
}