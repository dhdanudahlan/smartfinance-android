**Project: SmartFinance**
==================================

A personal finance tracker that helps users manage their income, expenses, and financial goals.

**Core Features**
------------
- Income & Expense Tracking: Add, edit, and delete transactions.
- Category Management: Categorize transactions (e.g., food, travel, bills).
- Analytics & Insights: Visualize spending trends using charts.
- Budgeting: Allow users to set monthly budgets and get alerts when exceeding limits.
- Reminders: Notify users about upcoming bills.
- Offline Support: Enable the app to work seamlessly without an internet connection.
- Cloud Backup: Sync data using Firebase.

**App Architechture**
------------
- Architechture: MVVM (Model-View-ViewModel) with Clean Architecture
- Programming Language: Kotlin
- UI Framework: Jetpack Compose
- Data Layer: Room Database
- Dependency Injection: Hilt
- Asynchronous Processing: Kotlin Coroutines & Flow
- Firebase: Authentication, Firestore (optional for cloud sync), and Cloud Messaging (for reminders).
- Charts: MPAndroidChart library for analytics.
- Testing: JUnit, MockK, and Espresso.
