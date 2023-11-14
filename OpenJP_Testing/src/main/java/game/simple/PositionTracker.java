package game.simple;

import game.Component;

public class PositionTracker extends Component {
    
    @Override
    public void onTick() {
        System.out.println("Pos: "+gameObject.transform.getPosition());
    }
}

