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

Unit testing usually refers to testing a particular unit of code in complete isolation from other components to ensure
its correctness and functionality. Developers often use frameworks like `Mockito` to create stubs (test doubles), mocks,
etc., to achieve this isolation.

* **Stub**: A stub is a direct replacement for a function, interface, or abstract class (or any other dependency). It
  allows
  us to swap the original implementation with a test-specific version, often referred to as a test dummy (or test
  double).


* **Mock**: A mock serves as a more advanced test double for a dependency. Mocking frameworks let us actively simulate
  different behaviours by configuring the mock to return specific responses based on inputs or conditions. Furthermore,
  mocks allow us to confirm interactions by verifying the existence of a method, its number of calls, and the arguments
  passed during each call.


* **Why do we need this?** During testing, especially unit testing, we aim to isolate the component under test from its
  dependencies. This ensures that we're testing the component alone, making the tests simpler, faster, and less
  error-prone. Mocking or stubbing helps us avoid injecting side effects or relying on external dependencies.


* **Example**: Imagine a ViewModel class that depends on a repository. The Repository class, in turn, makes network API
  calls.
  If we want to write a unit test for the ViewModel alone, we don’t want to incur the overhead of making actual API
  calls,
  as this can make the test error-prone due to network conditions or server response times. To avoid these side effects,
  we can replace the repository with a stub (test double) or a mock during the test. This ensures that we focus only on
  the behaviour of the ViewModel while bypassing external dependencies.

### Famous Unit testing frameworks

| Framework 			 | Description                                             |    
|---------------|---------------------------------------------------------|
| Junit         | Testing framework for Java                              |
| Mockito       | Mocking framework for unit tests written in Java/Kotlin |
| Truth         | To perform assertions in tests                          |

### Example

<details>
<summary>Simple test without mocks</summary>

### System under test

```kotlin
data class Email(val value: String?) : Parcelable {
    fun isValid(): Boolean {
        return if (value == null) false else PatternsCompat.EMAIL_ADDRESS.matcher(value).matches()
    }
}
```

### Test

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

### System under test

```kotlin
@HiltViewModel
class ProfileViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val logoutUseCase: LogoutUseCase
) : ViewModel() {
  val emailAddress = savedStateHandle.get<String>(BundleArgs.KEY_EMAIL)
}
```

### Test

```kotlin
@Test
fun `should return email value from saved state handle when email address is read from viewModel`() {
  val savedStateHandleMock = mockk<SavedStateHandle>()
  every<String?> { savedStateHandleMock[BundleArgs.KEY_EMAIL] } returns "abcd@gmail.com"

  val logoutUseCase = mockk<LogoutUseCase>()

  val profileViewModel = ProfileViewModel(savedStateHandleMock, logoutUseCase)
  assertThat(profileViewModel.emailAddress).isEqualTo("abcd@gmail.com")
}
```

</details>


<details>
<summary>Test with mocks and stubs</summary>

### System under test

```kotlin
@HiltViewModel
class ProfileViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val logoutUseCase: LogoutUseCase
) : ViewModel() {
  val emailAddress = savedStateHandle.get<String>(BundleArgs.KEY_EMAIL)

  var shouldLogout by mutableStateOf(false)
    private set

  fun logout() {
    viewModelScope.launch {
      logoutUseCase.logout(Email(emailAddress))
      shouldLogout = true
    }
  }
}
```

### Test

```kotlin
@Test
fun `should call logout callback when logout button is pressed`() = runTest(testDispatcher) {
    Dispatchers.setMain(testDispatcher)

    val savedStateHandleMock = mockk<SavedStateHandle>()
    every<String?> { savedStateHandleMock[BundleArgs.KEY_EMAIL] } returns "abcd@gmail.com"

    var isLogoutSuccess = false
    val logoutStub = object : LogoutUseCase {
      override suspend fun logout(email: Email) {
        isLogoutSuccess = true
      }
    }

    val profileViewModel = ProfileViewModel(savedStateHandleMock, logoutStub)
    assertThat(profileViewModel.emailAddress).isEqualTo("abcd@gmail.com")
    assertThat(profileViewModel.shouldLogout).isFalse()

    profileViewModel.logout()
    runCurrent()  // run current co routine to completion

    assertThat(profileViewModel.shouldLogout).isTrue()
    assertThat(isLogoutSuccess).isTrue()
  }
```

</details>

### Dependencies

```
// Regular JUnit dependency
testImplementation("junit:junit:4.13.2")

// Assertion library
testImplementation("com.google.truth:truth:1.1.4")

// Allows us to create and configure mock objects, stub methods, verify method invocations, and more
testImplementation("io.mockk:mockk:1.13.5")
```

### Command

```shell
./gradlew testDebugUnitTest
```

<hr/>

## UI testing

UI testing usually refers testing the user interface by simulating user action and verify the behavior of UI elements.

### Famous UI testing frameworks

| Framework 			         | Description                                                                                                                   |        
|-----------------------|-------------------------------------------------------------------------------------------------------------------------------|
| Espresso		   	        | Android UI test framework to perform UI interaction and state assertion.  (White box testing)                                 |
| UI Automator          | To perform cross-app functional UI testing across system and installed apps. (Both Black box & white box testing)             |
| Compose UI test Junit | To provide Junit rules invoke composable function in Junit. also provides APIs to perform UI interaction and state assertion. |
| Appium				            | *Yet to add *	                                                                                                                |

### Compose UI test Junit

<details>
<summary>Compose UI+Interaction Unit Test </summary>

### System under test

Test uses `RobolectricTestRunner` to run code on `JVM`.
Some code from `android.jar` requires special config to return use android resources and return default values (i.e.,
Log methods).

```
testOptions {
        unitTests {
            // Enables unit tests to use Android resources, assets, and manifests.
            isIncludeAndroidResources = true
            // Whether unmocked methods from android.jar should throw exceptions or return default values (i.e. zero or null).
            isReturnDefaultValues = true
        }
}
```

```kotlin
@Composable
fun Login(onSuccess: (email: Email) -> Unit, viewModel: LoginViewModel = hiltViewModel()) {

  LaunchedEffect(key1 = viewModel.loginState, block = {
    if (viewModel.loginState == LoginState.LoginSuccess) onSuccess(viewModel.email)
  })

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(MaterialTheme.appDimens.mediumContentPadding),
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      modifier = Modifier.padding(bottom = MaterialTheme.appDimens.largeContentPadding),
      text = stringResource(id = R.string.login),
      style = MaterialTheme.typography.headlineMedium
    )
    EmailInput(modifier = Modifier
      .semantics { testTagsAsResourceId = true;testTag = "emailInput" }
      .testTag("emailInput")
      .fillMaxWidth()
      .padding(bottom = MaterialTheme.appDimens.largeContentPadding),
      value = viewModel.email.value ?: "",
      isEnabled = viewModel.loginState !== LoginState.InProgress,
      onValueChange = viewModel::updateEmail)
    PasswordInput(modifier = Modifier
      .semantics { testTagsAsResourceId = true;testTag = "passwordInput" }
      .fillMaxWidth()
      .padding(bottom = MaterialTheme.appDimens.mediumContentPadding),
      value = viewModel.password.value ?: "",
      isEnabled = viewModel.loginState !== LoginState.InProgress,
      onValueChange = viewModel::updatePassword)
    if (viewModel.loginState === LoginState.LoginPending)
      PrimaryButton(modifier = Modifier
        .semantics { testTagsAsResourceId = true;testTag = "loginButton" }
        .fillMaxWidth(),
        text = stringResource(id = R.string.login),
        enabled = viewModel.isLoginButtonEnabled,
        onClick = viewModel::login)
    if (viewModel.loginState === LoginState.InProgress)
      CircularProgressIndicator(
        modifier = Modifier
          .semantics { testTagsAsResourceId = true;testTag = "progressLoader" }
          .align(Alignment.CenterHorizontally)
          .padding(MaterialTheme.appDimens.smallContentPadding)
      )
  }
}
```

### Test

```kotlin
@RunWith(RobolectricTestRunner::class)
class LoginKtTest {

  @get:Rule
  val composeRule = createComposeRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()

  @Test
  fun shouldEnableButtonOnlyWhenInputsAreValid() {
    with(composeRule) {
      val loginUseCase = mockk<LoginUseCaseImpl>()
      val loginViewModel = LoginViewModel(loginUseCase)
      setContent { Login(onSuccess = {}, viewModel = loginViewModel) }
      onNodeWithTag("loginButton").assertIsNotEnabled()

      onNodeWithTag("emailInput").performTextInput("abcd")
      onNodeWithTag("loginButton").assertIsNotEnabled()

      onNodeWithTag("emailInput").performTextInput("abcd@gmail.com")
      onNodeWithTag("loginButton").assertIsNotEnabled()

      onNodeWithTag("passwordInput").performTextInput("12")
      onNodeWithTag("loginButton").assertIsNotEnabled()

      onNodeWithTag("passwordInput").performTextInput("12345")
      onNodeWithTag("loginButton").assertIsEnabled()
    }
  }
}
```

</details>

### Dependencies

```
// Allows us to create and configure mock objects, stub methods, verify method invocations, and more
androidTestImplementation("io.mockk:mockk-agent:1.13.5")
androidTestImplementation("io.mockk:mockk-android:1.13.5")
androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

// Assertion library
androidTestImplementation("com.google.truth:truth:1.1.4")

// Needed for createComposeRule , createAndroidComposeRule and other rules used to perform UI test
testImplementation("androidx.compose.ui:ui-test-junit4:$compose_version") // used with robolectric to run ui test on jvm
androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version") // used with AndroidTestRunner to run ui test on virtual/physical device.

// Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")

// Dependency injection for For instrumented tests on JVM
testImplementation("com.google.dagger:hilt-android-testing:2.49")
kaptTest("com.google.dagger:hilt-compiler:2.49")

// Needed to run android UI test on JVM instead of on an emulator or device
testImplementation("org.robolectric:robolectric:4.10.3)

// Helper for other arch dependencies, including JUnit test rules that can be used with LiveData, coroutines etc
testImplementation("androidx.arch.core:core-testing:2.2.0")
```

### Command

```shell
./gradlew testDebugUnitTest
```

<hr/>


## Integration testing

Integration testing typically involves testing the interactions between different components or modules of an
application.

During these tests, we can visually observe the app launching, with all the interactions specified in the code happening
in real time.

However, there’s an alternative approach that leverages GradleManagedDevices to run integration tests. This method skips
the UI preview and executes the tests on a configured virtual or physical device. More details on this approach are
provided in the next section.

### Integration Testing Frameworks

| Framework 			        | Description                                                                                                                                                                                                                                                                |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Robolectric			       | 	 To perform android UI/functional testing on JVM without the need for android device.<br/> * Test files are located inside the test folder                                                                                                                                |
| AndroidX test runner | Provides AndroidJUnitRunner which is a JUnit test runner that allows to run instrumented JUnit 4 tests on Android devices, including those using the Espresso, UI Automator, and Compose testing frameworks. <br/> * Test files are located inside the androidTest folder. |
| UI Automator         |                                                                                                                                                                                                                                                                            |

Test uses `AndroidJUnitRunner` to run on android virtual/physical device.

### Setup & Dependencies
```
// Used to create AndroidHiltTestRunner from AndroidJUnitRunner
androidTestImplementation("androidx.test:runner:1.6.2")
```

```kotlin
android {

  defaultConfig {
    // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    // If we are using Hilt we can extend the AndroidJUnitRunner and pass the HiltTestApplication as application component.
    testInstrumentationRunner = "com.gandiva.android.sample.AndroidHiltTestRunner"
  }
}
```

```kotlin
class AndroidHiltTestRunner : AndroidJUnitRunner() {
  override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
    return super.newApplication(cl, HiltTestApplication::class.java.name, context)
  }
}
```

### Robolectric

<details>
<summary>Instrumentation test with Robolectric</summary>

You can observe from the test cases it look similar to the Compose UI Unit Test code snippet,
because `androidx.compose.ui.test.junit4` library has the test implementation for both the JVM and Android. so using the
same interfaces we can run the test on both runtime. Based on the test runner configured it will use the corresponding
implementation at runtime.

The `androidx.compose.ui.test.junit4` module includes a `ComposeTestRule` and an implementation for Android
called `AndroidComposeTestRule`. Through this rule you can set Compose content or access the activity. You construct the
rules using factory functions, either `createComposeRule` or, if you need access to an
activity, `createAndroidComposeRule`.

### System under test

```kotlin
@Composable
fun Login(onSuccess: (email: Email) -> Unit, viewModel: LoginViewModel = hiltViewModel()) {

  LaunchedEffect(key1 = viewModel.loginState, block = {
    if (viewModel.loginState == LoginState.LoginSuccess) onSuccess(viewModel.email)
  })

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(MaterialTheme.appDimens.mediumContentPadding),
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      modifier = Modifier.padding(bottom = MaterialTheme.appDimens.largeContentPadding),
      text = stringResource(id = R.string.login),
      style = MaterialTheme.typography.headlineMedium
    )
    EmailInput(modifier = Modifier
      .semantics { testTagsAsResourceId = true;testTag = "emailInput" }
      .testTag("emailInput")
      .fillMaxWidth()
      .padding(bottom = MaterialTheme.appDimens.largeContentPadding),
      value = viewModel.email.value ?: "",
      isEnabled = viewModel.loginState !== LoginState.InProgress,
      onValueChange = viewModel::updateEmail)
    PasswordInput(modifier = Modifier
      .semantics { testTagsAsResourceId = true;testTag = "passwordInput" }
      .fillMaxWidth()
      .padding(bottom = MaterialTheme.appDimens.mediumContentPadding),
      value = viewModel.password.value ?: "",
      isEnabled = viewModel.loginState !== LoginState.InProgress,
      onValueChange = viewModel::updatePassword)
    if (viewModel.loginState === LoginState.LoginPending)
      PrimaryButton(modifier = Modifier
        .semantics { testTagsAsResourceId = true;testTag = "loginButton" }
        .fillMaxWidth(),
        text = stringResource(id = R.string.login),
        enabled = viewModel.isLoginButtonEnabled,
        onClick = viewModel::login)
    if (viewModel.loginState === LoginState.InProgress)
      CircularProgressIndicator(
        modifier = Modifier
          .semantics { testTagsAsResourceId = true;testTag = "progressLoader" }
          .align(Alignment.CenterHorizontally)
          .padding(MaterialTheme.appDimens.smallContentPadding)
      )
  }
}
```

### Test

```kotlin
class LoginKtTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun shouldEnableButtonOnlyWhenInputsAreValid() {

    val loginUseCase = mockk<LoginUseCaseImpl>(relaxed = true)
    val loginViewModel = LoginViewModel(loginUseCase)

    coEvery { loginUseCase.login(any(), any()) } returns Unit

    with(composeRule) {
      setContent { Login(onSuccess = {}, viewModel = loginViewModel) }
      onNodeWithTag("loginButton").assertIsNotEnabled()

      onNodeWithTag("emailInput").performTextInput("abcd")
      onNodeWithTag("loginButton").assertIsNotEnabled()

      onNodeWithTag("emailInput").performTextInput("abcd@gmail.com")
      onNodeWithTag("loginButton").assertIsNotEnabled()

      onNodeWithTag("passwordInput").performTextInput("12")
      onNodeWithTag("loginButton").assertIsNotEnabled()

      onNodeWithTag("passwordInput").performTextInput("12345")
      onNodeWithTag("loginButton").assertIsEnabled()
    }
  }
}
```
</details>

### Dependencies
```
// Needed for createComposeRule , createAndroidComposeRule and other rules used to perform UI test
testImplementation("androidx.compose.ui:ui-test-junit4:$compose_version") // used with robolectric to run ui test on jvm
androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version") // used with AndroidTestRunner to run ui test on virtual/physical device.

// Required to add androidx.activity.ComponentActivity to test manifest.
// Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")
```

### Android JUnit test

*** Write few lines about Android JUnit test ***

```
// To perform UI automation test.
androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
```

<details>
<summary>Instrumentation test that runs on Virtual/Physical/GradleManagedDevice</summary>

### System under test

Explain about system under test

### Test

```kotlin

```

</details>

### UI Automator

*** Write few lines about UI Automator ***

```
// To perform UI automation test.
androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
```

<details>
<summary>Instrumentation test with UI Automator</summary>

### System under test

Explain about system under test

### Test

```kotlin

```
</details>

### Command

```shell
./gradlew connectedAndroidTest --continue
```

### Gradle Managed Devices

Gradle Managed Devices provide a way to configure virtual or physical devices directly in Gradle for running integration
tests. Since the configuration is managed within Gradle, it gains full control over the device lifecycle, allowing it to
start or shut down devices as needed.

Unlike standard Android Virtual Devices (AVDs) or physical devices, there won’t be any visual preview during the test
run. Once the test completes, you can review the results in the reports generated in the build folder.

Gradle Managed Devices are primarily used for running automated tests at scale on various virtual devices, so the focus
is on configuration details rather than a visual representation.

### Setup

```kotlin
testOptions {
  managedDevices {
    devices {
      create<ManagedVirtualDevice>("testDevice") {
        device = "Pixel 6"
        apiLevel = 34
        systemImageSource = "aosp"
      }
    }
  }
}
```

### Command

```shell
./gradlew testDeviceDebugAndroidTest
```
<hr/>

## Reference

* https://developer.android.com/training/testing/instrumented-tests/ui-tests
* https://developer.android.com/jetpack/compose/testing
* https://developer.android.com/studio/test/test-in-android-studio
* https://developer.android.com/training/dependency-injection/hilt-testing
* https://developer.android.com/training/testing/other-components/ui-automator
* https://martinfowler.com/articles/practical-test-pyramid.html#ProviderTestourTeam
* https://martinfowler.com/bliki/TestDouble.html
* https://developer.android.com/studio/test/gradle-managed-devices
* https://developer.android.com/training/testing/other-components/ui-automator

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

- Unit test --- one element of the software at a time
- Test Double as the generic term for any kind of pretend object used in place of a real object for testing purposes.
  Keywords
- collaborators
- SUT
- DD-style way of writing tests
- Talking about different test classifications is always difficult.

```
// Dependencies 

// Allows us to create and configure mock objects, stub methods, verify method invocations, and more
androidTestImplementation("io.mockk:mockk-agent:1.13.5")
androidTestImplementation("io.mockk:mockk-android:1.13.5")
androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

// Assertion library
androidTestImplementation("com.google.truth:truth:1.1.4")

// Needed for createComposeRule , createAndroidComposeRule and other rules used to perform UI test
testImplementation("androidx.compose.ui:ui-test-junit4:$compose_version") // used with robolectric to run ui test on jvm
androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version") // used with AndroidTestRunner to run ui test on virtual/physical device.

// Required to add androidx.activity.ComponentActivity to test manifest.
// Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")

// To perform UI automation test.
androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")

// Used to create AndroidHiltTestRunner from AndroidJUnitRunner
androidTestImplementation("androidx.test:runner:1.6.2")

// Dependency injection for For instrumented tests on Android
androidTestImplementation("com.google.dagger:hilt-android-testing:2.49")
kaptAndroidTest("com.google.dagger:hilt-compiler:2.49")

// Dependency injection for For instrumented tests on JVM
testImplementation("com.google.dagger:hilt-android-testing:2.49")
kaptTest("com.google.dagger:hilt-compiler:2.49")

// Needed to run android UI test on JVM instead of on an emulator or device
testImplementation("org.robolectric:robolectric:4.10.3)

// Helper for other arch dependencies, including JUnit test rules that can be used with LiveData, coroutines etc
testImplementation("androidx.arch.core:core-testing:2.2.0")

// Regular JUnit dependency
testImplementation("junit:junit:4.13.2")

// Assertion library
testImplementation("com.google.truth:truth:1.1.4")

// Allows us to create and configure mock objects, stub methods, verify method invocations, and more
testImplementation("io.mockk:mockk:1.13.5")
```

