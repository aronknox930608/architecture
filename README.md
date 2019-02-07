# Android Architecture Blueprints

<img src="https://github.com/googlesamples/android-architecture/wiki/images/aab-logo.png" alt="Android Architecture Blueprints"/>

The Android framework provides a lot of flexibility in deciding how to organize and <em>architect</em> an Android app. While this freedom is very valuable, it can also lead to apps with large classes, inconsistent naming schemes, as well as mismatching or missing architectures. These types of issues can make testing, maintaining and extending your apps difficult.

The Android Architecture Blueprints project demonstrates strategies to help solve or avoid these common problems. This project implements the same app using different architectural concepts and tools.

You can use the samples in this project as a learning reference, or as a starting point for creating your own apps. The focus of this project is on demonstrating how to structure your code, design your architecture, and the eventual impact of adopting these patterns on testing and maintaining your app. You can use the techniques demonstrated here in many different ways to build apps. Your own particular priorities will impact how you implement the concepts in these projects, so you should not consider these samples to be canonical examples. To ensure the focus is kept on the aims described above, the app uses a simple UI.

## Explore the samples

This project hosts each sample app in separate repository branches. For more information, see the `README.md` file in each branch.

### Stable samples - Java
| Sample | Description |
| ------------- | ------------- |
| [todo‑mvp](https://github.com/googlesamples/android-architecture/tree/todo-mvp/) | Demonstrates a basic [Model‑View‑Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) (MVP) architecture and provides a foundation on which the other samples are built. This sample also acts as a reference point for comparing and contrasting the other samples in this project. |
| [todo‑mvp‑clean](https://github.com/googlesamples/android-architecture/tree/todo-mvp-clean/) | Uses concepts from [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html). |
| [todo‑mvp‑dagger](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/) | Uses [Dagger 2](https://google.github.io/dagger/) to add support for [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection). |
| [todo‑mvp‑rxjava](https://github.com/googlesamples/android-architecture/tree/todo-mvp-rxjava/) | Uses [RxJava 2](https://github.com/ReactiveX/RxJava) to implement concurrency, and abstract the data layer. |
| [todo‑mvvm‑databinding](https://github.com/googlesamples/android-architecture/tree/todo-mvvm-databinding/) | Based on the todo-databinding sample, this version incorporates the [Model‑View‑ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) pattern.|
| [todo‑mvvm‑live](https://github.com/googlesamples/android-architecture/tree/todo-mvvm-live/) | Uses ViewModels and LiveData from [Architecture Components](http://developer.android.com/arch) and the Data Binding library with an MVVM architecture. |

### Stable samples - Kotlin
| Sample | Description |
| ------------- | ------------- |
| [todo-mvp-kotlin](https://github.com/googlesamples/android-architecture/tree/todo-mvp-kotlin/) | Conversion of todo-mvp to Kotlin. |
| [todo-mvvm-live-kotlin](https://github.com/googlesamples/android-architecture/tree/todo-mvvm-live-kotlin/) | Conversion of todo-mvvm-live to Kotlin. |

### External samples
[External samples](https://github.com/googlesamples/android-architecture/wiki/External-samples) are variants that may not be in sync with the rest of the branches in this repository.

| Sample | Description |
| ------------- | ------------- |
| [todo‑mvp‑fragmentless](https://github.com/Syhids/android-architecture/tree/todo-mvp-fragmentless) | Uses [View](https://developer.android.com/reference/android/view/View.html) objects instead of [Fragment](https://developer.android.com/reference/android/app/Fragment.html) objects.|
| [todo‑mvp‑conductor](https://github.com/grepx/android-architecture/tree/todo-mvp-conductor) | Uses the [Conductor](https://github.com/bluelinelabs/Conductor) framework to refactor the app to use a single Activity architecture. |
| [todo‑mvi-rxjava](https://github.com/oldergod/android-architecture/tree/todo-mvi-rxjava) | Adapts the [Model-View-Intent](https://cycle.js.org/model-view-intent.html) pattern to Android to create a fully reactive architecture. |
| [todo‑mvp-kotlin-coroutines](https://github.com/dmytrodanylyk/android-architecture/tree/todo-mvp-kotlin-coroutines) | Replaces the asynchronous operations with [Kotlin's coroutines](https://github.com/Kotlin/kotlinx.coroutines/blob/master/README.md#documentation). |


### Deprecated samples

These samples are no longer being maintained, but their implementation is still valid.

| Sample | Description |
| ------------- | ------------- |
| [todo‑mvp‑loaders](https://github.com/googlesamples/android-architecture/tree/deprecated-todo-mvp-loaders/) | Fetches data using the [Loaders API](https://developer.android.com/guide/components/loaders.html). |
| [todo‑databinding](https://github.com/googlesamples/android-architecture/tree/deprecated-todo-databinding/) | Replaced by [todo‑mvvm‑databinding](https://github.com/googlesamples/android-architecture/tree/todo-mvvm-databinding/) |
[todo‑mvp‑contentproviders](https://github.com/googlesamples/android-architecture/tree/deprecated-todo-mvp-contentproviders/) | Based on the todo-mvp-loaders sample, this version fetches data using the Loaders API, and also makes use of [content providers](https://developer.android.com/guide/topics/providers/content-providers.html). |

### Samples in progress

| Sample | Description |
| ------------- | ------------- |
| [dev‑todo‑mvvm‑rxjava](https://github.com/googlesamples/android-architecture/tree/dev-todo-mvvm-rxjava/) | Based on the todo-rxjava sample, this version incorporates the [Model‑View‑ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) pattern.|

For information about planned samples, see ["New sample" issues](https://github.com/googlesamples/android-architecture/issues?q=is%3Aissue+is%3Aopen+label%3A%22New+sample%22).

## Why a to-do app?

The app in this project aims to be simple enough that you can understand it quickly, but complex enough to showcase difficult design decisions and testing scenarios. For more information, see the [app's specification](https://github.com/googlesamples/android-architecture/wiki/To-do-app-specification).

The following screenshot illustrates the UI of the app:

<img src="https://github.com/googlesamples/android-architecture/wiki/images/tasks2.png" alt="A screenshot illustratrating the UI of the app" width="160" style="display: inline; float: right"/>

## Choose a sample for your app

Each sample includes a dedicated `README.md` file where you can find related metrics, as well as subjective assessments and observations by contributors. The following factors are worth considering when selecting a particular sample for your app:

* The size of the app you are developing
* The size and experience of your team
* The amount of maintenance that you are expecting to have to do
* Whether you need a tablet layout
* Whether you need to support multiple platforms
* Your preference for the compactness of your codebase

For more information on choosing and comparing samples, see the following page: [How to compare samples](https://github.com/googlesamples/android-architecture/wiki/How-to-compare-samples)

## Open a sample in Android Studio

To open one of the samples in Android Studio, begin by checking out one of the sample branches, and then open the `todoapp/` directory in Android Studio. The following series of steps illustrate how to open the [todo‑mvp](https://github.com/googlesamples/android-architecture/tree/todo-mvp) sample.

**Note:** The master branch does not compile.

Clone the repository:

```
git clone git@github.com:googlesamples/android-architecture.git
```

Checkout the todo-mvp sample:
```
git checkout todo-mvp 
```

**Note:** To review a different sample, replace `todo-mvp` with the name of sample you want to check out.

Finally open the `todoapp/` directory in Android Studio.

## Contributors

This project is **built by the community**, and curated by Google as well as other core [maintainers](https://github.com/googlesamples/android-architecture/wiki/Contributors).

# License

Copyright 2019 Google Inc. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
