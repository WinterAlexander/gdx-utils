package me.winter.gdx.utils.io;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Checks if the specified port is available
 * <p>
 * Created on 2017-01-30.
 *
 * @author Alexander Winter
 */
public class PortChecker {
	public static final int MIN_PORT_NUMBER = 1100;
	public static final int MAX_PORT_NUMBER = 49151;

	private PortChecker() {}

	public static boolean portAvailable(int port) {
		if(port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER)
			throw new IllegalArgumentException("Invalid port: " + port);

		try(ServerSocket ss = new ServerSocket(port)) {
			ss.setReuseAddress(true);
			return true;
		} catch(IOException ignored) {
			return false;
		}
	}
}
