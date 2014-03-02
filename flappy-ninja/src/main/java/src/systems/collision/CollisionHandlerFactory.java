package src.systems.collision;

import com.artemis.World;
import src.systems.collision.handlers.BouncingCollision;
import src.systems.collision.handlers.KillingCollision;
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

    public static CollisionPair getBouncingHandler(World world, String groupA, String groupB) {
        CollisionPair collisionBouncing = new CollisionPair(world, groupA, groupB);
        BouncingCollision collisionHandler = new BouncingCollision();
        collisionBouncing.addCollisionHandler(collisionHandler);
        return collisionBouncing;
    }

    public static CollisionPair getKillingHandler(World world, String groupA, String groupB) {
        CollisionPair collisionBouncing = new CollisionPair(world, groupA, groupB);
        KillingCollision collisionHandler = new KillingCollision();
        collisionBouncing.addCollisionHandler(collisionHandler);
        return collisionBouncing;
    }
}
