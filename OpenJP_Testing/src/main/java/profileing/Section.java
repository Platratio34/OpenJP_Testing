package profileing;

/**
 * Frame sub section
 */
public class Section {
    
    private String name;
    private long start;
    private long end;
    private long time = -1;

    /**
     * Create a new named section
     * @param name section name
     */
    public Section(String name) {
        this.name = name;
        start();
    }

    /**
     * Start the section timer
     */
    public void start() {
        start = System.currentTimeMillis();
    }

    /**
     * End the section timer
     */
    public void end() {
        end = System.currentTimeMillis();
        time = end - start;
    }

    /**
     * Get the current time
     * @return current time OR <code>-1</code> if the section is not yet ended
     */
    public long getTime() {
        return time;
    }
    /**
     * Get the name of the section
     * @return
     */
    public String getName() {
        return name;
    }
}
