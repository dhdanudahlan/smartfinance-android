# SmartFinance

## Overview
SmartFinance is a personal finance manager app designed to help users manage their income, expenses, and financial goals effectively. With a user-friendly interface and robust features, the app empowers users to take control of their finances.

## Features.
### Budgeting
- Budget Creation: Users can create budgets for specific categories (e.g., Food, Transportation) with a monthly or weekly duration.
- Progress Tracking: Visualize budget utilization with progress bars or pie charts.
- Notifications: Notify users when spending approaches or exceeds the set budget.
### Expense Tracking
- Expense Entry: Users can log expenses with details like amount, category, date, and optional notes.
- Categorization: Assign expenses to predefined or custom categories.
- Attachments: Allow users to attach receipts or images.
- Visual Insights: Provide charts and graphs showing expense trends over time.
### Financial Goal Setting
- Goal Creation: Users can set financial goals (e.g., saving $2000 for a vacation) with deadlines.
- Progress Tracking: Visualize savings progress with interactive charts.
- Automation: Suggest savings plans based on the goal and user’s financial habits.
### Other Features
- Generate detailed reports (PDF/Excel) for specified periods.
- Include expense breakdowns and budgeting summaries.
- Use biometric authentication and encrypted data storage

## Project Details
### Architecture
- Clean Architecture: Follows Clean Architecture principles with distinct layers:
  - Presentation Layer
  - Domain Layer
  - Data Layer
- MVVM Pattern
### Core Tech Stack:
- Programming Language: Kotlin
- UI Framework: Jetpack Compose
- Database: Room with migrations and DAO patterns
- Dependency Injection: Dagger-Hilt for scalable and testable dependency management
- Networking: Retrofit with Kotlin Coroutines for efficient API handling
- State Management: State Hoisting and ViewModel's StateFlow for UI state management
- Security: Encrypted SharedPreferences or SQLCipher for secure data storage
- Firebase Integration:
  - Firebase Authentication for user login
  - Firestore for cloud sync
  - Cloud Messaging for reminders and notifications
- Charts and Analytics: MPAndroidChart for financial visualizations
### Testing:
- Unit Testing: JUnit for validating business logic.
- UI Testing: Espresso and Jetpack Compose Test for UI interactions.
- Code Quality Checks: Integrated with tools like Ktlint and Detekt.
### Development Tools:
- Version Control: GitHub with clear branching strategies.
- CI/CD: GitHub Actions for automated builds, testing, and deployment pipelines.

[//]: # (- Performance Optimization:)

[//]: # (  - Android Profiler for debugging and performance analysis. )

[//]: # (  - LeakCanary for detecting memory leaks.)

[//]: # (- Analytics: Firebase Analytics or Mixpanel for user behavior tracking.)


## Instalation
1. Clone the repository:
`git clone https://github.com/your-repo-url.git`
2. Open the project in Android Studio.
3. Sync Gradle files and install dependencies.
4. Build and run the app on an emulator or physical device.

# Usage
...

## Credits
SmartFinance is a personal project developed by myself (dimas/dimashdanud/dhdanudahlan/aetherized)
