package src.systems.collision.handlers;

import com.artemis.Entity;
import src.components.Death;
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
public class PointEarning extends CollisionHandler {

    @Override
    public void collide(Entity entityA, Entity entityB) {

        Score score = entityA.getComponent(Score.class);
        Value pointValue = entityB.getComponent(Value.class);
        if(score != null && pointValue != null && entityA.getComponent(Death.class) == null)
            score.score += pointValue.value;

        entityB.deleteFromWorld();
    }
}
