package com.winteralexander.gdx.utils.system;

import com.winteralexander.gdx.utils.io.PortChecker;
import com.winteralexander.gdx.utils.math.NumberUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static com.winteralexander.gdx.utils.error.ExceptionUtil.unchecked;

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

	/**
	 * Retrieves a list of CPU model installed on this system, using platform dependent techniques.
	 * This may spawn child process to retrieve information.
	 *
	 * @return cpus of this machine
	 * @throws ProcessErrorException if a process spawned to get the information encoutered an error
	 * @throws IOException if an I/O error occurs from launching a process
	 * @throws InterruptedException if the thread was interrupted while waiting for the process
	 * @throws UnsupportedOperationException if the current system is not supported
	 */
	public static String[] getCPUs() throws IOException, InterruptedException {
		if(isLinux()) {
			return Stream.of(ProcessUtil.execute("cat", "/proc/cpuinfo").split("\n\n"))
					.map(page -> Arrays.stream(page.split("\n"))
							.filter(line -> line.startsWith("model name"))
							.findFirst())
					.filter(Optional::isPresent)
					.map(Optional::get)
					.map(s -> s.substring(s.indexOf(":") + 1))
					.map(String::trim)
					.distinct()
					.toArray(String[]::new);
		}

		if(isWindows()) {
			return Stream.of(ProcessUtil.execute("wmic", "cpu", "get", "name").split("\n"))
					.skip(1)
					.map(String::trim)
					.filter(l -> !l.isEmpty())
					.toArray(String[]::new);
		}

		if(isMac()) {
			return ProcessUtil.execute("sysctl", "-n", "machdep.cpu.brand_string")
					.split("\n");
		}

		throw new UnsupportedOperationException("Operation not supported on this system");
	}

	public static String[] getGPUs() throws IOException, InterruptedException {
		if(isLinux()) {
			return Stream.of(ProcessUtil.execute("lspci").split("\n"))
					.filter(line -> line.contains(" VGA "))
					.map(line -> line.split(" ")[0])
					.map(id -> unchecked(() -> ProcessUtil.execute("lspci", "-v", "-s", id)))
					.map(page -> Arrays.stream(page.split("\n"))
							.filter(line -> line.contains("Subsystem:"))
							.findFirst())
					.filter(Optional::isPresent)
					.map(Optional::get)
					.map(s -> s.substring(s.indexOf(":") + 1))
					.map(String::trim)
					.toArray(String[]::new);

		}

		if(isWindows()) {
			return Stream.of(ProcessUtil.execute("powershell",
							"(Get-WmiObject Win32_VideoController).Name").split("\n"))
					.skip(1)
					.map(String::trim)
					.filter(l -> !l.isEmpty())
					.toArray(String[]::new);
		}

		if(isMac()) {
			return Stream.of(ProcessUtil.execute("system_profiler", "SPDisplaysDataType")
							.split("\n"))
					.filter(l -> l.startsWith("Chipset Model:"))
					.map(l -> l.substring(l.indexOf(':') + 1))
					.map(String::trim)
					.toArray(String[]::new);
		}

		throw new UnsupportedOperationException("Operation not supported on this system");
	}

	public static SystemMemory getRAM() throws IOException, InterruptedException {
		if(isLinux()) {
			String[] memInfo = ProcessUtil.execute("cat", "/proc/meminfo").split("\n");
			long memTotal = Stream.of(memInfo)
					.filter(line -> line.startsWith("MemTotal:"))
					.map(l -> l.substring(l.indexOf(":") + 1))
					.map(String::trim)
					.map(l -> l.split(" ")[0])
					.map(s -> NumberUtil.tryParseLong(s, -1L))
					.findFirst().orElse(-1L);
			long memFree = Stream.of(memInfo)
					.filter(line -> line.startsWith("MemFree:"))
					.map(l -> l.substring(l.indexOf(":") + 1))
					.map(String::trim)
					.map(l -> l.split(" ")[0])
					.map(s -> NumberUtil.tryParseLong(s, -1L))
					.findFirst().orElse(-1L);
			long memAvailable = Stream.of(memInfo)
					.filter(line -> line.startsWith("MemAvailable:"))
					.map(l -> l.substring(l.indexOf(":") + 1))
					.map(String::trim)
					.map(l -> l.split(" ")[0])
					.map(s -> NumberUtil.tryParseLong(s, -1L))
					.findFirst().orElse(-1L);

			if(memTotal < 0L || memFree < 0L || memAvailable < 0L)
				throw new IOException("Failed to parse output of /proc/meminfo");

			return new SystemMemory(memTotal * 1024L, memFree * 1024L, memAvailable * 1024L);
		}

		if(isWindows()) {
			long memTotal = Stream.of(ProcessUtil.execute("wmic", "OS", "get",
							"TotalVisibleMemorySize", "/Value").split("\n"))
					.filter(l -> l.startsWith("TotalVisibleMemorySize="))
					.map(l -> l.substring(l.indexOf('=') + 1))
					.map(String::trim)
					.map(s -> NumberUtil.tryParseLong(s, -1L))
					.findFirst().orElse(-1L);

			long memFree = Stream.of(ProcessUtil.execute("wmic", "OS", "get",
							"FreePhysicalMemory", "/Value").split("\n"))
					.filter(l -> l.startsWith("FreePhysicalMemory="))
					.map(l -> l.substring(l.indexOf('=') + 1))
					.map(String::trim)
					.map(s -> NumberUtil.tryParseLong(s, -1L))
					.findFirst().orElse(-1L);

			if(memTotal < 0L || memFree < 0L)
				throw new IOException("Failed to parse output of wmic for RAM");

			return new SystemMemory(memTotal * 1024L, memFree * 1024L, memFree * 1024L);
		}

		if(isMac()) {
			String test = ProcessUtil.execute("sysctl", "-n", "hw.memsize")
					.replaceAll("\n", "")
					.trim();
			long totalMemory = NumberUtil.tryParseLong(
					test, -1L);

			if(totalMemory < 0L)
				throw new IOException("Failed to retrieve total memory from sysctl");

			String[] vmStat = ProcessUtil.execute("vm_stat").split("\n");
			String pageSizeStr = "page size of";
			long pageSize = Stream.of(vmStat)
					.filter(l -> l.contains(pageSizeStr))
					.map(l -> l.substring(l.indexOf(pageSizeStr) + pageSizeStr.length()))
					.map(String::trim)
					.map(l -> l.split(" ")[0])
					.map(String::trim)
					.map(s -> NumberUtil.tryParseLong(s, -1L))
					.findFirst().orElse(-1L);

			if(pageSize < 0L)
				throw new IOException("Failed to retrieve page size of vm_stat");
			long pagesFree = Stream.of(vmStat)
					.filter(l -> l.contains("Pages free:"))
					.map(l -> l.substring(l.indexOf(':') + 1))
					.map(l -> l.split("\\.")[0])
					.map(String::trim)
					.map(s -> NumberUtil.tryParseLong(s, -1L))
					.findFirst().orElse(-1L);

			if(pagesFree < 0L)
				throw new IOException("Failed to retrieve free pages of vm_stat");

			long pagesSpeculative = Stream.of(vmStat)
					.filter(l -> l.contains("Pages speculative:"))
					.map(l -> l.substring(l.indexOf(':') + 1))
					.map(l -> l.split("\\.")[0])
					.map(String::trim)
					.map(s -> NumberUtil.tryParseLong(s, -1L))
					.findFirst().orElse(-1L);

			long freeMem = pagesFree * pageSize;
			long availableMem = freeMem;
			if(pagesSpeculative > 0L)
				availableMem += pagesSpeculative * pageSize;

			return new SystemMemory(totalMemory, freeMem, availableMem);
		}

		throw new UnsupportedOperationException("Operation not supported on this system");
	}

	public static class SystemMemory {
		/**
		 * Total, free and available system memory as bytes
		 */
		public final long total, free, available;

		public SystemMemory(long total, long free, long available) {
			this.total = total;
			this.free = free;
			this.available = available;
		}
	}
}
