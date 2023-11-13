package profileing;

import java.util.HashMap;

/**
 * Proflling frame
 */
public class Frame {
    
    /** Frame number */
    public int frame;
    private HashMap<String, Section> sections;
    private Section main;

    /**
     * Create a new frame
     */
    public Frame() {
        sections = new HashMap<String, Section>();
    }
    /**
     * Create a new frame, setting frame number
     * @param frame frame number
     */
    public Frame(int frame) {
        this.frame = frame;
        sections = new HashMap<String, Section>();
    }

    /**
     * Start the frame timer
     */
    public void start() {
        main = new Section("frame-"+frame);
    }
    /**
     * Start a section within the frame
     * @param name section name
     */
    public void start(String name) {
        if(sections.containsKey(name)) {
            sections.get(name).start();
            return;
        }
        sections.put(name, new Section(name));
    }

    /**
     * End the frame timer
     */
    public void end() {
        main.end();
    }
    /**
     * End a section within the frame
     * @param name section name
     */
    public void end(String name) {
        if(!sections.containsKey(name)) {
            System.err.println("Tried to end section \""+name+"\", but it was never started");
            return;
        }
        sections.get(name).end();
    }

    /**
     * Get the frame time
     * @return Frame time OR <code>-1</code> if the frame has not been ended
     */
    public long time() {
        return main.getTime();
    }
    /**
     * Get the time of a named section
     * @param name section name
     * @return Section time OR <code>-1</code> if the section does not exist or has not been ended
     */
    public long time(String name) {
        if(!sections.containsKey(name)) {
            System.err.println("Tried to get section time for \""+name+"\", but it was never created");
            return -1;
        }
        return sections.get(name).getTime();
    }

    /**
     * Creates a single line string representation of the frame and all sub-sections
     */
    public String toString() {
        String str = "{";
        str += "time "+time()+"ms";
        if(sections.size() > 0) {
            boolean c = false;
            str += "; sections:[";
            for (Section section : sections.values()) {
                if(c) str += ", ";
                str += "{"+section.getName()+": "+section.getTime()+"}";
                c = true;
            }
            str += "]";
        }
        return str + "}";
    }
}
