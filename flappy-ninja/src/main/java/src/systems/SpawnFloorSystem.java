package src.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import src.components.Camera;
import src.entities.EntityFactory;

/**
 * User: eptwalabha
 * Date: 25/02/14
 * Time: 21:39
 */
public class SpawnFloorSystem extends EntitySystem {

    private float tileWidth;
    private int tilesSpawned;
    private Camera cameraInformation;

    public SpawnFloorSystem(Camera cameraInformation) {
        super(Aspect.getEmpty());
        tileWidth = 50;
        tilesSpawned = 0;
        this.cameraInformation = cameraInformation;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(float tileWidth) {
        this.tileWidth = tileWidth;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        tilesSpawned += EntityFactory.createFloor(world, cameraInformation, tileWidth, tilesSpawned);
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    public int getTilesSpawned() {
        return tilesSpawned;
    }
}
