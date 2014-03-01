package entities;

import com.artemis.Entity;
import com.artemis.World;
import org.junit.Before;
import org.junit.Test;
import src.components.Camera;
import src.components.Position;
import src.entities.EntityFactory;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 24/02/14
 * Time: 21:09
 */
public class TestEntityCamera {

    private World world;
    private Position positionZero;

    @Before
    public void getInitializedWorld() {
        world = new World();
        world.initialize();

        positionZero = new Position(0, 0);
    }

    @Test
    public void canCreateANewCamera() {

        Entity camera = EntityFactory.createCamera(world, positionZero);

        Position positionCamera = camera.getComponent(Position.class);
        Camera fieldOfView = camera.getComponent(Camera.class);

        assertThat(positionCamera).isNotNull();
        assertThat(positionCamera).isNotNull();
        assertThat(fieldOfView.screenWidth).isEqualTo(500f);
        assertThat(fieldOfView.screenHeight).isEqualTo(500f);

        camera = EntityFactory.createCamera(world, positionZero, 800, 600);
        fieldOfView = camera.getComponent(Camera.class);

        assertThat(fieldOfView.screenWidth).isEqualTo(800f);
        assertThat(fieldOfView.screenHeight).isEqualTo(600f);

    }

    @Test
    public void canTellIfAPositionIsInsideTheScreenFieldOfViewOrNot() {

        Entity cameraEntity = EntityFactory.createCamera(world, positionZero, 100, 100);

        Camera cameraComponent = cameraEntity.getComponent(Camera.class);
        Position cameraPosition = cameraEntity.getComponent(Position.class);

        Position pointToTest = new Position(50, 50);

        assertThat(cameraComponent.containsThisPoint(pointToTest)).isTrue();

        pointToTest.x = 200;
        pointToTest.y = 200;

        assertThat(cameraComponent.containsThisPoint(pointToTest)).isFalse();

        cameraPosition.x = 150;
        cameraPosition.y = 150;

        assertThat(cameraComponent.containsThisPoint(pointToTest)).isTrue();

        assertThat(cameraComponent.containsThisPoint(new Position(0, 200))).isFalse();
        assertThat(cameraComponent.containsThisPoint(new Position(251, 200))).isFalse();
        assertThat(cameraComponent.containsThisPoint(new Position(200, 0))).isFalse();
        assertThat(cameraComponent.containsThisPoint(new Position(200, 251))).isFalse();
        assertThat(cameraComponent.containsThisPoint(new Position(150, 150))).isTrue();
        assertThat(cameraComponent.containsThisPoint(new Position(250, 250))).isTrue();


    }

}
