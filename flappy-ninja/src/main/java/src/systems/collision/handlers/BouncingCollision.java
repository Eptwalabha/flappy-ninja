package src.systems.collision.handlers;

import com.artemis.Entity;
import org.newdawn.slick.Color;
import src.components.*;
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

//        velocityA.y *= 0.8f;
//        velocityA.x *= 0.8f;

        if (coefficient < coefficientAB) {

            float direction = (velocityA.y < 0) ? -1 : 1;
            velocityA.y *= -1.0f;

            float positionYA = entityShapeA.position.getY() - entityShapeA.height / 2;
            float positionYB = entityShapeB.position.getY() - entityShapeB.height / 2;
            float heightAB = entityShapeA.height + entityShapeB.height;
            float deltaHeightAB = Math.abs(2 * (positionYB - positionYA));

            entityShapeA.position.y += (deltaHeightAB - heightAB) * direction;

        } else {

            float direction = (velocityA.x < 0) ? -1 : 1;
            velocityA.x *= -1.0f;

            float positionXA = entityShapeA.position.getX() + entityShapeA.width / 2;
            float positionXB = entityShapeB.position.getX() + entityShapeB.width / 2;
            float widthAB = entityShapeA.width + entityShapeB.width;
            float deltaWidthAB = Math.abs(2 * (positionXB - positionXA));

            entityShapeA.position.x += (deltaWidthAB - widthAB) * direction;
        }

        for (CollisionListener listener : collisionListeners)
            listener.hasCollide(entityA, entityB);

        entityB.addComponent(new EntityColor(Color.pink));
        entityB.changedInWorld();
    }
}
