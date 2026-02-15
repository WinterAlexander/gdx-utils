package com.winteralexander.gdx.utils.test.io;

import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.io.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.winteralexander.gdx.utils.io.FileUtil.ensureFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for FileUtil
 * <p>
 * Created on 2018-08-30.
 *
 * @author Alexander Winter
 */
public class FileUtilTest {
	@Test
	public void testSimultaneousFileCreation() throws Throwable {
		File testTmp = new File("test-tmp");
		if(testTmp.exists())
			assertTrue(testTmp.delete());

		Array<Throwable> errors = new Array<>();

		Runnable create = () -> {
			try {
				Thread.sleep(10);
				ensureFile(testTmp);
			} catch(Throwable t) {
				errors.add(t);
			}
		};

		Thread t1 = new Thread(create);
		Thread t2 = new Thread(create);

		t1.start();
		t2.start();

		t1.join();
		t2.join();

		for(Throwable throwable : errors)
			throwable.printStackTrace(System.err);

		if(errors.size > 0)
			throw errors.get(0);

		assertTrue(testTmp.delete());
	}

	@Test
	public void testRecurse() throws Throwable {
		File testTmp = new File("test-tmp-recurse");
		if(testTmp.exists())
			FileUtil.deleteRecursively(testTmp);

		FileUtil.ensureDirectory(new File(testTmp, "test/1/2/3"));
		FileUtil.ensureDirectory(new File(testTmp, "test/1/4"));

		new File(testTmp, "test/1/2/3/1.txt").createNewFile();
		new File(testTmp, "test/1/4/2.txt").createNewFile();
		new File(testTmp, "test/1/4/3.txt").createNewFile();
		new File(testTmp, "test/4.txt").createNewFile();
		new File(testTmp, "5.txt").createNewFile();

		Array<File> files = FileUtil.recurse(testTmp);
		assertEquals(5, files.size);
		assertTrue(files.contains(new File(testTmp, "test/1/2/3/1.txt"), false));
		assertTrue(files.contains(new File(testTmp, "test/1/4/2.txt"), false));
		assertTrue(files.contains(new File(testTmp, "test/1/4/3.txt"), false));
		assertTrue(files.contains(new File(testTmp, "test/4.txt"), false));
		assertTrue(files.contains(new File(testTmp, "5.txt"), false));

		FileUtil.deleteRecursively(testTmp);
	}

	@Test
	public void testGetResources() throws IOException {
		List<String> resources = FileUtil.listResources(fileName ->
				fileName.startsWith("test_resources" + File.separatorChar));
		assertEquals(3, resources.size());
		assertTrue(resources.contains("test_resources" + File.separatorChar + "test_resource.txt"));
		assertTrue(resources.contains("test_resources" + File.separatorChar +
				"inner" + File.separatorChar + "test_inner.txt"));
		assertTrue(resources.contains("test_resources" + File.separatorChar +
				"inner" + File.separatorChar + "test_inner2.txt"));
	}
}
