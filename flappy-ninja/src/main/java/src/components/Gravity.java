package src.components;

import com.artemis.Component;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:39
 */
public class Gravity extends Component {

    public float x;
    public float y;

    public Gravity(float y) {
        this.y = y;
        this.x = 0f;
    }
}
