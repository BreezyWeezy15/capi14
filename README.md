# Countries App

## Overview

The Countries App is an Android application that fetches and displays a list of countries using data from a remote JSON endpoint. This application demonstrates modern Android development practices, including dependency injection with Hilt, state management with Kotlin Flows, and asynchronous operations using coroutines.

## Architecture

The app follows a clean architecture pattern with the following components:

### 1. Model

The `CountriesModel` class extends `ArrayList<CountriesModelItem>`. This class is used to manage a list of country items.

- **Class**: `CountriesModel`
- **Purpose**: To represent and manage a list of country items fetched from the network.

### 2. Network Service

#### AuthService

The `AuthService` interface defines the network operations needed to fetch country data.

- **Interface**: `AuthService`
- **Method**: `fetchCountries()`
  - **Annotation**: `@GET("countries.json")`
  - **Type**: `suspend fun` (for asynchronous operations with coroutines)
  - **Purpose**: Fetches data from the `countries.json` endpoint and returns it as a `CountriesModel`.

### 3. Repository

#### CountriesRepo

The `CountriesRepo` class is responsible for fetching data from the `AuthService` and implementing the `CountriesRepoImpl` interface.

- **Class**: `CountriesRepo`
- **Constructor**: Injected `AuthService` instance.
- **Method**: `fetchData()`
  - **Type**: `suspend fun`
  - **Purpose**: Calls `authService.fetchCountries()` to retrieve country data and return it.

#### CountriesRepoImpl

The `CountriesRepoImpl` interface defines the contract for repository operations.

- **Interface**: `CountriesRepoImpl`
- **Method**: `fetchData()`
  - **Purpose**: Ensures that any repository implementation provides a method to fetch data.

### 4. ViewModel

#### CountriesViewModel

The `CountriesViewModel` manages the UI-related data and business logic for fetching and displaying country data.

- **Class**: `CountriesViewModel`
- **Constructor**: Injected `CountriesRepo` instance.
- **Initialization**: Fetches data immediately upon creation.
- **Method**: `fetchRemoteJson()`
  - **Purpose**: Sets the state to `LOADING`, fetches data on the `IO` dispatcher, and updates the state based on the result.
- **State Handling**:
  - **`handleResponse(data)`**: Updates state to `SUCCESS` with the fetched data.
  - **`handleError(e)`**: Updates state to `ERROR` with the exception message.
- **StateFlow**: `_countriesState` MutableStateFlow to observe and manage state (loading, success, error).

### 5. MainActivity

#### MainActivity

The `MainActivity` is the main entry point of the app and is responsible for displaying the country data.

- **Class**: `MainActivity`
- **UI Setup**:
  - **RecyclerView**: Initializes and sets up the RecyclerView and adapter.
  - **Window Insets**: Adjusts padding to accommodate system bars.
- **State Observation**:
  - **Loading State**: Displays a progress bar.
  - **Success State**: Updates the adapter with fetched data and hides the progress bar.
  - **Error State**: Displays an error message and hides the progress bar.

### 6. Testing

#### CountriesRepoTest

The `CountriesRepoTest` class ensures that the `CountriesRepo` correctly interacts with the `AuthService` and returns the expected data.

- **Class**: `CountriesRepoTest`
- **Setup**:
  - **Mocking**: Mocks `AuthService` to provide controlled responses.
- **Test Execution**:
  - **Method**: `fetchData()`
    - **Purpose**: Calls `fetchData` and compares the result with expected values.
- **Verification**:
  - **Ensures**: `fetchCountries` method was called on `AuthService`.


