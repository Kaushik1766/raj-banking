# Raj Banking - Loan Application System

A Bank Loan Application System that lets **Maker** users create loan
applications and **Checker** users approve or reject them, built with a
classic Servlet/JSP MVC stack, Hibernate, Jakarta Bean Validation and Log4j2.

## Architecture

| Layer      | Technology                                            |
|------------|--------------------------------------------------------|
| View       | JSP + JSTL (`WEB-INF/views`)                            |
| Controller | Jakarta Servlets (`com.rajbank.loanapp.controller`)     |
| Service    | Plain Java services with business/eligibility rules     |
| DAO        | Hibernate ORM, explicit transaction management          |
| Model      | JPA entities with Jakarta Bean Validation annotations   |
| Security   | BCrypt password hashing, selector/verifier remember-me tokens |
| Logging    | Log4j2 (console + rolling file)                         |
| i18n       | `messages*.properties` (English default, French)        |

### Package layout

```
com.rajbank.loanapp
├── model        Customer, LoanApplication, User, enums, RememberMeToken, SequenceCounter
├── dao          DAO interfaces + Hibernate implementations (dao.impl)
├── service      AuthService, CustomerService, LoanApplicationService, EligibilityService, IdGeneratorService
├── controller   LoginServlet, MakerDashboardServlet, CustomerXxxServlet, LoanApplicationXxxServlet,
│                CheckerDashboardServlet, CheckerReviewServlet, StatusServlet, LocaleServlet
├── filter       AuthenticationFilter (role-based access), CharacterEncodingFilter
├── listener     AppContextListener (Hibernate bootstrap + default user seeding)
└── util         HibernateUtil, PasswordUtil, MessageUtil, ValidatorUtil, CookieUtil, Constants
```

## Features implemented

- **Common login page** with a "Keep me signed in" option. Checking it stores
  an opaque selector/verifier pair in a cookie (never the password); on return
  the user is silently re-authenticated and redirected straight to their
  Loan Application screen.
- **Maker flow**: dashboard of existing applications (create / delete),
  existing-vs-new customer choice, customer search & prefilled edit form
  (identity fields locked), new customer registration, loan application form,
  and loan application number generation on submit.
- **Checker flow**: pending applications list, a review screen with an
  automated eligibility check, and Approve/Reject actions that set the
  application status.
- **Public status check** (`/status`) - no login required - to look up an
  application by its Application Number at any time.
- All validation messages from the spec (`Username and Password does not
  match`, `Customer Id is invalid/not available`, `Customer Id is
  mandatory`, `Application Number is invalid/not available`, `Application Id
  is mandatory`, `Loan is Approved`, `Loan is Rejected`) are wired through
  `messages*.properties` for i18n.

## Eligibility criteria

`EligibilityService` evaluates, for every pending application:

1. Customer age between 21 and 60.
2. Customer has a verifiable employment type (not `UNEMPLOYED`).
3. Estimated EMI (loan amount / tenure) does not exceed 40% of monthly income.
4. Loan amount does not exceed 5x the customer's annual income.

All criteria satisfied → **"Loan is Approved"**; otherwise → **"Loan is
Rejected"**, with the specific reasons shown to the Checker. The Checker
still makes the final Approve/Reject call.

## Default accounts

Seeded automatically on first startup (`AppContextListener`):

| Role    | Username  | Password    |
|---------|-----------|-------------|
| Maker   | `maker1`  | `Maker@123` |
| Checker | `checker1`| `Checker@123` |

## Running locally

### Prerequisites

- JDK 17+
- Apache Maven 3.9+
- MySQL 8.x (a `raj_banking` schema is auto-created; update
  `src/main/resources/hibernate.cfg.xml` if your credentials differ)
- A Servlet 6.0 container, e.g. Apache Tomcat 10.1+

### Build & deploy

```bash
mvn clean package
# deploy target/loan-application.war to Tomcat's webapps/ directory
```

Then browse to `http://localhost:8080/loan-application/`.

### Running the tests

```bash
mvn test
```

Unit tests cover the validation/eligibility business rules; `DaoIntegrationTest`
exercises the Hibernate DAO layer end-to-end against an in-memory H2 database
(`src/test/resources/hibernate.cfg.xml` transparently overrides the MySQL
config for the test classpath), so the full suite runs without any external
database.

## Internationalization

The language switcher (EN / FR) in the header sets a `preferredLocale`
cookie; `MessageUtil` and the JSTL `<fmt:message>` tags read from the same
`messages` resource bundle family (`messages.properties` default,
`messages_en.properties`, `messages_fr.properties`).
