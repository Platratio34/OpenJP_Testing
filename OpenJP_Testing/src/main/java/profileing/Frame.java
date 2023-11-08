package profileing;

import java.util.HashMap;

public class Frame {
    
    public int frame;
    private HashMap<String, Section> sections;
    private Section main;

    public Frame() {
        sections = new HashMap<String, Section>();
    }
    public Frame(int frame) {
        this.frame = frame;
        sections = new HashMap<String, Section>();
    }

    public void start() {
        main = new Section("frame-"+frame);
    }
    public void start(String name) {
        if(sections.containsKey(name)) {
            sections.get(name).start();
            return;
        }
        sections.put(name, new Section(name));
    }

    public void end() {
        main.end();
    }
    public void end(String name) {
        sections.get(name).end();
    }

    public long time() {
        return main.getTime();
    }
    public long time(String name) {
        return sections.get(name).getTime();
    }

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
