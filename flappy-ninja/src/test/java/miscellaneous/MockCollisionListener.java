package miscellaneous;

import com.artemis.Entity;
import src.systems.collision.CollisionListener;

/**
 * User: eptwalabha
 * Date: 07/03/14
 * Time: 10:13
 */

public class MockCollisionListener implements CollisionListener {

    public int collisionCounter = 0;

    @Override
    public void hasCollide(Entity entityA, Entity entityB) {
        collisionCounter++;
    }
}