package net.gunn.elimination.auth;

public class BannedUserException extends Exception {
	public BannedUserException(String email) {
		super("User " + email + " is banned");
	}
}
