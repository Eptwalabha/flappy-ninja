package src.components;

import com.artemis.Component;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 21:04
 */
public class Friction extends Component {

    public float restitutionRatio;

    public Friction(float restitutionRatio) {
        this.restitutionRatio = restitutionRatio;
    }
}
