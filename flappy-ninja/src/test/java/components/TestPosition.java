package components;

import org.junit.Test;
import src.components.Position;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 22/02/14
 * Time: 11:58
 */

public class TestPosition {

    @Test
    public void canCreateAPosition() {

        Position position = new Position();

        assertThat(position.x).isEqualTo(0f);
        assertThat(position.y).isEqualTo(0f);
        assertThat(position.getX()).isEqualTo(0f);
        assertThat(position.getY()).isEqualTo(0f);

        assertThat(position.origin).isNull();

        position.x += 10;
        position.y += 5;

        assertThat(position.getX()).isEqualTo(10f);
        assertThat(position.getY()).isEqualTo(5f);

        position.origin = new Position();
        position.origin.x += 10;
        position.origin.y += 10;

        assertThat(position.x).isEqualTo(10f);
        assertThat(position.getX()).isEqualTo(20f);
        assertThat(position.y).isEqualTo(5f);
        assertThat(position.getY()).isEqualTo(15f);

        position.origin = null;

        assertThat(position.x).isEqualTo(10f);
        assertThat(position.getX()).isEqualTo(10f);
        assertThat(position.y).isEqualTo(5f);
        assertThat(position.getY()).isEqualTo(5f);

    }

    @Test
    public void canTwoPositionsCanShareTheSameOrigin() {

        Position positionOrigin = new Position();
        Position positionA = new Position(0f, 10f, positionOrigin);
        Position positionB = new Position(20f, -10f, positionOrigin);

        assertThat(positionA.getX()).isEqualTo(0f);
        assertThat(positionA.getY()).isEqualTo(10f);
        assertThat(positionA.origin).isEqualTo(positionOrigin);

        assertThat(positionB.getX()).isEqualTo(20f);
        assertThat(positionB.getY()).isEqualTo(-10f);
        assertThat(positionB.origin).isEqualTo(positionA.origin);

        positionOrigin.x += 100;
        positionOrigin.y -= 100;

        assertThat(positionA.getX()).isEqualTo(100f);
        assertThat(positionA.getY()).isEqualTo(-90f);

        assertThat(positionB.getX()).isEqualTo(120f);
        assertThat(positionB.getY()).isEqualTo(-110f);

    }

    @Test
    public void canAttachAPositionToAnOrigin() {

        Position positionOrigin = new Position(100f, 100f);
        Position position = new Position(50f, 75f);

        assertThat(position.getX()).isEqualTo(50f);
        assertThat(position.x).isEqualTo(50f);
        assertThat(position.getY()).isEqualTo(75f);
        assertThat(position.y).isEqualTo(75f);
        assertThat(position.origin).isNull();

        position.attachToOrigin(positionOrigin);

        assertThat(position.getX()).isEqualTo(50f);
        assertThat(position.x).isEqualTo(50f - 100f);
        assertThat(position.getY()).isEqualTo(75f);
        assertThat(position.y).isEqualTo(75f - 100f);
        assertThat(position.origin).isNotNull();

    }

    @Test
    public void canDetachPositionFromOrigin() {

        Position positionOrigin = new Position(100f, 100f);
        Position position = new Position(50f, 75f);

        assertThat(position.getX()).isEqualTo(50f);
        assertThat(position.getY()).isEqualTo(75f);

        position.origin = positionOrigin;

        assertThat(position.getX()).isEqualTo(50f + 100f);
        assertThat(position.getY()).isEqualTo(75f + 100f);

        position.detachFromOrigin();

        assertThat(position.getX()).isEqualTo(150f);
        assertThat(position.getY()).isEqualTo(175f);
        assertThat(position.origin).isNull();


    }

}
