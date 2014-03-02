package systems;

import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.World;
import org.junit.Before;
import org.junit.Test;
import src.components.Limit;
import src.components.Position;
import src.components.ToDelete;
import src.systems.DeleteEntityOutOfLimitSystem;
import src.systems.DeleteEntitySystem;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: eptwalabha
 * Date: 22/02/14
 * Time: 11:58
 */
public class TestDeleteEntitiesSystem {


    private World world;
    private DeleteEntitySystem deleteSystem;
    private DeleteEntityOutOfLimitSystem deleteEntityOutOfLimitSystem;

    @Before
    public void setUp() {
        world = new World();
        world.initialize();

        deleteSystem = new DeleteEntitySystem();
        deleteEntityOutOfLimitSystem = new DeleteEntityOutOfLimitSystem();

        world.setSystem(deleteSystem, false);
        world.setSystem(deleteEntityOutOfLimitSystem, false);
    }

    @Test
    public void canDeleteEntity() {

        Entity entityA = addANewEntityInOurWorld();

        world.process();
        assertThat(world.getEntityManager().getActiveEntityCount()).isEqualTo(1);
        assertThat(deleteSystem.getActives().size()).isEqualTo(0);

        entityA.addComponent(new ToDelete());
        entityA.changedInWorld();
        assertThat(deleteSystem.getActives().size()).isEqualTo(1);

        deleteSystem.process();

        assertThat(world.getEntityManager().getActiveEntityCount()).isEqualTo(0);
        assertThat(deleteSystem.getActives().size()).isEqualTo(0);

    }

    private Entity addANewEntityInOurWorld() {
        Entity entityA = world.createEntity();
        entityA.addToWorld();
        return entityA;
    }

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

        assertThat(deleteEntityOutOfLimitSystem.getActives().size()).isEqualTo(0);
        assertThat(deleteSystem.getActives().size()).isEqualTo(0);

        Entity entity = addANewEntityInOurWorld();
        Position position = new Position(500, 500);
        Limit limit = new Limit(-100, -100, 700, 700);

        entity.addComponent(position);
        entity.addComponent(limit);
        entity.changedInWorld();

        world.process();

        assertThat(deleteEntityOutOfLimitSystem.getActives().size()).isEqualTo(1);
        assertThat(deleteSystem.getActives().size()).isEqualTo(0);
        assertThat(entity.getComponent(ToDelete.class)).isNull();

        position.x = -200;
        deleteEntityOutOfLimitSystem.process();

        assertThat(deleteEntityOutOfLimitSystem.getActives().size()).isEqualTo(0);
        assertThat(deleteSystem.getActives().size()).isEqualTo(1);
        assertThat(world.getEntityManager().isActive(entity.getId())).isTrue();
        assertThat(entity.getComponent(ToDelete.class)).isNotNull();

        deleteSystem.process();
        assertThat(deleteSystem.getActives().size()).isEqualTo(0);
        assertThat(deleteEntityOutOfLimitSystem.getActives().size()).isEqualTo(0);
    }


}
