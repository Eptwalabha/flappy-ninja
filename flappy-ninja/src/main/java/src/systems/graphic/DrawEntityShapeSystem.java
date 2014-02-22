package src.systems.graphic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import src.components.EntityShape;
import src.components.Position;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 15:01
 */
public class DrawEntityShapeSystem extends EntityProcessingSystem {

    private Graphics graphics;
    @Mapper
    ComponentMapper<Position> positionComponentMapper;
    @Mapper
    ComponentMapper<EntityShape> entityShapeComponentMapper;

    public DrawEntityShapeSystem(GameContainer gameContainer) {
        super(Aspect.getAspectForAll(Position.class, EntityShape.class));
        this.graphics = gameContainer.getGraphics();
    }

    @Override
    protected void process(Entity entity) {

        Position position = positionComponentMapper.get(entity);
        EntityShape shape = entityShapeComponentMapper.get(entity);
        shape.shape.setLocation(position.x, 500 - position.y);
        graphics.setColor(shape.color);
        graphics.fill(shape.shape);
    }
}
