package systems;

import com.artemis.Entity;
import com.artemis.World;
import org.junit.Test;
import src.components.Limit;
import src.components.Position;
import src.systems.DeleteEntityOutOfLimitSystem;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 22/02/14
 * Time: 11:58
 */
public class TestDeleteEntitiesOutOfScreenSystem {

    @Test
    public void canDefineALimit() {

        Limit limit = new Limit(0f, 0f, 100f, 100f);

        assertThat(limit.x).isEqualTo(0f);
        assertThat(limit.y).isEqualTo(0f);
        assertThat(limit.getX()).isEqualTo(0f);
        assertThat(limit.getY()).isEqualTo(0f);

        assertThat(limit.isPositionInsideLimit(new Position(50f, 50f))).isTrue();
        assertThat(limit.isPositionInsideLimit(new Position(100, 100))).isTrue();
        assertThat(limit.isPositionInsideLimit(new Position(101, 100))).isFalse();
        assertThat(limit.isPositionInsideLimit(new Position(101, 101))).isFalse();

        limit.origin = new Position(60f, 60f);

        assertThat(limit.getX()).isEqualTo(60f);
        assertThat(limit.getY()).isEqualTo(60f);

        assertThat(limit.isPositionInsideLimit(new Position(50f, 50f))).isFalse();
        assertThat(limit.isPositionInsideLimit(new Position(100, 100))).isTrue();
        assertThat(limit.isPositionInsideLimit(new Position(101, 100))).isTrue();
        assertThat(limit.isPositionInsideLimit(new Position(120, 120))).isTrue();

        Position position = new Position(100f, 100f);
        position.origin = new Position(200f, 200f);

        limit.origin = new Position(200f, 200f);
        assertThat(limit.isPositionInsideLimit(position)).isTrue();

        position.origin.x += 10;
        assertThat(limit.isPositionInsideLimit(position)).isFalse();

    }

    @Test
    public void canDeleteAnEntityWithLimit() {

        World world = new World();
        world.initialize();

        DeleteEntityOutOfLimitSystem deleteEntityOutOfLimitSystem = new DeleteEntityOutOfLimitSystem();
        assertThat(deleteEntityOutOfLimitSystem.getActives().size()).isEqualTo(0);

        world.setSystem(deleteEntityOutOfLimitSystem);

        Entity entity = world.createEntity();
        Position position = new Position(500, 500);
        Limit limit = new Limit(-100, -100, 700, 700);

        entity.addComponent(position);
        entity.addComponent(limit);
        entity.addToWorld();

        world.process();
        assertThat(deleteEntityOutOfLimitSystem.getActives().size()).isEqualTo(1);

        position.x = -200;
        world.process();
        assertThat(deleteEntityOutOfLimitSystem.getActives().size()).isEqualTo(0);
    }



}
