package profileing;

public class Section {
    
    private String name;
    private long start;
    private long end;
    private long time = -1;

    public Section(String name) {
        this.name = name;
        start();
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end() {
        end = System.currentTimeMillis();
        time = end - start;
    }

    public long getTime() {
        return time;
    }
    public String getName() {
        return name;
    }
}
