package src.systems.collision.handlers;

import com.artemis.Entity;
import src.systems.collision.CollisionHandler;
import src.systems.collision.CollisionListener;

/**
 * User: eptwalabha
 * Date: 01/03/14
 * Time: 15:07
 */
public class EmptyCollisionHandler extends CollisionHandler {

    @Override
    public void collide(Entity a, Entity b) {}
}
