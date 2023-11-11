# Start Once Module

Sometimes you want to execute a flow when your Mule application starts. This can be achieved by this module.

Here a complete example, place it as message source at the beginning of the flow:

```
	<starter:run-once initialDelayTime="1500"  initialDelayTimeUnit="MILLISECONDS"
		delayBetweenTries="500" delayBetweenTriesUnit="MILLISECONDS"
		numberOfTries="3"
		doc:name="Run once"/>
```

This starter will wait 1500ms after you application has been started. It will then try at most three times to execute the flow,
with 500ms delay between the tries. It will stop immediately, when the flow executes successful.  

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

* 1.0.0 2023-11-12: Initial release. 

## Hint for developers

Must be compiled with a JDK 1.8, otherwise tests will not run (missing package in newer JDKs). This is a restriction of the Mule SDK. 

You can use the JDK bundled with AnypointStudio, e.g. in: C:/AnypointStudio-7.15.0/plugins/org.mule.tooling.jdk.v8.win32.x86_64_1.1.1

