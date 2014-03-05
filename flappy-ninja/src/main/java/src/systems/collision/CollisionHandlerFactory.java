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
        addEarningPointToCollisionPair(collisionPlayerPoint);
        return collisionPlayerPoint;
    }

    public static CollisionPair getBouncingHandler(World world, String groupA, String groupB) {
        CollisionPair collisionBouncing = new CollisionPair(world, groupA, groupB);
        addBouncingHandlerToCollisionPair(collisionBouncing);
        return collisionBouncing;
    }

    public static CollisionPair getKillingHandler(World world, String groupA, String groupB) {
        CollisionPair collisionBouncing = new CollisionPair(world, groupA, groupB);
        addKillingHandlerToCollisionPair(collisionBouncing);
        return collisionBouncing;
    }

    public static void addEarningPointToCollisionPair(CollisionPair collisionPlayerPoint) {
        PointEarning collisionHandler = new PointEarning();
        collisionPlayerPoint.addCollisionHandler(collisionHandler);
    }

    public static void addKillingHandlerToCollisionPair(CollisionPair collisionBouncing) {
        KillingCollision collisionHandler = new KillingCollision();
        collisionBouncing.addCollisionHandler(collisionHandler);
    }

    public static void addBouncingHandlerToCollisionPair(CollisionPair collisionPair) {
        BouncingCollision collisionHandler = new BouncingCollision();
        collisionPair.addCollisionHandler(collisionHandler);
    }
}
