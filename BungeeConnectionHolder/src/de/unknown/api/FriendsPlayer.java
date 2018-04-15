package de.unknown.api;

import java.util.HashSet;
import java.util.UUID;

public class FriendsPlayer {
	
	private int id;
	private UUID uuid;
	
	private HashSet<Integer> friends = new HashSet<>();
	
	private boolean friendsMsg, friendsFind, friendsJoin, friendsRequests;
	
	private FriendsPlayer(int id) {
		this.id = id;
//		PreparedStatement ps = 
	}
	
	private FriendsPlayer(UUID uuid) {
		
	}
	
	public void update() {
//		String db = friends.toString();
//		ps.setString(18, db.substring(1, db.length()-1));
	}
	
	public boolean addFriend(int friend) {
		return friends.add(friend);
	}
	
	public boolean removeFriend(int friend) {
		return friends.remove(friend);
	}

	public boolean isFriendsMsg() {
		return friendsMsg;
	}

	public void setFriendsMsg(boolean friendsMsg) {
		this.friendsMsg = friendsMsg;
	}

	public boolean isFriendsFind() {
		return friendsFind;
	}

	public void setFriendsFind(boolean friendsFind) {
		this.friendsFind = friendsFind;
	}

	public boolean isFriendsJoin() {
		return friendsJoin;
	}

	public void setFriendsJoin(boolean friendsJoin) {
		this.friendsJoin = friendsJoin;
	}

	public boolean isFriendsRequests() {
		return friendsRequests;
	}

	public void setFriendsRequests(boolean friendsRequests) {
		this.friendsRequests = friendsRequests;
	}

	public HashSet<Integer> getFriends() {
		return friends;
	}
	
	public int getId() {
		return id;
	}
	
	public UUID getUniqueId() {
		return uuid;
	}

}
