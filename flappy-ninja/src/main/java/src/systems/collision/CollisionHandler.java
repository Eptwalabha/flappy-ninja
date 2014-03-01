package src.systems.collision;

import com.artemis.Entity;

/**
 * User: eptwalabha
 * Date: 01/03/14
 * Time: 09:58
 */
public interface CollisionHandler {

    void collide(Entity entityA, Entity entityB);
    void clearAllCollisionListener();
    boolean addCollisionListener(CollisionListener collisionListener);
    boolean removeCollisionListener(CollisionListener collisionListener);
}
