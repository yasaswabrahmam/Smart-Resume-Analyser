path = 'Smart-Resume-Analyzer-Java/src/main/resources/templates/dashboard/admin.html'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

target = '        <!-- Audit Activity Logs Table -->'

addition = """        <!-- MLOps Dataset Catalog & Training Pipeline Runner -->
        <div class="custom-card mb-4 p-4 border-info border-opacity-25" id="models" th:if="${datasetCatalog != null}">
            <div class="card-header-clean mb-3">
                <div>
                    <h5 class="fw-bold text-info mb-1 d-flex align-items-center gap-2">
                        <i class="fa-solid fa-server"></i>
                        <span>Approved Training Datasets &amp; Pipeline Orchestrator</span>
                    </h5>
                    <small class="text-muted">Registered open datasets with explicit consent gates. Private user data without consent is automatically rejected.</small>
                </div>
            </div>

            <div class="row g-3 mb-4">
                <div class="col-md-4" th:each="ds : ${datasetCatalog}">
                    <div class="p-3 rounded-2 h-100" style="background: rgba(255, 255, 255, 0.02); border: 1px solid var(--border-subtle);">
                        <div class="d-flex justify-content-between align-items-center mb-1">
                            <span class="badge bg-secondary text-info extra-small" th:text="${ds.license}">LICENSE</span>
                            <span class="badge bg-success bg-opacity-20 text-success extra-small"><i class="fa-solid fa-check me-1"></i> Consent OK</span>
                        </div>
                        <h6 class="text-white fw-bold mb-1" th:text="${ds.datasetName}">Dataset Name</h6>
                        <span class="text-muted extra-small d-block mb-2" th:text="${ds.datasetId}">ID</span>
                        <div class="d-flex justify-content-between text-muted extra-small">
                            <span>Samples: <strong class="text-white" th:text="${#numbers.formatInteger(ds.sampleCount, 0, 'COMMA')}">10,000</strong></span>
                            <span>Integrity: <strong class="text-success" th:text="${#numbers.formatPercent(ds.labelIntegrityScore, 1, 0)}">96%</strong></span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Trigger Training Form -->
            <form th:action="@{/admin/models/train}" method="post" class="p-3 rounded-2" style="background: rgba(14, 19, 26, 0.6); border: 1px solid var(--border-subtle);">
                <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />
                <div class="row g-2 align-items-end">
                    <div class="col-md-4">
                        <label class="form-label text-muted extra-small mb-1">Target Approved Dataset</label>
                        <select name="datasetId" class="form-select custom-input form-select-sm">
                            <option th:each="ds : ${datasetCatalog}" th:value="${ds.datasetId}" th:text="${ds.datasetName} + ' (' + ${ds.datasetId} + ')'"></option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label text-muted extra-small mb-1">Task Capability</label>
                        <select name="taskType" class="form-select custom-input form-select-sm">
                            <option value="SKILL_EXTRACTION">Skill Extraction</option>
                            <option value="ROLE_CLASSIFICATION">Role Classification</option>
                            <option value="ATS_SCORING">ATS Scoring Evaluation</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label text-muted extra-small mb-1">Model Architecture</label>
                        <select name="architecture" class="form-select custom-input form-select-sm">
                            <option value="TF-IDF + Logistic Regression">TF-IDF + Logistic Regression</option>
                            <option value="spaCy EntityRuler + Token Matcher">spaCy EntityRuler + Token Matcher</option>
                            <option value="Sentence-Transformers Bi-Encoder">Sentence-Transformers Bi-Encoder</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary-custom btn-sm w-100">
                            <i class="fa-solid fa-play me-1"></i> Run Pipeline
                        </button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Audit Activity Logs Table -->"""

normalized = content.replace('\\r\\n', '\\n')
if target in normalized:
    new_content = normalized.replace(target, addition).replace('\\n', '\\r\\n')
    with open(path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    print("Updated admin.html successfully.")
else:
    print("Target not found in admin.html.")
