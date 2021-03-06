package settings;

public enum EvoTypes {
	
	EVO1OF1 (50,  80, 1, 3),
	EVO1OF2 (30,  60, 0, 1),
	EVO2OF2 (60,  90, 1, 3),
	EVO1OF3 (30,  50, 0, 1),
	EVO2OF3 (50,  70, 1, 2),
	EVO3OF3 (70, 120, 2, 3);
	
	private int minHP;
	private int maxHP;
	private int minRC;
	private int maxRC;

	EvoTypes (int minHP, int maxHP, int minRC, int maxRC) {
		
		this.minHP = minHP;
		this.maxHP = maxHP;
		this.minRC = minRC;
		this.maxRC = maxRC;
	}

	public int getMinHP() {
		return minHP;
	}

	public void setMinHP(int minHP) {
		this.minHP = minHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getMinRC() {
		return minRC;
	}

	public void setMinRC(int minRC) {
		this.minRC = minRC;
	}

	public int getMaxRC() {
		return maxRC;
	}

	public void setMaxRC(int maxRC) {
		this.maxRC = maxRC;
	}
	
}
