package src.systems.gui;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import src.components.Button;

/**
 * User: eptwalabha
 * Date: 22/02/14
 * Time: 00:17
 */
public class ButtonSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Button> buttonComponentMapper;

    private Input input;

    public ButtonSystem(GameContainer gameContainer) {
        super(Aspect.getAspectForAll(Button.class));
        input = gameContainer.getInput();
    }

    @Override
    protected void process(Entity entity) {

        if (input.isMousePressed(0)) {
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY();
            Button button = buttonComponentMapper.get(entity);
            if (mouseX >= button.position.x && mouseX <= button.position.x + button.width
                    && mouseY >= button.position.y && mouseY <= button.position.y + button.height)
                button.buttonHandler.pressed();
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
