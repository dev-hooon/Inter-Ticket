package dev.hooon.auth.entity;

public interface EncryptHelper {
	String encrypt(String password);
	boolean isMatch(String password, String hashed);
}
