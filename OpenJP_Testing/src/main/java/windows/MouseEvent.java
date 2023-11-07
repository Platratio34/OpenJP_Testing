package windows;

public interface MouseEvent {
    public void onMouseButtonEvent(int button, int action, int mods);

    public void onMouseCursorEvent(double xPos, double yPos);
}
