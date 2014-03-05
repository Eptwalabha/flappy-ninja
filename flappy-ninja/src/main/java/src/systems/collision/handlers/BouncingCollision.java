package src.systems.collision.handlers;

import com.artemis.Entity;
import src.components.EntityShape;
import src.components.Velocity;
import src.systems.collision.CollisionHandler;
import src.systems.collision.CollisionListener;

/**
 * User: eptwalabha
 * Date: 02/03/14
 * Time: 00:25
 */
public class BouncingCollision extends CollisionHandler {
    @Override
    public void collide(Entity entityA, Entity entityB) {

        EntityShape entityShapeA = entityA.getComponent(EntityShape.class);
        EntityShape entityShapeB = entityB.getComponent(EntityShape.class);
        Velocity velocityA = entityA.getComponent(Velocity.class);

        if (entityShapeA == null || entityShapeB == null || velocityA == null)
            return;

        float coefficientAB = Math.abs(entityShapeA.getCoefficient(entityShapeB));
        float coefficient = Math.abs(entityShapeB.getCoefficient());

        if (coefficient < coefficientAB)
            velocityA.y *= -1.0f;
        else
            velocityA.x *= -1.0f;

        for (CollisionListener listener : collisionListeners)
            listener.hasCollide(entityA, entityB);
    }
}
