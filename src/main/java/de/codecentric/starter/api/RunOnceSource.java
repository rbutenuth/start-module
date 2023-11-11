package de.codecentric.starter.api;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.message.Error;
import org.mule.runtime.api.scheduler.Scheduler;
import org.mule.runtime.api.scheduler.SchedulerConfig;
import org.mule.runtime.api.scheduler.SchedulerService;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alias("run-once")
public class RunOnceSource extends Source<Number, Void> {
	private static final Logger logger = LoggerFactory.getLogger(RunOnceSource.class);
	// for scheduler documentation see:
	// https://docs.mulesoft.com/mule-sdk/latest/threading-asynchronous-processing

	@Inject
	private SchedulerService schedulerService;

	@Inject
	private SchedulerConfig schedulerConfig;

	private ComponentLocation location;
	private Scheduler scheduler;

	@Parameter
	@Optional(defaultValue = "1")
	private long initialDelayTime;
	
	@Parameter
	@Optional(defaultValue = "SECONDS")
	private TimeUnit initialDelayTimeUnit;
	
	@Parameter
	@Optional(defaultValue = "1")
	private long delayBetweenTries;
	
	@Parameter
	@Optional(defaultValue = "SECONDS")
	private TimeUnit delayBetweenTriesUnit;
	
	@Parameter
	@Optional(defaultValue = "1")
	private int numberOfTries;
	
	private int currentTry;
	
	private SourceCallback<Number, Void> sourceCallback;
	
	@Override
	public void onStart(SourceCallback<Number, Void> callback) {
		this.sourceCallback = callback;
		scheduler = schedulerService.customScheduler(schedulerConfig.withMaxConcurrentTasks(1)
				.withName("start-once-scheduler " + location.getRootContainerName()).withWaitAllowed(true)
				.withShutdownTimeout(5, TimeUnit.SECONDS));
		scheduler.schedule(new StarterTask(), initialDelayTime, initialDelayTimeUnit);
	}

	@Override
	public void onStop() {
		if (scheduler != null) {
			scheduler.stop();
		}
	}
	
	@OnSuccess
    public void onSuccess() {
		logger.error("run once succeded");
		scheduler.stop();
	}
	
	@OnError
	public void onError(Error error) {
		logger.error("run once, try {} of {} failed: {}", currentTry, numberOfTries, error.getDescription());
		if (currentTry < numberOfTries) {
			scheduler.schedule(new StarterTask(), delayBetweenTries, delayBetweenTriesUnit);
		} else {
			scheduler.stop();
		}
	}
	
	private class StarterTask implements Callable<Void> {
		@Override
		public Void call() {
			Result<Number, Void> result = Result.<Number, Void>builder().output(Integer.valueOf(++currentTry)).build();
			sourceCallback.handle(result);
			return null;
		}
	}
}
