package collision;

import game.Component;

public class Collider extends Component {

    protected Triangle[] triangles;
    
    public boolean checkCollision(Collider other) {
        for (int i = 0; i < triangles.length; i++) {
            for (int j = 0; j < other.triangles.length; i++) {
                if (triangles[i].collides(other.triangles[j])) {
                    return true;
                }
            }
        }
        return false;
    }

}
