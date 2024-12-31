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

Unit testing usually refers testing a particular units of code totally in isolation with other component to ensure their
correctness and functionality.
To bring the isolation we need to seek help on framework like `Mockito` to create stubs (test doubles), mock.

#### Famous Unit testing frameworks

| Framework 			 | Description                                             |    
|---------------|---------------------------------------------------------|
| Junit         | Testing framework for Java                              |
| Mockito       | Mocking framework for unit tests written in Java/Kotlin |
| Truth         | To perform assertions in tests                          |

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
@HiltViewModel
class ProfileViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val logoutUseCase: LogoutUseCase
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

  val logoutUseCase = mockk<LogoutUseCase>()

  val profileViewModel = ProfileViewModel(savedStateHandleMock, logoutUseCase)
  assertThat(profileViewModel.emailAddress).isEqualTo("abcd@gmail.com")
}
```

</details>


<details>
<summary>Test with mocks and stubs</summary>

#### System under test

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

#### Test

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

### Compose UI test Junit

<details>
<summary>Compose UI+Interaction Unit Test </summary>

#### System under test

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

#### Test

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

<hr/>

## Integration testing

Integration testing usually refers testing interaction between different components or modules of an application.

#### Integration Testing Frameworks

| Framework 			        | Description                                                                                                                                                                                                                                                                |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Robolectric			       | 	 To perform android UI/functional testing on JVM without the need for android device.<br/> * Test files are located inside the test folder                                                                                                                                |
| AndroidX test runner | Provides AndroidJUnitRunner which is a JUnit test runner that allows to run instrumented JUnit 4 tests on Android devices, including those using the Espresso, UI Automator, and Compose testing frameworks. <br/> * Test files are located inside the androidTest folder. |

#### Robolectric

<details>
<summary>Instrumentation test with Robolectric</summary>

Test uses `AndroidJUnitRunner` to run on android virtual/physical device.

#### System under test

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

#### Test

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

### Integration Testing Support

#### Gradle Managed Devices

Gradle Managed Devices offers a way to configure a virtual or real device in Gradle to run the integration test. Since
the configuration is added to Gradle, it allows Gradle to be aware of the device lifecycle and can start or shut down
the device as required.

#### Setup

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

#### Run

```shell
./gradlew testDeviceDebugAndroidTest
```

<hr/>

#### Reference

* https://developer.android.com/training/testing/instrumented-tests/ui-tests
* https://developer.android.com/jetpack/compose/testing
* https://developer.android.com/studio/test/test-in-android-studio
* https://developer.android.com/training/dependency-injection/hilt-testing
* https://developer.android.com/training/testing/other-components/ui-automator
* https://martinfowler.com/articles/practical-test-pyramid.html#ProviderTestourTeam
* https://martinfowler.com/bliki/TestDouble.html
* https://developer.android.com/studio/test/gradle-managed-devices

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

<hr/>
Commands

`./gradlew connectedAndroidTest --continue`

`./gradlew testDebugUnitTest`



