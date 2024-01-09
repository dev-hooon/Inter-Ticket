package dev.hooon.auth.entity;

public interface EncryptHelper {
	String encrypt(String plainPassword);
	boolean isMatch(String plainPassword, String hashedPassword);
}
