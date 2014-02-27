package src.systems.graphic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import src.components.Camera;
import src.components.EntityShape;
import src.components.Position;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 15:01
 */
public class DebugDrawEntityShapeSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Position> positionComponentMapper;
    @Mapper
    ComponentMapper<EntityShape> entityShapeComponentMapper;

    private Graphics graphics;
    private Camera camera;
    private boolean fill = true;

    public DebugDrawEntityShapeSystem(GameContainer gameContainer, Camera camera) {
        super(Aspect.getAspectForAll(Position.class, EntityShape.class));
        this.graphics = gameContainer.getGraphics();
        this.camera = camera;
    }

    public void drawPlainShape(boolean fill) {
        this.fill = fill;
    }

    public void drawEntity(Entity entity) {

        Position position = positionComponentMapper.get(entity);
        EntityShape shape = entityShapeComponentMapper.get(entity);

        shape.shape.setLocation(position.getX() - camera.cameraPosition.getX(), camera.screenHeight - position.getY() - camera.cameraPosition.getY());
        graphics.setColor(shape.color);
        if (!fill)
            graphics.draw(shape.shape);
        else
            graphics.fill(shape.shape);
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

        int maxDepth = 0;

        for (Entity entity : entities) {
            int entityDepth = entityShapeComponentMapper.get(entity).depth;
            if (entityDepth > maxDepth)
                maxDepth = entityDepth;
        }

        for (int depth = maxDepth; depth >= 0; depth--) {
            for (Entity entity : entities) {
                if (entityShapeComponentMapper.get(entity).depth == depth)
                    drawEntity(entity);
            }
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
