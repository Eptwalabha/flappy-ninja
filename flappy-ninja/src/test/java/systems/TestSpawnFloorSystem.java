package systems;


import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.World;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;
import src.components.*;
import src.entity.EntityFactory;import src.systems.SpawnFloorSystem;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 25/02/14
 * Time: 20:23
 */
public class TestSpawnFloorSystem {

    private World world;
    private Entity camera;
    private SpawnFloorSystem spawnFloorSystem;
    private MockEntityManager mockEntityManager;

    @Before
    public void setUp() {

        world = new World();
        mockEntityManager = new MockEntityManager();

        world.setManager(mockEntityManager);
        world.initialize();

        camera = EntityFactory.createCamera(world, new Position(0, 0), 600, 20);

        spawnFloorSystem = new SpawnFloorSystem(camera.getComponent(Camera.class));
        world.setSystem(spawnFloorSystem);

    }

    @Test
    public void canCreateASpawnFloorSystem() throws SlickException {

        assertThat(spawnFloorSystem.getTileWidth()).isEqualTo(50);

        spawnFloorSystem.setTileWidth(100);
        assertThat(spawnFloorSystem.getTileWidth()).isEqualTo(100);

        world.setSystem(spawnFloorSystem);

    }

    @Test
    public void canSpawnFloorEntity() {

        Camera cameraInformation = camera.getComponent(Camera.class);

        assertThat(spawnFloorSystem.getTilesSpawned()).isEqualTo(0);
        int numberOfEntityOrigin = mockEntityManager.counter;
        float cameraFOVWidth = cameraInformation.screenWidth;

        testFloorTileGenerationIfCameraMoves(cameraInformation, numberOfEntityOrigin, 0);

        testFloorTileGenerationIfCameraMoves(cameraInformation, numberOfEntityOrigin, 90);

        testFloorTileGenerationIfCameraMoves(cameraInformation, numberOfEntityOrigin, 10);

        processTheWorld();

        Entity lastAddedTile = mockEntityManager.lastAddedEntity;
        Position positionOfTile = lastAddedTile.getComponent(Position.class);

        assertThat(positionOfTile).isNotNull();
        assertThat(positionOfTile.origin).isEqualTo(cameraInformation.cameraPosition.origin);
        assertThat(positionOfTile.getX()).isEqualTo(cameraInformation.cameraPosition.getX() + cameraFOVWidth + 2 * 50 - 50);
        assertThat(positionOfTile.getY()).isEqualTo(cameraInformation.cameraPosition.getY() + 2 * spawnFloorSystem.getTileWidth() / 3);

        assertThat(lastAddedTile.getComponent(EntityShape.class)).isNotNull();
        assertThat(lastAddedTile.getComponent(Collide.class)).isNotNull();
        assertThat(lastAddedTile.getComponent(Limit.class)).isNotNull();
        assertThat(lastAddedTile.getComponent(Limit.class).origin).isEqualTo(cameraInformation.cameraPosition);

    }

    private void testFloorTileGenerationIfCameraMoves(Camera cameraInformation, int numberOfEntityOrigin, int amount) {
        cameraInformation.cameraPosition.x += amount;
        processTheWorld();
        assertThat(spawnFloorSystem.getTilesSpawned()).isEqualTo(world.getEntityManager().getActiveEntityCount() - numberOfEntityOrigin);
    }

    private void processTheWorld() {
        world.process();
        world.process();
    }

    private class MockEntityManager extends EntityManager {

        public Entity lastAddedEntity;
        public int counter = 0;

        @Override
        public void added(Entity entity) {
            super.added(entity);
            lastAddedEntity = entity;
            counter++;
        }
    }

}
