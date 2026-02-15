package com.winteralexander.gdx.utils.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.system.SystemUtil;
import com.winteralexander.gdx.utils.Validation;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Gives access to useful method while working with files and resources
 * <p>
 * Created on 2016-01-10.
 *
 * @author Alexander Winter
 */
public class FileUtil {
	private FileUtil() {}

	public static void appendToFile(File file, String line) {
		try {
			if(!file.isDirectory() && !file.getParentFile().mkdirs())
				throw new IOException("Failed to create directory " + file.getParentFile());

			if(!file.exists() && !file.createNewFile())
				throw new IOException("Failed to create file" + file);

			try(FileWriter writer = new FileWriter(file, true)) {
				writer.write(line);
			}
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Ensures specified directory exists
	 *
	 * @param directory directory to be sure exists
	 * @throws IOException if an I/O error occurs
	 */
	public static void ensureDirectory(File directory) throws IOException {
		Validation.ensureNotNull(directory, "directory");

		if(directory.exists()) {
			if(directory.isDirectory())
				return;

			if(!directory.delete())
				throw new IOException("Can't delete file " + directory.getAbsolutePath() + " to get equivalent directory");
		}

		if(!directory.mkdirs() && (!directory.isDirectory()))
			throw new IOException("Can't create directory " + directory.getAbsolutePath());
	}

	/**
	 * Ensures specified file exists
	 *
	 * @param file file to be sure exists
	 * @throws IOException if an I/O error occurs
	 */
	public static void ensureFile(File file) throws IOException {
		Validation.ensureNotNull(file, "file");

		if(file.getParentFile() != null)
			ensureDirectory(file.getParentFile());

		if(file.exists()) {
			if(file.isFile())
				return;

			if(!file.delete())
				throw new IOException("Can't delete directory " + file.getAbsolutePath() + " to get equivalent file");
		}

		if(!file.createNewFile() && (!file.isFile()))
			throw new IOException("Can't create file " + file.getAbsolutePath());
	}

	/**
	 * Recurses on a directory, calling a consumer everytime a file is found in the directory
	 * structure. If a file is provided instead, simply calls the consumer once
	 *
	 * @param file file or directory to recurse
	 * @param consumer consumer to call for files found
	 */
	public static void recurse(File file, Consumer<File> consumer) {
		if(file.isFile()) {
			consumer.accept(file);
			return;
		}
		File[] children = file.listFiles();

		if(children == null)
			return;

		for(File child : children)
			recurse(child, consumer);
	}

	/**
	 * Recurses on a directory, collecting all files (non directories) encountered in the directory
	 * structure.
	 *
	 * @param file file or directory to recurse
	 * @return array that contains all files in subdirectories
	 */
	public static Array<File> recurse(File file) {
		Array<File> array = new Array<>();
		recurse(file, array::add);
		return array;
	}

	/**
	 * Deletes specified file
	 *
	 * @param file file to delete
	 * @throws IOException if an I/O error occurs
	 */
	public static void deleteFile(File file) throws IOException {
		if(!file.delete())
			throw new IOException("Couldn't delete file " + file.getAbsolutePath());
	}

	/**
	 * Deletes the specified file and any children is has. An error may leave
	 * the directory structure partially deleted.
	 *
	 * @param file file to delete
	 * @throws IOException if an I/O error occurs
	 */
	public static void deleteRecursively(File file) throws IOException {
		if(file.isDirectory())
			for(File child : listFiles(file))
				deleteRecursively(child);

		deleteFile(file);
	}

	/**
	 * Finds the most recent modification of a file or any of its children to any depth
	 * @param file file or directory to check for last modification recursively
	 * @return last modification of the file or any children
	 */
	public static long getLastModifiedRecursively(File file) {
		if(file.isFile())
			return file.lastModified();
		File[] children = file.listFiles();
		if(children == null)
			throw new IllegalArgumentException("File does not exist " + file.toString());

		long lastModification = file.lastModified();
		for(File child : children)
			lastModification = Math.max(lastModification, getLastModifiedRecursively(child));
		return lastModification;
	}

	/**
	 * Gets the file of the "AppData" directory, the folder in which application data should be saved
	 *
	 * @return file representing AppData directory
	 */
	public static File getAppData() {
		return new File(getAppDataPath());
	}

	/**
	 * Gets the file of the "Home" directory
	 *
	 * @return file representing Home directory
	 */
	public static File getHomeFolder() {
		return new File(getHomeFolderPath());
	}

	/**
	 * Gets the path of the "AppData" directory, the folder in which application data should be saved
	 *
	 * @return path ot AppData directory
	 */
	public static String getAppDataPath() {
		if(SystemUtil.isWindows())
			return System.getenv("APPDATA");

		if(SystemUtil.isMac())
			return getHomeFolderPath() + "/Library/Application Support";

		if(SystemUtil.isLinux()) {
			String dataPath = System.getenv("XDG_DATA_HOME");
			if(dataPath == null || dataPath.isEmpty())
				return getHomeFolderPath() + "/.local/share";
			return dataPath;
		}

		return System.getProperty("user.dir");
	}

	/**
	 * Gets the path of the "Home" directory
	 *
	 * @return path to Home directory
	 */
	public static String getHomeFolderPath() {
		return System.getProperty("user.home");
	}

	/**
	 * Resolves path that refers to the home directory with a ~ at the beginning
	 * of the path by replacing the character by the actual home directory. If
	 * the path do not refer to a home directory, the argument is returned
	 * without modification.
	 *
	 * @param path path to resolve
	 * @return resolved path
	 */
	public static String resolveHomeInPath(String path) {
		return path.replaceFirst("^~", Matcher.quoteReplacement(System.getProperty("user.home")));
	}

	/**
	 * Gets the attributes of the manifest of the jar of this class
	 *
	 * @return attributes of manifest
	 * @throws IOException if an I/O error occurs
	 */
	public static Attributes getManifestAttributes() throws IOException {
		return new Manifest(getManifestPath().openStream()).getMainAttributes();
	}

	/**
	 * Gets the path of the manifest of the jar of this class
	 *
	 * @return path of the manifest
	 * @throws IOException if an I/O error occur
	 */
	public static URL getManifestPath() throws IOException {
		Class<FileUtil> clazz = FileUtil.class;
		URL url = clazz.getResource(clazz.getSimpleName() + ".class");

		if(url == null)
			throw new IOException("Could not find internal path of this class in the jar (null)");

		String classPath = url.toString();

		if(!classPath.startsWith("jar"))
			throw new IOException("class does not come from jar");

		return new URL(classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF");
	}

	/**
	 * Gets the input stream for a specified resource inside the jar of this class
	 *
	 * @param resourceName name of the resource
	 * @return inputstream reading the resource
	 * @throws FileNotFoundException if the resource couldn't be found
	 */
	public static InputStream resourceAsStream(String resourceName) throws FileNotFoundException {
		if(!resourceName.startsWith("/"))
			resourceName = "/" + resourceName;

		InputStream stream = FileUtil.class.getResourceAsStream(resourceName);

		if(stream == null)
			throw new FileNotFoundException("Resource not found: " + resourceName);

		return stream;
	}

	/**
	 * Lists all files in specified directory
	 *
	 * @param directory directory to list files in
	 * @return array of children of this directory
	 * @throws IOException if an I/O error occurs
	 */
	public static File[] listFiles(File directory) throws IOException {
		if(!directory.isDirectory())
			throw new IOException("Specified file is not a directory");

		File[] files = directory.listFiles();

		if(files == null)
			throw new IOException("listFiles call returned null");

		return files;
	}

	/**
	 * Returns a file instance named after the current date into specified directory
	 *
	 * @param directory directory containing the log file
	 * @return log file of current date
	 */
	public static File getLogFile(File directory) {
		return new File(directory, getDailyLogfileName());
	}

	/**
	 * Returns a filename composed of the current date and the ".log" extension.
	 *
	 * @return a filename
	 */
	public static String getDailyLogfileName() {
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date) + ".log";
	}

	/**
	 * Returns the specified filename without its extension
	 *
	 * @param filename filename to remove extension from
	 * @return filename without extension
	 */
	public static String withoutExtension(String filename) {
		Validation.ensureNotNull(filename, "filename");

		return filename.replaceFirst("[.][^.]+$", "");
	}

	/**
	 * Converts the specified {@link File} to a libGDX's {@link FileHandle}
	 *
	 * @param file file to convert
	 * @return converted file
	 */
	public static FileHandle toGdx(File file) {
		return new FileHandle(file);
	}

	/**
	 * Lists the resources in all java classpath (java.class.path) currently in execution.
	 * This includes .class files
	 *
	 * @return list of resources present in class path
	 */
	public static List<String> listResources() throws IOException {
		return listResources(fileName -> true);
	}

	/**
	 * Lists the resources in all java classpath (java.class.path) currently in execution.
	 * This includes .class files
	 *
	 * @return list of resources present in class path
	 */
	public static List<String> listResources(Predicate<String> condition) throws IOException {
		ArrayList<String> out = new ArrayList<>();
		String classPath = System.getProperty("java.class.path", ".");
		String[] classPathElements = classPath.split(File.pathSeparator);

		for(String element : classPathElements) {
			File file = new File(element);
			if(file.isDirectory())
				listResourcesFromDir(file, file, condition, out);
			else
				listResourcesFromJar(file, condition, out);
		}

		return out;
	}

	private static void listResourcesFromJar(File jarFile, Predicate<String> condition, List<String> out)
			throws IOException {
		try(ZipFile zf = new ZipFile(jarFile)) {
			Enumeration<? extends ZipEntry> e = zf.entries();
			while(e.hasMoreElements()) {
				ZipEntry ze = e.nextElement();
				String fileName = ze.getName();
				if(condition.test(fileName))
					out.add(fileName);
			}
		}
	}

	private static void listResourcesFromDir(File root, File directory, Predicate<String> condition, List<String> out)
			throws IOException {
		File[] fileList = directory.listFiles();
		if(fileList == null)
			throw new IOException("listFiles returned null for file " + directory + " in listResourcesFromDir");
		for(File file : fileList) {
			if(file.isDirectory()) {
				listResourcesFromDir(root, file, condition, out);
				continue;
			}
			String fileName = root.toPath().relativize(file.toPath()).toString();
			if(condition.test(fileName))
				out.add(fileName);
		}
	}
}