package src.systems.collision.handlers;

import com.artemis.Entity;
import src.systems.collision.CollisionHandler;
import src.systems.collision.CollisionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * User: eptwalabha
 * Date: 01/03/14
 * Time: 15:10
 */
public class BasicCollisionHandler extends CollisionHandler {

    @Override
    public void collide(Entity entityA, Entity entityB) {
        for (CollisionListener collisionListener : collisionListeners)
            collisionListener.hasCollide(entityA, entityB);
    }
}
