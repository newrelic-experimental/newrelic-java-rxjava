[![New Relic Experimental header](https://github.com/newrelic/opensource-website/raw/master/src/images/categories/Experimental.png)](https://opensource.newrelic.com/oss-category/#new-relic-experimental)
   
![GitHub forks](https://img.shields.io/github/forks/newrelic-experimental/newrelic-java-rxjava?style=social)
![GitHub stars](https://img.shields.io/github/stars/newrelic-experimental/newrelic-java-rxjava?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/newrelic-experimental/newrelic-java-rxjava?style=social)

![GitHub all releases](https://img.shields.io/github/downloads/newrelic-experimental/newrelic-java-rxjava/total)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/newrelic-experimental/newrelic-java-rxjava)
![GitHub last commit](https://img.shields.io/github/last-commit/newrelic-experimental/newrelic-java-rxjava)
![GitHub Release Date](https://img.shields.io/github/release-date/newrelic-experimental/newrelic-java-rxjava)


![GitHub issues](https://img.shields.io/github/issues/newrelic-experimental/newrelic-java-rxjava)
![GitHub issues closed](https://img.shields.io/github/issues-closed/newrelic-experimental/newrelic-java-rxjava)
![GitHub pull requests](https://img.shields.io/github/issues-pr/newrelic-experimental/newrelic-java-rxjava)
![GitHub pull requests closed](https://img.shields.io/github/issues-pr-closed/newrelic-experimental/newrelic-java-rxjava) 
    
# New Relic Java Instrumentation for RxJava 

Instrumentation for the RxJava Framework (https://github.com/ReactiveX/RxJava).  There are three sets of instrumentation, one for RxJava1 one for RxJava2 and one for RxJava3.

## Installation

To install:   
1. Download the latest release jar files.    
2. In the New Relic Java directory (the one containing newrelic.jar), create a directory named extensions if it does not already exist.   
3. Copy the downloaded jars into the extensions directory.   
4. Restart the application.   

## Getting Started

Once installed the instrumentation will start to track RxJava objects.  

### RxJava1
Tracks Observable, Completable and Single.  For an Observable, it will link the onNext back to the original transaction and complete and link on either the onError or onCompleted and expire the token.   For a Completable or Single, it will link back to the original transaction on the onError or onCompleted and expired the token. 
The rxjava1-finder extension will find methods which return an RxJava1 object (Completable, Observable, Single) and include them in the transaction trace.  Methods in the rx package (and subpackages) are ignored.
### RxJava2
Tracks Completable, Flowable, Maybe, Observable and Single.  For objects with onNext, the method call will be linked back to the original transaction.   For all objects the link is expired and linked with the object completes or an error is recorded.
The rxjava2-finder extension will find methods which return an RxJava1 object (Completable, Flowable, Maybe, Observable, Single) and include them in the transaction trace.  Methods in the io.reactivex package (and subpackages) are ignored.   
#### RxJava2 Segments
The RxJava2 instrumentation uses a New Relic Java Agent segment (https://docs.newrelic.com/docs/agents/java-agent/async-instrumentation/java-agent-api-asynchronous-applications/#segments) to track the time from when the object is subscribed to until the object is completed or throws an error. It is reported as RxJava2/*rxType*/TotalTime/*rx Class simple name*   This feature is turned on by default.  Additional you can choose not to track certain RxJava Object by configuring a comma separated list of the simple class names that you wish to ignore.  Due to the logic used with the From methods (e.g. fromCallable) it is not possible to use segments to track from and hence they are not tracked via segments.      
    
To turn this feature off:
1. Edit newrelic.yml
2. Insert the following text into the file.  It can be placed anywhere in the file but the preferred place is right before the **labels:** stanza.  Please note that spaces matter so first two lines has two space at the beginning and the third has four spaces.    
     
&nbsp;&nbsp;\# Used to toggle the use of segments in RxJava2 objects  
&nbsp;&nbsp;RxJava2:   
&nbsp;&nbsp;&nbsp;&nbsp;useSegments: false  

To ignore certain Rx objects add the rxType and simple class name to the above configuration.   For example to stop using segments to track with the name RxJava2/Flowable/TotalTime/FlowableFlatMap, use the following configuration:   
&nbsp;&nbsp;RxJava2:   
&nbsp;&nbsp;&nbsp;&nbsp;useSegments: true  
&nbsp;&nbsp;&nbsp;&nbsp;Flowable:     
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ignores: FlowableFlatMap   
   
### RxJava3
Support for RxJava3 is the same as the RxJava2.  Requires different extension jars due to changes to package names    

### Finder Extensions   
There are three finder extensions included with this instrumentation, one for each RxJava version.   The purpose of these extensions to to track methods outside of those in RxJava that return one of the RxJava objects.  This allows you to track your methods that return an RxJava object.   
   
## Building
Building the extension requires that Gradle is installed.
To build the extension jars from source, follow these steps:
### Build single extension
To build a single extension with name *extension*, do the following:
1. Set an environment variable *NEW_RELIC_EXTENSIONS_DIR* and set its value to the directory where you want the jar file built.
2. Run the command: gradlew *extension*:clean *extension*:install
### Build all extensions
To build all extensions, do the following:
1. Set an environment variable *NEW_RELIC_EXTENSIONS_DIR* and set its value to the directory where you want the jar file built.
2. Run the command: gradlew clean install
## Support

New Relic has open-sourced this project. This project is provided AS-IS WITHOUT WARRANTY OR DEDICATED SUPPORT. Issues and contributions should be reported to the project here on GitHub.

We encourage you to bring your experiences and questions to the [Explorers Hub](https://discuss.newrelic.com) where our community members collaborate on solutions and new ideas.


## Contributing

We encourage your contributions to improve [Project Name]! Keep in mind when you submit your pull request, you'll need to sign the CLA via the click-through using CLA-Assistant. You only have to sign the CLA one time per project. If you have any questions, or to execute our corporate CLA, required if your contribution is on behalf of a company, please drop us an email at opensource@newrelic.com.

**A note about vulnerabilities**

As noted in our [security policy](../../security/policy), New Relic is committed to the privacy and security of our customers and their data. We believe that providing coordinated disclosure by security researchers and engaging with the security community are important means to achieve our security goals.

If you believe you have found a security vulnerability in this project or any of New Relic's products or websites, we welcome and greatly appreciate you reporting it to New Relic through [HackerOne](https://hackerone.com/newrelic).

## License

[Project Name] is licensed under the [Apache 2.0](http://apache.org/licenses/LICENSE-2.0.txt) License.

>[If applicable: [Project Name] also uses source code from third-party libraries. You can find full details on which libraries are used and the terms under which they are licensed in the third-party notices document.]
