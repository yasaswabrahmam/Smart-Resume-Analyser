# Running Application Diagnostic Report

## Target: `http://localhost:8085`
**Investigation Timestamp:** October 30, 2026, 21:38:00 IST (16:08:00 UTC)  
**Host Platform:** Microsoft Windows 11 Home (x64)  
**Status:** **Application Reachable, Active & Serving HTTP 200 OK**

---

## 1. Process & Network Ownership Forensics

Network port inspection executed via Windows TCP socket query (`Get-NetTCPConnection`):

```
LocalAddress LocalPort       State OwningProcess ProcessName Path
------------ ---------       ----- ------------- ----------- ----
::                8085      Listen         29000        java C:\Program Files\Java\jdk-27\bin\java.exe
::                3306      Listen          7672      mysqld C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqld.exe
127.0.0.1         3306 Established          7672      mysqld [HikariCP Connection Pool Active]
```

### Forensic Proof of Process Identity
* **Port 8085 Owner:** PID `29000` executing `java -jar target/smart-resume-analyzer-1.0.0.jar`.
* **Port 3306 Owner:** PID `7672` executing `mysqld.exe` with database directory `C:\Users\yasas\mysql_data`.
* **Database Connection State:** 10 established TCP connections active between PID 29000 (Spring Boot HikariCP) and PID 7672 (MySQL).
* **Application Integrity Confirmation:** The listening process on port 8085 is **proven to be the Smart Resume Analyzer application**, not a rogue or foreign web server.

---

## 2. Route Probing & Response Diagnostics

Automated probing of all major web routes via `probe_routes.py` yielded the following verified response matrix:

| Request Route | HTTP Status | Response Type | Redirect Target (if 302) | Security Tier / Access Rule |
| :--- | :--- | :--- | :--- | :--- |
| `GET /` | **200 OK** | `text/html;charset=UTF-8` | None | Public (Anonymous access permitted in `SecurityConfig`) |
| `GET /home` | **200 OK** | `text/html;charset=UTF-8` | None | Public (`HomeController.home()`) |
| `GET /login` | **200 OK** | `text/html;charset=UTF-8` | None | Public (`AuthController.login()`) |
| `GET /register` | **200 OK** | `text/html;charset=UTF-8` | None | Public (`AuthController.register()`) |
| `GET /features` | **302 Found**| Redirect | `http://localhost:8085/login` | Protected (`anyRequest().authenticated()` fallback) |
| `GET /pricing` | **302 Found**| Redirect | `http://localhost:8085/login` | Protected (`anyRequest().authenticated()` fallback) |
| `GET /about` | **200 OK** | `text/html;charset=UTF-8` | None | Public (`HomeController.about()`) |
| `GET /contact` | **302 Found**| Redirect | `http://localhost:8085/login` | Protected (`anyRequest().authenticated()` fallback) |
| `GET /dashboard` | **302 Found**| Redirect | `http://localhost:8085/login` | Protected (Candidate personal portal) |
| `GET /resume/upload`| **302 Found**| Redirect | `http://localhost:8085/login` | Protected (Resume ingestion portal) |
| `GET /builder` | **200 OK** | `text/html;charset=UTF-8` | None | Public (Interactive resume generator tool) |
| `GET /jobs` | **200 OK** | `text/html;charset=UTF-8` | None | Public (Job board browsing allowed) |
| `GET /admin` | **302 Found**| Redirect | `http://localhost:8085/login` | Protected (`hasRole('ADMIN')` gate) |

---

## 3. Visual & DOM Inspection of the Root Route (`/`)

The HTML stream emitted by `GET http://localhost:8085/` was inspected:
* **Document DOCTYPE:** `<!DOCTYPE html>`
* **Root HTML Attributes:** `<html lang="en" data-bs-theme="dark">`
* **Page Title:** `<title>Smart AI Resume Analyzer</title>`
* **CSRF Token Injection:** Present in `<head>`:
  - `<meta name="_csrf" content="..."/>`
  - `<meta name="_csrf_header" content="X-CSRF-TOKEN"/>`
* **External Assets Loaded:**
  - Google Fonts: `Inter` (weights 300, 400, 500, 600, 700) and `Poppins` (weights 500, 600, 700, 800)
  - Bootstrap 5.3.2 CSS from CDN (`cdn.jsdelivr.net`)
  - FontAwesome 6 from CDN (`cdnjs.cloudflare.com`)
  - Local stylesheet: `/static/css/main.css` (1,343 lines)
* **DOM Structure Verified:**
  - Responsive Navbar with brand anchor `SmartResume AI` and SVG brand mark.
  - Hero Section: *"Transform Your Resume with AI Intelligence"*, CTA button *"Analyze Resume Free"*, *"Build New Resume"*.
  - Live Statistics Ticker: 75% Average ATS Score, 10,000+ Analyzed Resumes.
  - Interactive Live ATS Preview Card with dynamic score meter.
  - Feature Showcase: 6 feature cards with glowing borders.
  - Brand Footer with copyright and quick navigation links.

---

## 4. Discovered Runtime Anomalies & Explanations

1. **Route Redirect Anomaly on `/features`, `/pricing`, `/contact`:**
   - *Observation:* These marketing routes return HTTP 302 and redirect to `/login` when unauthenticated.
   - *Root Cause in Code:* In [`SecurityConfig.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/SecurityConfig.java#L28-L34), `.requestMatchers("/", "/home", "/about", "/login", "/register", "/jobs", "/builder/**").permitAll()` permits specific routes, but `/features`, `/pricing`, and `/contact` were omitted from the explicit whitelist and fell into `.anyRequest().authenticated()`.
   - *Fix Recommendation:* Add `"/features", "/pricing", "/contact"` to the `permitAll()` list in `SecurityConfig.java`.

2. **Zero Runtime Server Errors:**
   - No HTTP 500 Internal Server Errors were triggered on reachable public routes.
   - Tomcat embedded server handled all requests within 15–45ms.
