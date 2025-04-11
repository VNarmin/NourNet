# 🌱 NourNet     

**Empowering communities by reducing food waste and feeding those in need.**
 
> *Developed as part of the Software Engineering course at Baku Higher Oil School.* 

---

## 📱 About the App
 
**NourNet** is a mobile-first Food Waste Management and Donation platform that connects food donors—restaurants, grocery stores, and individuals—with people or organizations in need. The app facilitates real-time food listing, claiming, and geolocation-based coordination while emphasizing security, trust, and sustainability.

It contributes to the UN Sustainable Development Goals:

- 🧑‍🤝‍🧑 **No Poverty**
- 🍽️ **Zero Hunger**
- ⚖️ **Reduced Inequality**

---

## ✨ Features

- 🔐 Secure Authentication with Firebase (including 2FA)
- 📦 Food Listing & Claiming in real time
- 📍 Geolocation-Based Matching via Google Maps
- ⏰ Food Expiration Notifications
- 📊 Donation Impact Tracker
- 💬 User Reviews & Ratings
- 🎮 Gamification & Rewards System
- 🌐 Multi-language Support
- 🚚 Volunteer Coordination for Pickups
- 🛡️ Data Privacy & AES-256 Encryption

---

## 🧰 Tech Stack

| Layer        | Technology                             |
|--------------|-----------------------------------------|
| Frontend     | Kotlin (Android Studio)                |
| Backend      | Firebase (Auth, Firestore, Realtime DB)|
| Location     | Google Maps API                        |
| Local Storage| Room Database                          |
| Security     | Firebase Auth, AES-256, HTTPS          |
| CI/CD        | GitLab Pipelines (Planned Integration) |

---

## 📅 Project Timeline (MVP – 12 Weeks)

| Phase                           | Duration                  |
|---------------------------------|---------------------------|
| Requirements & Research         | Jan 24 – Jan 31, 2025     |
| UI/UX Design & Prototyping      | Feb 1 – Feb 14, 2025      |
| Core System Development         | Feb 15 – Mar 21, 2025     |
| Testing & Debugging             | Mar 22 – Apr 4, 2025      |
| Deployment & User Training      | Apr 5 – Apr 11, 2025      |

---

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/VNarmin/NourNet.git
   ```
2. Open the project in Android Studio.
3. Configure Firebase project and get your google-services.json.
4. Set up Maps API key in AndroidManifest.xml.
5. Build and run the app on an emulator or Android device.

---

## 🔒 Security Highlights

- 🧾 Secure user authentication
- 🔐 AES-256 encryption at rest
- 🧪 2FA & session expiration
- 🛡️ Role-based access control (RBAC)
- 🧠 Rate limiting, CAPTCHA, anti-abuse systems

---

## 📈 Future Enhancements

- 🤖 AI-driven donation suggestions
- 📦 Integration with local logistics services
- 📱 iOS version with Kotlin Multiplatform
- 🔗 Blockchain donation transparency
- 📊 Admin analytics dashboards

---

## 💚 Social Impact

- 🍲 Reduce food waste
- 🧍‍♂️ Support food-insecure individuals
- 🌿 Decrease carbon footprint
- 🧠 Educate users on sustainability

---

## 🔐 Security Integration Highlights

Snyk Integration – SAST Scanning
We integrated Snyk into our GitLab CI/CD pipeline to perform Static Application Security Testing (SAST). Snyk continuously scans the codebase for vulnerabilities in both custom code and open-source dependencies.

- 🔎 Detects known vulnerabilities
- 🔒 Suggests remediations
- 📉 Prevents insecure code from merging

Snyk SAST Scan Preview:
![](res/screenshots/snyk-dashboard.png)

---

## 🚀 GitLab Security Dashboard

We utilize [GitLab](https://gitlab.com/software-engineering2400315/nournet)’s Secure DevOps tools to automate security testing and maintain a comprehensive Security Dashboard. This includes:

- ✅ SAST (Snyk, GitLab)
- 📦 Dependency Scanning
- 🔍 Secret Detection (Gitleaks)
- 🧪 Mobile Security Testing (MobSF)
- ⚠️ Container Scanning (Planned)

GitLab aggregates results in a unified dashboard for faster vulnerability triage:

![](res/captures/gitlab-vuln-report.gif)

[dependency scanning report](res/security-reports/gl-dependency-scanning-report.json)

[sast report](res/security-reports/gl-sast-report.json)

[secret detection report](res/security-reports/gl-secret-detection-report.json)

[vulnerabilities report](res/security-reports/nournet_vulnerabilities_2025-04-11T1844.csv)

---

## 👥 Contributors

Project Team:
  - Nigar Abasli — Security & Research Lead
  - Narmin Valiyeva — Developer & UI/UX Designer
  - Shahana Huseynzade — Developer & Analyst
    
Instructor: 
  - Khayyam Masiyev

Institution: 
  - Baku Higher Oil School

Course: 
  - Software Engineering
