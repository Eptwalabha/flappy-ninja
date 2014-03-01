package src.systems.collision;

import com.artemis.World;
import src.systems.collision.handlers.PointEarning;

/**
 * User: eptwalabha
 * Date: 01/03/14
 * Time: 20:00
 */
public class CollisionHandlerFactory {

    public static CollisionPair getCollisionPlayerPoint(World world, String groupA, String groupB) {
        CollisionPair collisionPlayerPoint = new CollisionPair(world, groupA, groupB);
        PointEarning collisionHandler = new PointEarning();
        collisionPlayerPoint.addCollisionHandler(collisionHandler);
        return collisionPlayerPoint;
    }
}
