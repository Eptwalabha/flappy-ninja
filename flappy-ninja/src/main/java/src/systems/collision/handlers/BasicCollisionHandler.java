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
public class BasicCollisionHandler implements CollisionHandler {

    private List<CollisionListener> collisionListeners;

    public BasicCollisionHandler() {
        collisionListeners = new ArrayList<CollisionListener>();
    }

    @Override
    public void collide(Entity entityA, Entity entityB) {
        for (CollisionListener collisionListener : collisionListeners)
            collisionListener.hasCollide(entityA, entityB);
    }

    @Override
    public void clearAllCollisionListener() {
        collisionListeners = new ArrayList<CollisionListener>();
    }

    @Override
    public boolean addCollisionListener(CollisionListener collisionListener) {
        return collisionListeners.add(collisionListener);
    }

    @Override
    public boolean removeCollisionListener(CollisionListener collisionListener) {
        return collisionListeners.remove(collisionListener);
    }
}
