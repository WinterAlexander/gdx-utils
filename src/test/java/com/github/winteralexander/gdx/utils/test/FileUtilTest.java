package com.github.winteralexander.gdx.utils.test;

import com.badlogic.gdx.utils.Array;
import org.junit.Test;

import java.io.File;

import static com.github.winteralexander.gdx.utils.io.FileUtil.ensureFile;
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
}
