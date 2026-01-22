E-Waste Hub Kenya: Sustainable Tech Disposal & Marketplace

A modern Android application built to solve the electronic waste crisis in Kenya. By leveraging Firebase's real-time capabilities and Room's local persistence, the app provides a secure and seamless platform for users to resell, donate or dispose of gadgets responsibly.

Project Overview: 
E-Waste Hub Kenya focuses on security and sustainability. By removing the need for a custom backend, the app ensures high availability and secure data handling through Google’s Firebase infrastructure.

Key Features:
1. Cloud-Synced Marketplace where e-waste items are listed for sale or donation with real-time updates via Firebase Firestore 
2. Secure Media Uploads when item images are stored and retrieved using Firebase Storage 
3. Identity Management through secure user registration and login handled by Firebase Authentication 
4. Stolen Serial Verification where a dedicated verification module that queries Firestore to ensure devices aren't flagged as stolen 
5. Offline-First Experience which uses room database to cache user data and listings, ensuring the app remains functional even without an internet connection  
6. Mpesa Sandbox API to simulate payment purchases and record transactions

Contributors :
Emmanuel Ochieng,
Derrick Kimutai, 
Timothy Kaaya and Joe Karogo
                 
Technical Stack
1. Component Technology Language Kotlin Architecture; MVVM (Model-View-ViewModel)
2. Authentication; Firebase Auth (Email/Password)
3. Cloud Database; Cloud Firestore (NoSQL)
4. Cloud Storage; Firebase Storage (Images)
5. Local Database; Room Persistence Library (SQLite wrapper)

Installation & Setup. 
Firebase Configuration
1. Create a project in the Firebase Console.
2. Add an Android app with the package name com.example.e_wastehubkenya.
Download the google-services.json and place it in the app/ folder.Enable Email/Password in Firebase Auth.
Create a Cloud Firestore database and a Firebase Storage bucket
3. Clone the repository: git clone https://github.com/emmanuelamor5/E-WastehubKenya.git
4. Open the project in Android Studio.
5. Wait for Gradle Sync to complete.
6. Run the app on your physical device or emulator.
7. Add debug token generated onto the App Check section on the console.
   
Target Users
1. Environmentally Conscious Sellers: Users looking to declutter old electronics safely.
2. Tech Enthusiasts: Buyers seeking affordable parts or second-hand gadgets.
3. Security-Minded Consumers: Users utilizing the serial checker to avoid buying stolen goods.
4. Recycling Partners: Organizations looking to collect e-waste at scale.

Security & Data
1. Data Security: All user data is protected by Firebase Security Rules, ensuring users can only edit their own listings.
2. Integrity: The stolen serial database is a read-only collection for general users, preventing unauthorized tampering with security records.
