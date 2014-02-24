package src.components;

import com.artemis.Component;
import org.newdawn.slick.Image;

/**
 * User: eptwalabha
 * Date: 23/02/14
 * Time: 00:22
 */
public class Texture extends Component {

    public int depth;
    public Image image;

    public Texture(Image image, int depth) {
        this.depth = depth;
        this.image = image;
    }
}
