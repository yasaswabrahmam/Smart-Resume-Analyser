# Job Provider & Data Source Audit

**Smart Resume AI — Job Discovery & Aggregation Architecture**  
*Document Version: 2.0.0*  
*Generated: October 2026*

---

## 1. Overview: Authentic Job Discovery

A fundamental requirement of Smart Resume AI is **zero artificial or mock job postings**. Candidates must never be led to dead links or fabricated job postings.

The platform provides two complementary job discovery mechanisms:
1. **Live Remote API Ingestion (Remotive REST API)**: Direct programmatic retrieval of active software engineering requisitions.
2. **7-Portal Deep-Link Aggregator**: Dynamic query builder that constructs targeted, pre-filtered search URLs across the world's most popular hiring portals.

---

## 2. Remotive API Integration

### 2.1 Endpoint Specification
- **Provider**: Remotive Public API
- **Endpoint**: `https://remotive.com/api/remote-jobs`
- **Method**: `GET`
- **Authentication**: None required (Public Tech Jobs Endpoint)
- **Parameters Supported**:
  - `category`: `software-dev`
  - `search`: Dynamic keyword query matching the candidate's verified skills
  - `limit`: Default 20 jobs

### 2.2 Ingestion & Normalization Flow
Implemented in `JobSearchController.java`:
1. Client issues request to `/jobs/search?title=Java+Developer&location=Remote`.
2. Spring `RestClient` invokes Remotive API with timeout protection ($5,000\text{ms}$).
3. Response JSON is parsed into `Map<String, Object>` list containing:
   - `id`: Unique Remotive ID
   - `title`: Job Requisition Title
   - `company_name`: Employer Name
   - `candidate_required_location`: Geographic Eligibility
   - `url`: Direct, authentic link to the job posting
   - `tags`: Technologies required (e.g. `Java`, `Spring Boot`, `Docker`)
   - `publication_date`: ISO timestamp
4. Injected into Thymeleaf template `jobs/search.html` with **Apply on Official Site** and **Save to Tracker** action buttons.

---

## 3. 7-Portal Deep-Link Aggregator

When candidates want to search beyond Remotive (e.g. for regional on-site roles in Bangalore, London, New York, etc.), the system automatically generates optimized queries across 7 authenticated portals:

| # | Portal Name | Region / Focus | Deep-Link URL Format |
|---|---|---|---|
| **1** | **LinkedIn Jobs** | Global / Professional | `https://www.linkedin.com/jobs/search/?keywords={title}+{skills}&location={location}` |
| **2** | **Indeed** | Global / General | `https://www.indeed.com/jobs?q={title}+{skills}&l={location}` |
| **3** | **Naukri.com** | India / Tech & Enterprise | `https://www.naukri.com/{title}-jobs-in-{location}?k={skills}` |
| **4** | **Glassdoor** | Global / Salary & Reviews | `https://www.glassdoor.com/Job/jobs.htm?sc.keyword={title}+{skills}&locT=C&locKeyword={location}` |
| **5** | **Google Jobs** | Global Indexer | `https://www.google.com/search?q={title}+jobs+in+{location}&ibp=htl;jobs` |
| **6** | **Foundit (Monster)** | APAC / Gulf / US | `https://www.foundit.in/srp/results?query={title}+{skills}&locations={location}` |
| **7** | **Internshala** | India / College & Freshers | `https://internshala.com/internships/keywords-{title}` |

---

## 4. Application Tracker Interoperability

From the `/jobs/search` interface:
1. Every job card features a 1-click **Save to Tracker** button.
2. Clicking **Save to Tracker** issues a `POST /tracker/save` with:
   - `jobTitle`: Title of requisition
   - `company`: Employer name
   - `jobUrl`: Direct application URL
   - `location`: Position location
3. The job is persisted into the `job_matches` database table with initial status `SAVED`.
4. Users can subsequently advance the status to `APPLIED`, `INTERVIEWING`, `OFFERED`, or `REJECTED` in `/tracker`.
