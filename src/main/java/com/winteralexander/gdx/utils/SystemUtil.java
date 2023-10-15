package com.winteralexander.gdx.utils;

import com.winteralexander.gdx.utils.io.PortChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Utility class for system related methods
 * <p>
 * Created on 2018-03-31.
 *
 * @author Alexander Winter
 */
public class SystemUtil {
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
	}

	public static boolean isMac() {
		return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac");
	}

	public static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("nux");
	}

	public static boolean supportsCursorBlending() {
		return !isWindows();
	}

	/**
	 * Bug in question:
	 * <a href="https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8208479">
	 * https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8208479
	 * </a>
	 *
	 * @return true if the current machine experiences the Oracle bug, otherwise false
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static boolean hasOracleSocketBug() {
		try {
			int port = 10000;

			while(!PortChecker.portAvailable(port))
				port++;

			ServerSocket serverSocket = new ServerSocket(port);

			int fPort = port;
			new Thread(() -> {
				try {
					Socket socket = new Socket("localhost", fPort);
					OutputStream out = socket.getOutputStream();
					out.write(1);
					out.write(2);
					out.close();
					socket.close();
				} catch(Exception ex) {
					try {
						serverSocket.close();
					} catch(IOException ignored) {}
				}
			}).start();

			Socket socket = serverSocket.accept();

			InputStream in = socket.getInputStream();
			in.read();
			socket.getOutputStream().write(3);

			try {
				in.read();
			} catch(Exception ex) {
				socket.close();
				return true;
			}

			in.close();
			socket.close();
			return false;
		} catch(Throwable ignored) {
			return false;
		}
	}

	public static void sleepIfRequired(long millis) {
		if(millis > 0)
			sleep(millis);
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException ignored) {}
	}

	/**
	 * @see <a href="https://stackoverflow.com/a/32170974/4048657">See SO related answer</a>
	 */
	public static String getMacAddress() throws SocketException, UnknownHostException {
		InetAddress lanIp = null;

		String ipAddress;
		Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();

		while(net.hasMoreElements()) {
			NetworkInterface element = net.nextElement();
			Enumeration<InetAddress> addresses = element.getInetAddresses();

			while(addresses.hasMoreElements() && element.getHardwareAddress() != null && element.getHardwareAddress().length > 0) {
				InetAddress ip = addresses.nextElement();
				if(ip instanceof Inet4Address) {
					if(ip.isSiteLocalAddress()) {
						ipAddress = ip.getHostAddress();
						lanIp = InetAddress.getByName(ipAddress);
					}
				}
			}
		}

		if(lanIp == null)
			throw new UnknownHostException();

		NetworkInterface network = NetworkInterface.getByInetAddress(lanIp);

		if(network == null)
			throw new UnknownHostException();

		return toString(network.getHardwareAddress());
	}

	/**
	 * Takes a mac address as a byte array and creates a string that represents it
	 *
	 * @param macAddress address to transform to string
	 * @return mac address as string
	 */
	public static String toString(byte[] macAddress) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < macAddress.length; i++)
			sb.append(String.format("%02X%s", macAddress[i], (i < macAddress.length - 1) ? "-" : ""));

		return sb.toString();
	}
}
