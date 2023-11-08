package profileing;

import java.util.ArrayList;

public class Profiler {
    
    private ArrayList<Frame> frames;
    private Frame cFrame;
    
    public Profiler() {
        frames = new ArrayList<Frame>();
    }

    public Frame startFrame() {
        if(cFrame != null) cFrame.end();
        cFrame = new Frame(frames.size());
        cFrame.start();
        frames.add(cFrame);
        return cFrame;
    }

    public Frame getFrame(int f) {
        return frames.get(f);
    }
    public Frame getLastFrame() {
        return frames.get(frames.size()-1);
    }
    public Frame getLastFrame(int f) {
        return frames.get(frames.size()-1-f);
    }

    public void start(String name) {
        cFrame.start(name);
    }
    public void end(String name) {
        cFrame.end(name);
    }
    public void endFrame() {
        cFrame.end();
        cFrame = null;
    }
}
