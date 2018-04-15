package de.unknown.api;

public enum WarShipRang {
	LEADER(3), ADMIN(2), MEMBER(1);
	private final int id;
	WarShipRang(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
	
	

}
