package src.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import src.entity.EntityFactory;
import src.utils.SpriteGUI;

/**
 * User: eptwalabha
 * Date: 21/02/14
 * Time: 14:08
 */
public class SpawnPipeSystem extends IntervalEntitySystem {

    private SpriteGUI spriteGUI;

    public SpawnPipeSystem(float interval, SpriteGUI spriteGUI) {
        super(Aspect.getEmpty(), interval);
        this.spriteGUI = spriteGUI;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
//        EntityFactory.createPipe(world, (float)(Math.random() * 75 + 100));
        EntityFactory.createPipe(world, spriteGUI, 100f);
    }
}
