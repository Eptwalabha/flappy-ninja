package src.systems.collision.deprecated;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.geom.Shape;
import src.components.Collide;
import src.components.Collision;
import src.components.Position;
import src.components.Velocity;

/**
 * User: eptwalabha
 * Date: 27/02/14
 * Time: 22:18
 * @deprecated
 */
public class DeprecatedCollisionHandlerSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Collision> collisionComponentMapper;
    @Mapper
    ComponentMapper<Velocity> velocityComponentMapper;
    @Mapper
    ComponentMapper<Position> positionComponentMapper;
    @Mapper
    ComponentMapper<Collide> collideComponentMapper;

    public DeprecatedCollisionHandlerSystem() {
        super(Aspect.getAspectForAll(Collision.class, Collide.class));
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

        manageVelocity(velocity, positionEntityA, positionEntityB, shapeA, shapeB);

        entityA.removeComponent(Collision.class);
        entityA.changedInWorld();
    }

    protected void manageVelocity(Velocity velocity, Position positionEntityA, Position positionEntityB, Shape shapeA, Shape shapeB) {
        if (velocity == null || shapeB == null || positionEntityA == null || positionEntityB == null)
            return;

        float positionXA = positionEntityA.getX() + shapeA.getHeight() / 2;
        float positionYA = positionEntityA.getY() + shapeA.getWidth() / 2;
        float positionXB = positionEntityB.getX() + shapeB.getHeight() / 2;
        float positionYB = positionEntityB.getY() + shapeB.getWidth() / 2;

        float coeff = (positionYB - positionYA) / (positionXB - positionXA);
        float coeff2 = (shapeB.getHeight() / shapeB.getWidth());

        if (Math.abs(coeff) > Math.abs(coeff2))
            velocity.y *= -1;
        else
            velocity.x *= -1;
    }
}
