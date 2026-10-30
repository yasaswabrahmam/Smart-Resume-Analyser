# Landing Page Forensic Diagnostic Report

## Subject: Landing Page Preservation & Visibility Investigation
**Target URL:** `http://localhost:8085/` and `http://localhost:8085/home`  
**Investigation Date:** October 30, 2026  
**Status:** **100% Present, Reachable, Rendered & Visually Preserved**

---

## 1. Executive Forensic Finding

The original high-converting landing page is **intact, fully operational, and verified reachable at `http://localhost:8085/`**. It returns `HTTP 200 OK` with complete HTML markup, dark-mode CSS variables, responsive containers, and interactive widgets.

---

## 2. Source Code Asset Mapping

| Component | Exact File Path | Line Range / Identifier | Verification Status |
| :--- | :--- | :--- | :--- |
| **Landing View Template** | `src/main/resources/templates/index.html` | Lines 1–186 | **Verified Present** (Complete Thymeleaf template) |
| **Base HTML Layout** | `src/main/resources/templates/layout/base.html` | Lines 1–95 | **Verified Present** (Master wrapper & CSS/JS imports) |
| **Brand Identity Fragment** | `src/main/resources/templates/fragments/brand.html` | Lines 1–32 | **Verified Present** (3D animated SVG brand emblem) |
| **Top Navigation Fragment**| `src/main/resources/templates/fragments/navbar.html` | Lines 1–85 | **Verified Present** (Responsive navbar with auth state) |
| **Footer Fragment** | `src/main/resources/templates/fragments/footer.html` | Lines 1–65 | **Verified Present** (Platform disclaimer & links) |
| **Master CSS Stylesheet** | `src/main/resources/static/css/main.css` | Lines 1–1,343 | **Verified Present** (Bespoke design tokens, `#0B0F19`) |
| **Controller Route Handler**| `src/main/java/com/smartresume/controller/HomeController.java`| Lines 15–22 | **Verified Active** (`@GetMapping({"/", "/home"})`) |

---

## 3. Visual Identity & Design Token Verification

Inspection of [`main.css`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/resources/static/css/main.css) confirms that all visual tokens are defined:
* **Background Atmosphere:** Deep space dark theme:
  - `--bg-primary: #0B0F19;`
  - `--bg-secondary: #141A23;`
  - `--bg-card: #1A2232;`
  - `--border-subtle: rgba(255, 255, 255, 0.08);`
* **Vibrant Accent Accents:**
  - `--primary-indigo: #6366F1;`
  - `--accent-purple: #8B5CF6;`
  - `--success-emerald: #10B981;`
  - `--warning-amber: #F59E0B;`
  - `--danger-rose: #EF4444;`
* **Emblem Animation:**
  - Keyframe `@keyframes pulseGlow` driving glowing radial gradients on the 3D SVG emblem.
* **Typography:**
  - Headers: `'Poppins', sans-serif`
  - Body / Data: `'Inter', sans-serif`

---

## 4. Root-Cause Analysis: Why Might a User Think the Landing Page is Missing?

If a developer or evaluator previously reported that the landing page was "not appearing", our forensic probe identified three exact potential scenarios:

1. **Unpermitted Auxiliary Links Redirecting to `/login`:**
   - As documented in `RUNNING_APPLICATION_DIAGNOSTIC.md`, clicking *"Features"*, *"Pricing"*, or *"Contact"* in the navbar triggers an HTTP 302 redirect to `http://localhost:8085/login` because these specific paths were omitted from the public `permitAll()` whitelist in [`SecurityConfig.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/SecurityConfig.java#L28).
   - A user clicking one of those top-level links immediately lands on the login page and may falsely assume the landing page has been replaced by a login wall.
2. **Port Mismatch:**
   - Standard Spring Boot defaults to port `8080`. This project is explicitly configured in `application.properties` to run on **port `8085`**. Attempting to navigate to `http://localhost:8080` returns `ERR_CONNECTION_REFUSED`.
3. **Database Initialization Lag:**
   - If MySQL daemon on port 3306 is not started before Spring Boot starts, Hibernate halts during context creation, preventing Tomcat from listening on port 8085.

---

## 5. Non-Destructive Preservation Recommendation

* **Do NOT redesign or rewrite `index.html`.** The template is modern, elegant, and fully functional.
* **Recommended Security Filter Tweak (Phase 2):**
  In [`SecurityConfig.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/SecurityConfig.java#L28), update the public route matcher:
  ```java
  // Line 28 in SecurityConfig.java
  .requestMatchers("/", "/home", "/about", "/features", "/pricing", "/contact", "/login", "/register", "/jobs", "/builder/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
  ```
  This ensures clicking *"Features"*, *"Pricing"*, or *"Contact"* smoothly scrolls or renders the marketing pages without redirecting to `/login`.
