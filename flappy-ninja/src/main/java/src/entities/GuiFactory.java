package src.entities;

import com.artemis.Entity;
import com.artemis.World;
import org.newdawn.slick.Color;
import src.components.Button;
import src.components.Position;
import src.systems.gui.ButtonHandler;

/**
 * User: eptwalabha
 * Date: 22/02/14
 * Time: 00:33
 */
public class GuiFactory {


    public static Entity createButton(World world, String text, int posX, int posY, ButtonHandler buttonHandler) {

        Entity button = world.createEntity();

        Button buttonComponent = new Button(text, Color.orange);
        buttonComponent.setPosition(new Position(posX, posY));
        buttonComponent.buttonHandler = buttonHandler;

        button.addComponent(buttonComponent);

        button.addToWorld();
        return button;
    }
}
