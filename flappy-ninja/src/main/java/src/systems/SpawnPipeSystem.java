package src.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import src.entity.EntityFactory;

/**
 * User: eptwalabha
 * Date: 21/02/14
 * Time: 14:08
 */
public class SpawnPipeSystem extends IntervalEntitySystem {
    public SpawnPipeSystem(float interval) {
        super(Aspect.getEmpty(), interval);
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
//        EntityFactory.createPipe(world, (float)(Math.random() * 75 + 100));
        EntityFactory.createPipe(world, 100f);
    }
}
