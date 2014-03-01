package src.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import src.components.Camera;
import src.entities.EntityFactory;

/**
 * User: eptwalabha
 * Date: 21/02/14
 * Time: 14:08
 */
public class SpawnPipeSystem extends IntervalEntitySystem {

    private Camera camera;

    public SpawnPipeSystem(float interval, Camera cameraInformation) {
        super(Aspect.getEmpty(), interval);
        this.camera = cameraInformation;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
//        EntityFactory.createPipe(world, (float)(Math.random() * 75 + 100));
        EntityFactory.createPipe(world, camera, 100f);
    }
}
