package src.systems.collision.handlers;

import com.artemis.Entity;
import src.components.Score;
import src.components.Value;
import src.systems.collision.CollisionHandler;
import src.systems.collision.CollisionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * User: eptwalabha
 * Date: 01/03/14
 * Time: 17:30
 */
public class PointEarning implements CollisionHandler {

    private List<CollisionListener> collisionListeners;

    public PointEarning() {
        this.collisionListeners = new ArrayList<CollisionListener>();
    }

    @Override
    public void collide(Entity entityA, Entity entityB) {

        Score score = entityA.getComponent(Score.class);
        Value pointValue = entityB.getComponent(Value.class);
        if(score != null && pointValue != null)
            score.score += pointValue.value;

        entityB.deleteFromWorld();
    }

    @Override
    public void clearAllCollisionListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean addCollisionListener(CollisionListener collisionListener) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean removeCollisionListener(CollisionListener collisionListener) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
