package com.winteralexander.gdx.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.Integer.toHexString;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utilitary method to use hashing algorithms
 * <p>
 * Created on 2017-01-12.
 *
 * @author Alexander Winter
 */
public class Hash {
	private static final MessageDigest sha256Algorithm;

	static {
		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
		} catch(NoSuchAlgorithmException ex) {
			throw new Error("No SHA256 on machine", ex);
		}

		sha256Algorithm = algorithm;
	}

	private Hash() {}

	public static byte[] sha256AsBytes(String input) {
		byte[] hash;
		synchronized(sha256Algorithm) {
			hash = sha256Algorithm.digest(input.getBytes(UTF_8));
		}
		return hash;
	}

	public static String sha256(String input) {
		byte[] hash = sha256AsBytes(input);
		StringBuilder hexString = new StringBuilder();

		for(byte b : hash) {
			String hex = toHexString(0xff & b);

			if(hex.length() == 1)
				hexString.append('0');

			hexString.append(hex);
		}

		return hexString.toString();
	}
}
