# Android testing
A collection of samples to showcase various testing types that can be performed on the Android code base.

Following are the different types of testing involved in android.

* Unit testing
* UI testing
* Integration testing
* Performance testing

----
## Place of execution


| Test 				           | Execution             |
|---------------------|-----------------------|
| Unit testing        | JVM                   |
| UI testing          | JVM or Android device |
| Integration testing | Android device        |

## Unit testing
Unit testing usually refers testing a particular units of code totally in isolation with other component to ensure their correctness and functionality.
To bring the isolation we need to seek help on framework like `Mockito` to create stubs, mock, and test doubles.

#### Famous Unit testing frameworks

| Framework 			 | Description                                      |    
|---------------|--------------------------------------------------|
| Junit         | Testing framework for Java                       |
| Mockito       | Mocking framework for unit tests written in Java |
| Truth         | To perform assertions in tests                   |


## UI testing
UI testing usually refers testing the user interface by simulating user action and verify the behavior of UI elements.

#### Famous UI testing frameworks

| Framework 			         | Description                                                                                                                   |        
|-----------------------|-------------------------------------------------------------------------------------------------------------------------------|
| Espresso		   	        | Android UI test framework to perform UI interaction and state assertion.  (White box testing)                                 |
| UI Automator          | To perform cross-app functional UI testing across system and installed apps. (Both Black box & white box testing)             |
| Compose UI test Junit | To provide Junit rules invoke composable function in Junit. also provides APIs to perform UI interaction and state assertion. |
| Appium				            | *Yet to add *	                                                                                                                |


## Integration testing
Integration testing usually refers testing interaction between different components or modules of an application.

#### Integration Testing Frameworks
| Framework 			        | Description                                                                                                                                                                                                  |
|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Robolectric			       | 	 To perform android UI/functional testing on JVM without the need for android device                                                                                                                        |
| AndroidX test runner | Provides AndroidJUnitRunner which is a JUnit test runner that allows to run instrumented JUnit 4 tests on Android devices, including those using the Espresso, UI Automator, and Compose testing frameworks. |


#### Reference

* https://developer.android.com/training/testing/instrumented-tests/ui-tests
* https://developer.android.com/jetpack/compose/testing
* https://developer.android.com/studio/test/test-in-android-studio
* https://developer.android.com/training/dependency-injection/hilt-testing
* https://developer.android.com/training/testing/other-components/ui-automator
* https://martinfowler.com/articles/practical-test-pyramid.html#ProviderTestourTeam
* https://martinfowler.com/bliki/TestDouble.html
* 


#### Todo
* Screenshot test
* Small brief on junit rules, test apk
* Mock vs Stub
* Difference between AndroidJunitRunner and RobolectricTestRunner
* Example to Replace a binding in a single test
* Different types smoke testing 
  * Smoke
  * Regression (a return to a previous and less advanced or worse state, condition, or way of behaving)

Points
- Unit test ---  one element of the software at a time 
-  Test Double as the generic term for any kind of pretend object used in place of a real object for testing purposes.
Keywords
- collaborators
- SUT
- DD-style way of writing tests 
- Talking about different test classifications is always difficult.
- 

Commands
./gradlew connectedAndroidTest --continue
./gradlew testDebugUnitTest   


