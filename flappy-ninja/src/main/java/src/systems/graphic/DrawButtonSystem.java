package src.systems.graphic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import src.components.Button;
import src.components.Camera;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 15:01
 */
public class DrawButtonSystem extends EntityProcessingSystem {

    private Graphics graphics;
    @Mapper
    ComponentMapper<Button> buttonComponentMapper;

    public DrawButtonSystem(GameContainer gameContainer) {
        super(Aspect.getAspectForAll(Button.class));
        this.graphics = gameContainer.getGraphics();
    }

    @Override
    protected void process(Entity entity) {

        Button button = buttonComponentMapper.get(entity);

        graphics.setColor(button.color);
        graphics.fill(new Rectangle(button.position.x, button.position.y, button.width, button.height));
        graphics.setColor(Color.black);
        graphics.drawString(button.text, button.position.x + 5, button.position.y + 5);

    }
}
