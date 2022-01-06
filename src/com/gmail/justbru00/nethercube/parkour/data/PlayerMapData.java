package com.gmail.justbru00.nethercube.parkour.data;

public class PlayerMapData {
	
	public static final long NO_BEST_TIME = -1;

	private String internalName;
	private int attempts;
	private int finishes;
	/**
	 * Milliseconds
	 */
	private long bestTime;
	
	public PlayerMapData(String _internalName) {
		internalName = _internalName;
	}
	
	

	public PlayerMapData(String internalName, int attempts, int finishes, long bestTime) {
		this.internalName = internalName;
		this.attempts = attempts;
		this.finishes = finishes;
		this.bestTime = bestTime;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public int getFinishes() {
		return finishes;
	}

	public void setFinishes(int finishes) {
		this.finishes = finishes;
	}

	public long getBestTime() {
		return bestTime;
	}

	public void setBestTime(long bestTime) {
		this.bestTime = bestTime;
	}
	
	
}
