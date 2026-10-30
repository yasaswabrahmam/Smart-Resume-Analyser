import os

path = 'Smart-Resume-Analyzer-Java/src/main/resources/templates/layout/base.html'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

target = """            <div class="sidebar-nav-section">Part 2: Explore</div>
            <nav class="sidebar-nav">
                <a class="nav-link" th:href="@{/explore}">
                    <i class="fa-solid fa-compass text-info"></i>
                    <span>Career Explorer</span>
                    <span class="badge bg-info bg-opacity-20 text-info nav-pill-badge">Roles</span>
                </a>
                <a class="nav-link" th:href="@{/jobs}">
                    <i class="fa-solid fa-briefcase text-warning"></i>
                    <span>Jobs &amp; Internships</span>
                </a>
            </nav>

            <div class="sidebar-nav-section">Part 3: Prepare</div>
            <nav class="sidebar-nav">
                <a class="nav-link" th:href="@{/analyzer}">
                    <i class="fa-solid fa-file-magnifying-glass text-success"></i>
                    <span>Resume &amp; ATS</span>
                </a>
                <a class="nav-link" th:href="@{/skill-gap}">
                    <i class="fa-solid fa-layer-group text-danger"></i>
                    <span>Skills &amp; Projects</span>
                </a>
                <a class="nav-link" th:href="@{/roadmap}">
                    <i class="fa-solid fa-route text-info"></i>
                    <span>Learning Roadmap</span>
                </a>
                <a class="nav-link" th:href="@{/interview}">
                    <i class="fa-solid fa-user-tie text-warning"></i>
                    <span>Interview Practice</span>
                </a>
                <a class="nav-link ai-link" th:href="@{/ai-analyzer}">
                    <i class="fa-solid fa-wand-magic-sparkles text-info"></i>
                    <span>AI Career Coach</span>
                </a>
                <a class="nav-link" th:href="@{/builder}">
                    <i class="fa-solid fa-file-pen text-primary"></i>
                    <span>Resume Builder</span>
                </a>
            </nav>

            <div class="sidebar-nav-section">Part 4: Pursue</div>
            <nav class="sidebar-nav">
                <a class="nav-link" th:href="@{/tracker}">
                    <i class="fa-solid fa-list-check text-success"></i>
                    <span>Applications</span>
                </a>
                <a class="nav-link" th:href="@{/profile}">
                    <i class="fa-solid fa-user-gear text-secondary"></i>
                    <span>Profile &amp; Pathway</span>
                </a>
            </nav>"""

replacement = """            <div class="sidebar-nav-section">Explore</div>
            <nav class="sidebar-nav">
                <a class="nav-link" th:href="@{/explore}">
                    <i class="fa-solid fa-compass text-info"></i>
                    <span>Career Explorer</span>
                    <span class="badge bg-info bg-opacity-20 text-info nav-pill-badge">Roles</span>
                </a>
            </nav>

            <div class="sidebar-nav-section">Prepare</div>
            <nav class="sidebar-nav">
                <a class="nav-link" th:href="@{/analyzer}">
                    <i class="fa-solid fa-file-magnifying-glass text-success"></i>
                    <span>Resume &amp; ATS</span>
                </a>
                <a class="nav-link" th:href="@{/skill-gap}">
                    <i class="fa-solid fa-layer-group text-danger"></i>
                    <span>Skills &amp; Projects</span>
                </a>
                <a class="nav-link" th:href="@{/roadmap}">
                    <i class="fa-solid fa-route text-info"></i>
                    <span>Learning Roadmap</span>
                </a>
                <a class="nav-link" th:href="@{/interview}">
                    <i class="fa-solid fa-user-tie text-warning"></i>
                    <span>Interview Practice</span>
                </a>
                <a class="nav-link" th:href="@{/builder}">
                    <i class="fa-solid fa-file-pen text-primary"></i>
                    <span>Resume Builder</span>
                </a>
            </nav>

            <div class="sidebar-nav-section">Pursue</div>
            <nav class="sidebar-nav">
                <a class="nav-link" th:href="@{/jobs}">
                    <i class="fa-solid fa-briefcase text-warning"></i>
                    <span>Jobs &amp; Internships</span>
                </a>
                <a class="nav-link" th:href="@{/tracker}">
                    <i class="fa-solid fa-list-check text-success"></i>
                    <span>Applications</span>
                </a>
                <a class="nav-link" th:href="@{/profile}">
                    <i class="fa-solid fa-user-gear text-secondary"></i>
                    <span>Profile &amp; Settings</span>
                </a>
            </nav>"""

normalized_content = content.replace('\r\n', '\n')
if target in normalized_content:
    updated = normalized_content.replace(target, replacement).replace('\n', '\r\n')
    with open(path, 'w', encoding='utf-8') as f:
        f.write(updated)
    print('Updated base.html successfully')
else:
    print('Target not found in base.html')
