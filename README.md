# Selenium TestNG Automation Framework

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Selenium](https://img.shields.io/badge/Selenium-4.18.1-green?style=flat-square&logo=selenium)
![TestNG](https://img.shields.io/badge/TestNG-7.9.0-red?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.9.12-blue?style=flat-square&logo=apachemaven)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-yellow?style=flat-square&logo=jenkins)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen?style=flat-square)

> Production-level End-to-End Test Automation Framework built with Java, Selenium WebDriver 4,
> TestNG, Maven, Page Object Model, Extent Reports, Log4j2, Apache POI, and Jenkins CI/CD.

---

## Table of Contents

- [About the Project](#about-the-project)
- [Tech Stack](#tech-stack)
- [Framework Architecture](#framework-architecture)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [How to Run Tests](#how-to-run-tests)
- [Test Scenarios](#test-scenarios)
- [Reports](#reports)
- [CI/CD Pipeline](#cicd-pipeline)
- [Project Structure](#project-structure)
- [Design Patterns Used](#design-patterns-used)
- [Author](#author)

---

## About the Project

This framework automates End-to-End test scenarios for the **SauceDemo** e-commerce application
(`https://www.saucedemo.com`). It demonstrates production-level automation engineering skills
including framework design, data-driven testing, parallel execution, and CI/CD integration.

**What it covers:**
- User authentication (valid, invalid, locked-out users)
- Product selection and cart management
- Complete checkout flow with shipping details
- Order confirmation validation
- Logout verification
- Data-driven testing from Excel using Apache POI

---

## Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 21 LTS | Programming language |
| Selenium WebDriver | 4.18.1 | Browser automation |
| TestNG | 7.9.0 | Test runner and assertions |
| Maven | 3.9.12 | Build tool and dependency management |
| WebDriverManager | 5.7.0 | Auto ChromeDriver management |
| Extent Reports | 5.1.1 | HTML test reporting |
| Log4j2 | 2.23.1 | Logging framework |
| Apache POI | 5.2.5 | Excel data-driven testing |
| Jenkins | LTS | CI/CD pipeline |
| GitHub | - | Source code management |

---

## Framework Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    TEST LAYER                           │
│         TestNG @Test classes + DataProvider             │
├─────────────────────────────────────────────────────────┤
│                  PAGE OBJECT MODEL                      │
│    LoginPage │ ProductsPage │ CartPage │ CheckoutPage   │
├─────────────────────────────────────────────────────────┤
│                  UTILITIES & BASE                       │
│  BaseTest │ DriverFactory │ ConfigReader │ WaitUtility  │
├──────────────────┬──────────────────────────────────────┤
│   LISTENERS      │           REPORTS                    │
│  TestListener    │        ExtentManager                 │
│  RetryAnalyzer   │        HTML Reports                  │
├──────────────────┴──────────────────────────────────────┤
│                   CI/CD PIPELINE                        │
│              GitHub → Jenkins → Report                  │
└─────────────────────────────────────────────────────────┘
```

**Key design decisions:**
- **ThreadLocal WebDriver** — enables safe parallel test execution
- **Page Object Model** — separates locators from test logic
- **Singleton ConfigReader** — loads properties once, reused everywhere
- **ITestListener** — hooks into TestNG events for auto screenshots on failure
- **IRetryAnalyzer** — automatically retries flaky tests up to 2 times

---

## Prerequisites

- Java JDK 21 ([Eclipse Temurin](https://adoptium.net))
- Apache Maven 3.9+ ([Download](https://maven.apache.org/download.cgi))
- Google Chrome (latest)
- Eclipse IDE for Java Developers
- Git

---

## Setup and Installation

```bash
# Clone the repository
git clone https://github.com/kishore9384j/selenium-testng-framework.git

# Navigate to project
cd selenium-testng-framework

# Install dependencies
mvn clean install -DskipTests
```

---

## How to Run Tests

### Run full regression suite
```bash
mvn clean test
```

### Run smoke suite only
```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/suites/smoke.xml
```

### Run on specific browser
```bash
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

### Run in headless mode (no browser window)
```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

### Run in parallel (configured in regression.xml)
```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/suites/regression.xml
```

---

## Test Scenarios

| Test ID | Test Name | Type | Data Source |
|---|---|---|---|
| TC001 | Valid login with correct credentials | Functional | config.properties |
| TC002 | Invalid login shows error message | Negative | Hardcoded |
| TC003 | Locked out user cannot login | Negative | Hardcoded |
| TC004 | Data-driven login with multiple users | Data-Driven | Excel (LoginData) |
| TC005 | Complete E2E checkout flow | E2E | config.properties |
| TC006 | Add multiple products to cart | Functional | Hardcoded |
| TC007 | Data-driven checkout with shipping details | Data-Driven | Excel (CheckoutData) |
| TC008 | Logout returns to login page | Functional | config.properties |

---

## Reports

After test execution, the Extent HTML report is generated at:

```
test-output/ExtentReports/TestReport_[timestamp].html
```

**Report includes:**
- Pass/Fail/Skip status for each test
- Step-by-step execution logs
- Screenshots automatically embedded on test failure
- System info (browser, OS, Java version, environment)
- Execution time per test

---

## CI/CD Pipeline

```
Developer pushes code
        │
        ▼
   GitHub Repository
        │
        ▼ (webhook / poll SCM)
   Jenkins Job
        │
        ▼
   mvn clean test (headless)
        │
        ▼
   TestNG Suite Execution
        │
        ▼
   Extent HTML Report
   published in Jenkins
```

**Jenkins job:** `SauceDemo-Automation`
**Build trigger:** Poll SCM every 5 minutes + manual Build Now
**Headless execution:** Chrome headless mode for CI environment

---

## Project Structure

```
selenium-testng-framework/
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   └── com/saucedemo/automation/
│   │   │       ├── base/
│   │   │       │   └── BaseTest.java
│   │   │       ├── pages/
│   │   │       │   ├── BasePage.java
│   │   │       │   ├── LoginPage.java
│   │   │       │   ├── ProductsPage.java
│   │   │       │   ├── CartPage.java
│   │   │       │   ├── CheckoutPage.java
│   │   │       │   └── ConfirmationPage.java
│   │   │       ├── tests/
│   │   │       │   ├── LoginTest.java
│   │   │       │   └── E2ECheckoutTest.java
│   │   │       ├── utils/
│   │   │       │   ├── DriverFactory.java
│   │   │       │   ├── ConfigReader.java
│   │   │       │   ├── WaitUtility.java
│   │   │       │   ├── ScreenshotUtility.java
│   │   │       │   └── ExcelUtility.java
│   │   │       ├── listeners/
│   │   │       │   ├── TestListener.java
│   │   │       │   └── RetryAnalyzer.java
│   │   │       └── reports/
│   │   │           └── ExtentManager.java
│   │   └── resources/
│   │       ├── config.properties
│   │       ├── log4j2.xml
│   │       ├── screenshots/
│   │       ├── suites/
│   │       │   ├── regression.xml
│   │       │   └── smoke.xml
│   │       └── testdata/
│   │           └── TestData.xlsx
└── pom.xml
```

---

## Design Patterns Used

| Pattern | Where Used | Why |
|---|---|---|
| Page Object Model | All page classes | Separates UI locators from test logic |
| Singleton | ConfigReader | Load config.properties only once |
| Factory | DriverFactory | Creates correct WebDriver based on browser param |
| ThreadLocal | DriverFactory | Thread-safe driver for parallel execution |
| Fluent Interface | Page methods | Method chaining — login() returns ProductsPage |

---

## Author

**NandhaKishore**
- GitHub: [@kishore9384j](https://github.com/kishore9384j)
- Project: [selenium-testng-framework](https://github.com/kishore9384j/selenium-testng-framework)

---

*Built as a production-level placement project demonstrating SDET skills for product-based companies.*
