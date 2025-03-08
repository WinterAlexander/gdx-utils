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
			throw throwable;


		assertTrue(testTmp.delete());
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
