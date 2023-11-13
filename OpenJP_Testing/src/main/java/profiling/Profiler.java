package profiling;

import java.util.ArrayList;

/**
 * Simple profiler utility
 */
public class Profiler {
    
    private ArrayList<Frame> frames;
    private Frame cFrame;
    
    /**
     * Create a new profiler
     */
    public Profiler() {
        frames = new ArrayList<Frame>();
    }

    /**
     * Start a new frame.<br><br>
     * <b>Ends last frame if not yet done</b>
     * @return The frame that was just started
     */
    public Frame startFrame() {
        if(cFrame != null) cFrame.end();
        cFrame = new Frame(frames.size());
        cFrame.start();
        frames.add(cFrame);
        return cFrame;
    }

    /**
     * Get frame by frame number since profiling started
     * @param f frame number
     * @return Selected frame
     */
    public Frame getFrame(int f) {
        return frames.get(f);
    }
    /**
     * Get the most recently started frame
     * @return Last frame
     */
    public Frame getLastFrame() {
        return frames.get(frames.size()-1);
    }
    /**
     * Get frame by number of frames before current
     * @param f number of frames before current
     * @return Selected frame
     */
    public Frame getLastFrame(int f) {
        return frames.get(frames.size()-1-f);
    }

    /**
     * Start a section in the current frame
     * @param name
     */
    public void start(String name) {
        if(cFrame == null) {
            System.err.println("Tried to start section \""+name+"\", but no frame is current");
            return;
        }
        cFrame.start(name);
    }
    /**
     * End a section in the current frame
     * @param name
     */
    public void end(String name) {
        if(cFrame == null) {
            System.err.println("Tried to end section \""+name+"\", but no frame is current");
            return;
        }
        cFrame.end(name);
    }
    /**
     * End the current frame
     */
    public void endFrame() {
        if(cFrame == null) {
            System.err.println("Tried end frame, but no frame is current");
            return;
        }
        cFrame.end();
        cFrame = null;
    }
}
