package windows;

/**
 * Window loop runnable.
 */
public interface WindowLoopRunnable {
	
	/**
	 * Called during the window loop, before rendering
	 */
	public abstract void onLoop();
}
