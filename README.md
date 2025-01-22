# SmartFinance

A personal finance tracker that helps users manage their income, expenses, and financial goals.

## Core Features.
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
### Core Tech Stack:
- Architecture: MVVM (Model-View-ViewModel) with Clean Architecture.
- Programming Language: Kotlin
- UI Framework: Jetpack Compose
- Database: Room.
- Dependency Injection: Hilt
- Networking: Retrofit with Kotlin Coroutines for API calls.
- State Management: State Hoisting, ViewModel’s StateFlow.
- Security: Encrypted SharedPreferences/SQLCipher for sensitive data.
- Firebase: Authentication, Firestore (optional for cloud sync), and Cloud Messaging (for reminders).
- Charts: MPAndroidChart library for analytics.
### Testing:
- Unit Tests: JUnit for business logic.
- UI Tests: Espresso and Jetpack Compose Test.
- Code Coverage Tools: JaCoCo or Kover.
### Development Tools:
- Version Control: Git (use GitHub/GitLab/Bitbucket for portfolio visibility).

[//]: # (- CI/CD: GitHub Actions, CircleCI, or Bitrise for automated builds and testing. )

[//]: # (- Performance Tools: Android Profiler, LeakCanary for memory leaks. )

[//]: # (- Analytics: Firebase Analytics or Mixpanel for user behavior tracking.)


## Credits
SmartFinance is a personal project developed by myself (dimas/dimashdanud/dhdanudahlan/aetherized)
