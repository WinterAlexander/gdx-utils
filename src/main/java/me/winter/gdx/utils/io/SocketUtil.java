package me.winter.gdx.utils.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Util class for sockets
 * <p>
 * Created on 2018-01-18.
 *
 * @author Alexander Winter
 */
public class SocketUtil {
	public static void applyHints(Socket socket, SocketHints hints) throws SocketException {
		socket.setPerformancePreferences(
				hints.performancePrefConnectionTime,
				hints.performancePrefLatency,
				hints.performancePrefBandwidth);
		socket.setTrafficClass(hints.trafficClass);
		socket.setTcpNoDelay(hints.tcpNoDelay);
		socket.setKeepAlive(hints.keepAlive);
		socket.setSendBufferSize(hints.sendBufferSize);
		socket.setReceiveBufferSize(hints.receiveBufferSize);
		socket.setSoLinger(hints.linger, hints.lingerDuration);
		socket.setSoTimeout(hints.socketTimeout);
	}

	public static void applyHints(ServerSocket serverSocket, ServerSocketHints hints) throws SocketException {
		serverSocket.setPerformancePreferences(
				hints.performancePrefConnectionTime,
				hints.performancePrefLatency,
				hints.performancePrefBandwidth);
		serverSocket.setReuseAddress(hints.reuseAddress);
		serverSocket.setSoTimeout(hints.acceptTimeout);
		serverSocket.setReceiveBufferSize(hints.receiveBufferSize);
	}

	public static boolean ping(String hostname, int port) {
		try {
			Gdx.net.newClientSocket(Protocol.TCP, hostname, port, null).dispose();
			return true;
		} catch(GdxRuntimeException ex) {
			return false;
		}
	}
}
