# Machine Learning and Python Forensic Audit

## Subject: Investigation of ML Models, Python Ecosystem and Inference Architecture
**Audit Date:** October 30, 2026  
**Auditor:** Technical Forensic Auditor  
**Status:** **Forensically Verified: Zero Active ML Models / Zero Python Code in Current Baseline**

---

## 1. Definitive Status of Python & ML Libraries

An exhaustive file-level and classpath inspection across `Smart-Resume-Analyzer-Java` confirms the following exact implementation status for every technology:

| Technology / Library | Forensic Status in Codebase | Evidence / File Path | Operational Notes |
| :--- | :--- | :--- | :--- |
| **Python Runtime** | **NOT PRESENT** | 0 `*.py` files in repository | No Python environment or scripts exist in the project directory. |
| **FastAPI** | **PLANNED** | None in codebase | Planned as a sidecar microservice for Phase 4; not currently authored. |
| **Pydantic** | **PLANNED** | None in codebase | Planned for request/response validation in Phase 4 Python service. |
| **Scikit-learn** | **PLANNED** | None in codebase | Planned for cosine similarity matrix scoring in Phase 4. |
| **spaCy** | **PLANNED** | None in codebase | Planned for Named Entity Recognition (NER) skill extraction in Phase 4. |
| **Sentence-Transformers** | **PLANNED** | None in codebase | Planned for dense 384-dimensional embeddings (`all-MiniLM-L6-v2`) in Phase 4. |
| **PyTorch (torch)** | **PLANNED** | None in codebase | Planned as CPU-only tensor runtime in Phase 4. |
| **NumPy & Pandas** | **PLANNED** | None in codebase | Planned for dataset preprocessing and evaluation benchmarks. |
| **Model Artifacts (.pkl, .pt, .onnx)**| **NOT PRESENT** | 0 model artifact files | No serialized neural weights, checkpoints, or pipelines exist on disk. |
| **Java-to-Python IPC** | **NOT PRESENT** | No HTTP client configured for port 8000 | Java communicates solely with MySQL and Google Gemini cloud endpoint. |

---

## 2. Definitive Finding on ATS Score Prediction

> [!IMPORTANT]
> **No machine learning model is currently predicting or contributing to the ATS score.**
> - The ATS score is calculated **100% deterministically in Java source code** via static string matching and weighted arithmetic in [`ResumeAnalyzerService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/ResumeAnalyzerService.java).
> - There are no neural networks, random forests, regression weights, or trained classifiers running in the application today.
> - Any presentation referring to an "AI / ML predictive model" is describing the **planned Phase 4 architecture**, not the executable code currently running.
