package de.codecentric.starter.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;

import de.codecentric.starter.CallRecorder;

public class StarterTest extends MuleArtifactFunctionalTestCase {

    @Override
    protected String getConfigFile() {
    	
        return "starter-tests.xml";
    }
    
    @Test
    public void testThreeSchedulers() throws Exception {
    	long start = System.currentTimeMillis();
    	Thread.sleep(10_000);
    	firstIsSuccess(start);
    	firstIsFailure(start);
    	manyFailures(start);
    }

	private void firstIsSuccess(long start) {
    	Optional<CallRecorder> opt = registry.lookupByName("firstIsSuccess");
    	CallRecorder recorder = opt.get();
    	long[] deltas = computeDeltas(recorder.getEntries(), start);
    	assertEquals(1, deltas.length);
    	assertThat(deltas[0], greaterThan(500L));
	}

	private void firstIsFailure(long start) {
    	Optional<CallRecorder> opt = registry.lookupByName("firstIsFailure");
    	CallRecorder recorder = opt.get();
    	long[] deltas = computeDeltas(recorder.getEntries(), start);
    	assertEquals(2, deltas.length);
    	assertThat(deltas[0], greaterThan(500L));
    	assertThat(deltas[1], greaterThan(1500L));
	}

	private void manyFailures(long start) {
    	Optional<CallRecorder> opt = registry.lookupByName("manyFailures");
    	CallRecorder recorder = opt.get();
    	long[] deltas = computeDeltas(recorder.getEntries(), start);
    	assertEquals(3, deltas.length);
    	assertThat(deltas[0], greaterThan(500L));
    	assertThat(deltas[1], greaterThan(1500L));
    	assertThat(deltas[2], greaterThan(2500L));
	}
	
	private long[] computeDeltas(List<Long> entries, long start) {
		long[] result = new long[entries.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = entries.get(i) - start;
		}
		return result;
	}
}
