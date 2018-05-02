public class Timer {
	private long currentMS;
	private long lastMS;

	public final void updateMS() {
		currentMS = System.currentTimeMillis();
	}

	public final void updateLastMS() {
		lastMS = System.currentTimeMillis();
	}

	public final boolean hasTimePassedM(final long MS) {
		return currentMS >= lastMS + MS;
	}
}
