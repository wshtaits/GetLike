# GetLike!

## Overview
**GetLike!** is a Kotlin Multiplatform demo app for Android, iOS, and backend server that was built as a **technical showcase**.
It allows users to send likes to their contacts — one or thousands at a time.

**⚠️ Work in Progress:** This demo is feature-complete, but not ready for production deployment and may still contain some bugs to be fixed.
The server runs locally and can be temporarily exposed via [ngrok](https://ngrok.com/) for testing.

---

## Quick Walkthrough
<img src="./demo.gif" style="width:300px;" alt="Demo" />

---

## Features
* Authentication – via phone number
* Profile – creation, editing
* Contacts – adding, sharing, searching
* Chats
* Achievements – a lightweight gamification system that rewards user activity, for example:
    * 🖼️ Uploaded avatar — now you’ve got a face!
    * 🚀 Sent 100,000 likes at once — full power mode!
    * 👑 1,000,000 likes — you’re a true legend!
    * ...

---

## Tech Stack

### Mobile
- **UI:** Jetpack Compose Multiplatform
- **Architecture:** MVVM
- **Concurrency:** Kotlin Coroutines
- **Network:** Kotlin RPC, WebSockets, Firebase Messaging
- **Storage:** Room
- **Authentication:** Firebase Auth
- **Analytics:** Firebase Analytics/Crashlytics
- **iOS/Swift:** via CocoaPods
- **DI:** no external libraries, simple custom dependency injection via `PlatformDependenciesFactory` and `Dependencies`

### Server
- **Framework:** Ktor + Kotlinx RPC + WebSockets
- **Database:** PostgreSQL
- **ORM:** Exposed
- **Authentication and Notifications:** Firebase Admin SDK

---

## Project Structure
* **androidApp** – Android application entry point
* **iosApp** – iOS application (Xcode project)
* **build-logic** – custom Gradle convention plugins
* **client-base** – base client module
* **client-*** – screen modules
* **server** – backend server module
* **shared** – code shared between client and server
* **util-*** – Kotlin Multiplatform wrappers for platform-specific utilities
