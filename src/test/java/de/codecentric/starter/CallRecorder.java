package de.codecentric.starter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CallRecorder {
	private List<Long> entries;
	private int failures;
	
	public CallRecorder() {
		entries = new ArrayList<>();
	}

	public String getRecordCall() throws Exception {
		entries.add(System.currentTimeMillis());
		if (getFailures() > 0) {
			failures--;
			throw new Exception("bäm!");
		}
		return "recorded call";
	}
	
	public int getFailures() {
		return failures;
	}

	public void setFailures(int failures) {
		this.failures = failures;
	}
	
	public List<Long> getEntries() {
		return Collections.unmodifiableList(entries);
	}
}
