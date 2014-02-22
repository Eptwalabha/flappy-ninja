package src.components;

import com.artemis.Component;
import org.newdawn.slick.Color;
import src.systems.gui.ButtonHandler;

/**
 * User: eptwalabha
 * Date: 22/02/14
 * Time: 00:20
 */
public class Button extends Component {
    public String text;
    public Color color;
    public float width;
    public float height;
    public Position position;
    public ButtonHandler buttonHandler = null;

    public Button(String text, Color color) {
        this.text = text;
        this.color = color;
        this.width = text.length() * 10 + 10;
        this.height = 25;
        position = new Position(0, 0);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
