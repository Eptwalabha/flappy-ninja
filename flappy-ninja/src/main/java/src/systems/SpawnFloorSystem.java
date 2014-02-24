package src.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import src.components.Camera;
import src.entity.EntityFactory;
import src.utils.SpriteGUI;

/**
 * User: eptwalabha
 * Date: 21/02/14
 * Time: 14:08
 */
public class SpawnFloorSystem extends IntervalEntitySystem {

    private SpriteGUI spriteGUI;
    private Camera camera;

    public SpawnFloorSystem(Camera cameraInformation, SpriteGUI spriteGUI) {
        super(Aspect.getEmpty(), 75);
        this.spriteGUI = spriteGUI;
        this.camera = cameraInformation;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        EntityFactory.createFloor(world, camera, spriteGUI);
    }
}
