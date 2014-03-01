package src.systems.collision;

import com.artemis.*;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import src.components.EntityShape;

import java.util.ArrayList;
import java.util.List;

/**
 * User: eptwalabha
 * Date: 28/02/14
 * Time: 00:09
 */
public class CollisionSystem extends EntitySystem {

    @Mapper
    ComponentMapper<EntityShape> entityShapeComponentMapper;

    private List<CollisionPair> listOfCollisionPairs;
    private long numberOfCollision = 0;

    public CollisionSystem() {
        super(Aspect.getAspectForAll(EntityShape.class));
        listOfCollisionPairs = new ArrayList<CollisionPair>();
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

        numberOfCollision = 0;
        for (CollisionPair collisionPair : listOfCollisionPairs)
            numberOfCollision += collisionPair.process();

    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    public void addNewCollisionPair(CollisionPair collisionPair) {
        collisionPair.setCollisionSystem(this);
        listOfCollisionPairs.add(collisionPair);
    }

    public EntityShape getEntityShapeFor(Entity entity) {
        return entityShapeComponentMapper.getSafe(entity);
    }

    public long getNumberOfCollision() {
        return numberOfCollision;
    }
}