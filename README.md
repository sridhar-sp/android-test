# Android testing
A collection of samples to showcase various testing types that can be performed on the Android code base.

Following are the different types of testing involved in android.

* Unit testing
* UI testing
* Integration testing
* Performance testing

<hr/>

## Place of execution


| Test 				           | Execution             |
|---------------------|-----------------------|
| Unit testing        | JVM                   |
| UI testing          | JVM or Android device |
| Integration testing | Android device        |

<hr/>

## Unit testing
Unit testing usually refers testing a particular units of code totally in isolation with other component to ensure their correctness and functionality.
To bring the isolation we need to seek help on framework like `Mockito` to create stubs, mock, and test doubles.

#### Famous Unit testing frameworks

| Framework 			 | Description                                      |    
|---------------|--------------------------------------------------|
| Junit         | Testing framework for Java                       |
| Mockito       | Mocking framework for unit tests written in Java |
| Truth         | To perform assertions in tests                   |


#### Example

<details>
<summary>Simple test without mocks</summary>

#### System under test
```kotlin
data class Email(val value: String?) : Parcelable {
    fun isValid(): Boolean {
        return if (value == null) false else PatternsCompat.EMAIL_ADDRESS.matcher(value).matches()
    }
}
```
#### Test
```kotlin
class EmailTest {

    @Test
    fun shouldReturnIsValidAsFalseWhenEmailIsNull() {
        Truth.assertThat(Email(null).isValid()).isFalse()
    }

    @Test
    fun shouldReturnIsValidAsFalseWhenEmailIsInvalid() {
        Truth.assertThat(Email("aa@.com").isValid()).isFalse()
        Truth.assertThat(Email("aacd@aa.com@").isValid()).isFalse()
        Truth.assertThat(Email("").isValid()).isFalse()
        Truth.assertThat(Email("@gmail.com").isValid()).isFalse()
    }

    @Test
    fun shouldReturnIsValidAsTrueWhenEmailIsValid() {
        Truth.assertThat(Email("abcd@domain.com").isValid()).isTrue()
        Truth.assertThat(Email("a@domain.in").isValid()).isTrue()
    }
}
```
</details>

<details>
<summary>Simple test with mocks</summary>

#### System under test
```kotlin
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val emailAddress = savedStateHandle.get<String>(BundleArgs.KEY_EMAIL)
}
```
#### Test
```kotlin
	@Test
    fun `should return email value from saved state handle when email address is read from viewModel`() {
        val savedStateHandleMock = mockk<SavedStateHandle>()
        every<String?> { savedStateHandleMock[BundleArgs.KEY_EMAIL] } returns "abcd@gmail.com"
        val profileViewModel = ProfileViewModel(savedStateHandleMock)
        assertThat(profileViewModel.emailAddress).isEqualTo("abcd@gmail.com")
    }
```

</details>

<hr/>

## UI testing
UI testing usually refers testing the user interface by simulating user action and verify the behavior of UI elements.

#### Famous UI testing frameworks

| Framework 			         | Description                                                                                                                   |        
|-----------------------|-------------------------------------------------------------------------------------------------------------------------------|
| Espresso		   	        | Android UI test framework to perform UI interaction and state assertion.  (White box testing)                                 |
| UI Automator          | To perform cross-app functional UI testing across system and installed apps. (Both Black box & white box testing)             |
| Compose UI test Junit | To provide Junit rules invoke composable function in Junit. also provides APIs to perform UI interaction and state assertion. |
| Appium				            | *Yet to add *	                                                                                                                |

<hr/>

## Integration testing
Integration testing usually refers testing interaction between different components or modules of an application.

#### Integration Testing Frameworks
| Framework 			        | Description                                                                                                                                                                                                  |
|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Robolectric			       | 	 To perform android UI/functional testing on JVM without the need for android device                                                                                                                        |
| AndroidX test runner | Provides AndroidJUnitRunner which is a JUnit test runner that allows to run instrumented JUnit 4 tests on Android devices, including those using the Espresso, UI Automator, and Compose testing frameworks. |

<hr/>

#### Reference

* https://developer.android.com/training/testing/instrumented-tests/ui-tests
* https://developer.android.com/jetpack/compose/testing
* https://developer.android.com/studio/test/test-in-android-studio
* https://developer.android.com/training/dependency-injection/hilt-testing
* https://developer.android.com/training/testing/other-components/ui-automator
* https://martinfowler.com/articles/practical-test-pyramid.html#ProviderTestourTeam
* https://martinfowler.com/bliki/TestDouble.html
* 

<hr/>
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

<hr/>
Commands
./gradlew connectedAndroidTest --continue
./gradlew testDebugUnitTest   


