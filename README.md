# Start Once Module

This module runs a flow once after startup of your Mule application. In case of errors in the flow,
it will try it again after a configurable amount of time. The number of tries in total is limited
to a configurable value.

The payload in the started flow is the number of the try (starting with 1).

Here a complete example, place it as message source at the beginning of the flow:

```
	<starter:run-once initialDelayTime="1500"  initialDelayTimeUnit="MILLISECONDS"
		delayBetweenTries="500" delayBetweenTriesUnit="MILLISECONDS"
		numberOfTries="3"
		doc:name="Run once"/>
```

This starter will wait 1500ms after you application has been started. It will then try at most three times to execute the flow,
with 500ms delay between the tries. The delay starts after the flow has finished (with error). So you can be sure
the executions are sequential.

## Maven dependency

Add this dependency to your application pom.xml

```
<dependency>
	<groupId>de.codecentric.mule.modules</groupId>
	<artifactId>start-module</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<classifier>mule-plugin</classifier>
</dependency>
```

## Release notes

* 1.0.0 2023-11-11: Initial release. 

## Hint for developers

Must be compiled with a JDK 1.8, otherwise tests will not run (missing package in newer JDKs). This is a restriction of the Mule SDK. 

You can use the JDK bundled with AnypointStudio, e.g. in: C:/AnypointStudio-7.15.0/plugins/org.mule.tooling.jdk.v8.win32.x86_64_1.1.1

