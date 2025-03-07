package com.winteralexander.gdx.utils.system;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Represents the specs of a system
 * <p>
 * Created on 2025-03-06.
 *
 * @author Alexander Winter
 */
public class SystemSpecs {
	private final String[] cpus;
	private final String[] gpus;
	private final long totalMemory, usedMemory;

	public SystemSpecs(String[] cpus, String[] gpus, long totalMemory, long usedMemory) {
		ensureNotNull(cpus, "cpus");
		ensureNotNull(gpus, "gpus");
		this.cpus = cpus;
		this.gpus = gpus;
		this.totalMemory = totalMemory;
		this.usedMemory = usedMemory;
	}

	public String[] getCPUs() {
		return cpus;
	}

	public String[] getGPUs() {
		return gpus;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public long getUsedMemory() {
		return usedMemory;
	}
}
