package com.sample.jgit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashString {

	public static String hash(String stringToEncrypt) {
		String encryptedString = "";
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (messageDigest != null) {
			messageDigest.update(stringToEncrypt.getBytes());
			encryptedString = new String(messageDigest.digest());
		}
		return encryptedString;
	}

	public static void main(String[] args) {
		System.out.println(hash("abc"));
		System.out.println(hash("ab1c"));

		System.out.println(new String("abc").hashCode());
		System.out.println(new String("abc").hashCode());
		System.out.println(new String("absdfsdfsfssdfsdfsdfsdfsfs1c").hashCode());
	}
}
