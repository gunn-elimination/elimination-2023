package net.gunn.elimination.auth;

public class RegistrationClosedException extends Exception {
	public RegistrationClosedException() {
		super("Registration is closed.");
	}
}
