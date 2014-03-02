package src.systems.collision.handlers;

import com.artemis.Entity;
import src.components.Death;
import src.systems.collision.CollisionHandler;

/**
 * User: eptwalabha
 * Date: 02/03/14
 * Time: 00:31
 */
public class KillingCollision extends CollisionHandler {
    @Override
    public void collide(Entity entityA, Entity entityB) {
        entityA.addComponent(new Death());
        entityA.changedInWorld();
    }
}
