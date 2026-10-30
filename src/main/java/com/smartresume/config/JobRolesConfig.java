package com.smartresume.config;

import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class JobRolesConfig {

    public static class RoleDefinition {
        private String roleName;
        private String category;
        private String description;
        private List<String> requiredSkills;
        private List<String> sections;
        private List<String> recommendedTechnicalSkills;
        private List<String> recommendedSoftSkills;

        public RoleDefinition(String roleName, String category, String description,
                              List<String> requiredSkills, List<String> sections,
                              List<String> recommendedTechnicalSkills, List<String> recommendedSoftSkills) {
            this.roleName = roleName;
            this.category = category;
            this.description = description;
            this.requiredSkills = requiredSkills;
            this.sections = sections;
            this.recommendedTechnicalSkills = recommendedTechnicalSkills;
            this.recommendedSoftSkills = recommendedSoftSkills;
        }

        public String getRoleName() { return roleName; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public List<String> getSections() { return sections; }
        public List<String> getRecommendedTechnicalSkills() { return recommendedTechnicalSkills; }
        public List<String> getRecommendedSoftSkills() { return recommendedSoftSkills; }
    }

    private static final Map<String, Map<String, RoleDefinition>> JOB_ROLES = new LinkedHashMap<>();
    private static final Map<String, RoleDefinition> ROLES_BY_NAME = new LinkedHashMap<>();

    static {
        // 1. Software Development and Engineering
        Map<String, RoleDefinition> softwareRoles = new LinkedHashMap<>();
        addRole(softwareRoles, new RoleDefinition(
                "Frontend Developer", "Software Development and Engineering",
                "Create user interfaces and implement visual elements",
                List.of("HTML", "CSS", "JavaScript", "React", "Angular", "Vue.js", "UI/UX", "Responsive Design"),
                List.of("Technical Skills", "Projects", "Work Experience", "Education"),
                List.of("HTML5", "CSS3", "JavaScript", "React/Angular/Vue", "TypeScript", "Git"),
                List.of("Communication", "Problem-solving", "Attention to detail", "Creativity")
        ));
        addRole(softwareRoles, new RoleDefinition(
                "Backend Developer", "Software Development and Engineering",
                "Build server-side logic and databases",
                List.of("Python", "Java", "Node.js", "SQL", "APIs", "Django", "Flask", "Database Design"),
                List.of("Technical Skills", "System Architecture", "Work Experience", "Education"),
                List.of("Python/Java/Node.js", "SQL", "RESTful APIs", "Microservices", "Docker"),
                List.of("Analytical thinking", "Problem-solving", "Team collaboration")
        ));
        addRole(softwareRoles, new RoleDefinition(
                "Full Stack Developer", "Software Development and Engineering",
                "Handle both client and server-side development",
                List.of("Frontend Tech", "Backend Tech", "Databases", "DevOps", "System Design", "APIs"),
                List.of("Technical Skills", "Full Stack Projects", "Work Experience", "Education"),
                List.of("Frontend & Backend technologies", "Database design", "API development", "DevOps"),
                List.of("Versatility", "Project management", "Communication")
        ));
        addRole(softwareRoles, new RoleDefinition(
                "Mobile App Developer", "Software Development and Engineering",
                "Develop mobile applications for iOS and Android platforms",
                List.of("Swift", "Kotlin", "React Native", "Flutter", "Mobile UI/UX", "App Store Deployment"),
                List.of("Technical Skills", "Mobile Projects", "Work Experience", "Education"),
                List.of("iOS/Android Development", "Cross-platform frameworks", "Mobile UI/UX", "App Performance"),
                List.of("User-centric thinking", "Problem-solving", "Attention to detail")
        ));
        addRole(softwareRoles, new RoleDefinition(
                "Game Developer", "Software Development and Engineering",
                "Create engaging and interactive games",
                List.of("Unity", "Unreal Engine", "C++", "C#", "3D Graphics", "Game Physics"),
                List.of("Technical Skills", "Game Projects", "Work Experience", "Education"),
                List.of("Game Engines", "Graphics Programming", "Physics Simulation", "Multiplayer"),
                List.of("Creativity", "Problem-solving", "Team collaboration")
        ));
        JOB_ROLES.put("Software Development and Engineering", softwareRoles);

        // 2. Data Science and Analytics
        Map<String, RoleDefinition> dataRoles = new LinkedHashMap<>();
        addRole(dataRoles, new RoleDefinition(
                "Data Scientist", "Data Science and Analytics",
                "Analyze complex data sets to find patterns",
                List.of("Python", "R", "Machine Learning", "Statistics", "SQL", "Deep Learning"),
                List.of("Technical Skills", "Projects", "Research", "Education"),
                List.of("Python", "R", "Machine Learning", "Statistical Analysis", "Big Data"),
                List.of("Analytical thinking", "Research", "Problem-solving")
        ));
        addRole(dataRoles, new RoleDefinition(
                "Data Analyst", "Data Science and Analytics",
                "Transform data into insights",
                List.of("SQL", "Excel", "Python", "Data Visualization", "Statistics"),
                List.of("Technical Skills", "Analysis Projects", "Work Experience", "Education"),
                List.of("SQL", "Excel", "Python", "Tableau/Power BI", "Statistical Analysis"),
                List.of("Data interpretation", "Communication", "Attention to detail")
        ));
        addRole(dataRoles, new RoleDefinition(
                "Machine Learning Engineer", "Data Science and Analytics",
                "Build and deploy machine learning models",
                List.of("Python", "TensorFlow", "PyTorch", "MLOps", "Deep Learning"),
                List.of("Technical Skills", "ML Projects", "Work Experience", "Education"),
                List.of("Machine Learning", "Deep Learning", "MLOps", "Model Deployment"),
                List.of("Research", "Problem-solving", "Critical thinking")
        ));
        JOB_ROLES.put("Data Science and Analytics", dataRoles);

        // 3. Cloud Computing and DevOps
        Map<String, RoleDefinition> cloudRoles = new LinkedHashMap<>();
        addRole(cloudRoles, new RoleDefinition(
                "Cloud Architect", "Cloud Computing and DevOps",
                "Design and manage cloud infrastructure",
                List.of("AWS", "Azure", "GCP", "Infrastructure as Code", "Security"),
                List.of("Technical Skills", "Cloud Projects", "Work Experience", "Certifications"),
                List.of("Cloud Platforms", "Security", "Networking", "Cost Optimization"),
                List.of("Strategic thinking", "Problem-solving", "Communication")
        ));
        addRole(cloudRoles, new RoleDefinition(
                "DevOps Engineer", "Cloud Computing and DevOps",
                "Implement DevOps practices and tools",
                List.of("Docker", "Kubernetes", "CI/CD", "Automation", "Monitoring"),
                List.of("Technical Skills", "DevOps Projects", "Work Experience", "Education"),
                List.of("Containerization", "Orchestration", "CI/CD", "Infrastructure as Code"),
                List.of("Automation mindset", "Problem-solving", "Team collaboration")
        ));
        addRole(cloudRoles, new RoleDefinition(
                "Site Reliability Engineer", "Cloud Computing and DevOps",
                "Ensure system reliability and performance",
                List.of("Linux", "Monitoring", "Automation", "Performance Tuning", "Incident Response"),
                List.of("Technical Skills", "SRE Projects", "Work Experience", "Education"),
                List.of("System Administration", "Monitoring", "Automation", "Incident Management"),
                List.of("Problem-solving", "Communication", "Critical thinking")
        ));
        JOB_ROLES.put("Cloud Computing and DevOps", cloudRoles);

        // 4. Cybersecurity
        Map<String, RoleDefinition> cyberRoles = new LinkedHashMap<>();
        addRole(cyberRoles, new RoleDefinition(
                "Security Analyst", "Cybersecurity",
                "Monitor and protect against security threats",
                List.of("Network Security", "Threat Detection", "Security Tools", "Incident Response"),
                List.of("Technical Skills", "Security Projects", "Work Experience", "Certifications"),
                List.of("Security Tools", "Threat Analysis", "Incident Response", "Compliance"),
                List.of("Analytical thinking", "Attention to detail", "Communication")
        ));
        addRole(cyberRoles, new RoleDefinition(
                "Penetration Tester", "Cybersecurity",
                "Test systems for security vulnerabilities",
                List.of("Ethical Hacking", "Security Tools", "Network Security", "Web Security"),
                List.of("Technical Skills", "Security Projects", "Work Experience", "Certifications"),
                List.of("Penetration Testing", "Security Tools", "Vulnerability Assessment"),
                List.of("Ethical mindset", "Problem-solving", "Report writing")
        ));
        JOB_ROLES.put("Cybersecurity", cyberRoles);

        // 5. UI/UX Design
        Map<String, RoleDefinition> designRoles = new LinkedHashMap<>();
        addRole(designRoles, new RoleDefinition(
                "UI Designer", "UI/UX Design",
                "Create beautiful user interfaces",
                List.of("Figma", "Adobe XD", "Visual Design", "Typography", "Color Theory"),
                List.of("Design Skills", "Portfolio", "Work Experience", "Education"),
                List.of("Design Tools", "Visual Design", "Prototyping", "Design Systems"),
                List.of("Creativity", "Attention to detail", "User empathy")
        ));
        addRole(designRoles, new RoleDefinition(
                "UX Designer", "UI/UX Design",
                "Design user experiences and flows",
                List.of("User Research", "Wireframing", "Prototyping", "Usability Testing"),
                List.of("Design Skills", "Case Studies", "Work Experience", "Education"),
                List.of("Research Methods", "Information Architecture", "User Testing"),
                List.of("Empathy", "Communication", "Problem-solving")
        ));
        JOB_ROLES.put("UI/UX Design", designRoles);

        // 6. Project Management
        Map<String, RoleDefinition> pmRoles = new LinkedHashMap<>();
        addRole(pmRoles, new RoleDefinition(
                "Project Manager", "Project Management",
                "Lead and manage project delivery",
                List.of("Project Planning", "Agile", "Scrum", "Risk Management", "Stakeholder Management"),
                List.of("Management Skills", "Project History", "Work Experience", "Certifications"),
                List.of("Project Management Tools", "Agile Methodologies", "Budgeting"),
                List.of("Leadership", "Communication", "Problem-solving")
        ));
        addRole(pmRoles, new RoleDefinition(
                "Product Manager", "Project Management",
                "Define and drive product vision",
                List.of("Product Strategy", "Market Research", "User Stories", "Roadmapping"),
                List.of("Product Skills", "Product Launches", "Work Experience", "Education"),
                List.of("Product Management Tools", "Analytics", "Market Research"),
                List.of("Strategic thinking", "Communication", "Leadership")
        ));
        JOB_ROLES.put("Project Management", pmRoles);
    }

    private static void addRole(Map<String, RoleDefinition> categoryMap, RoleDefinition role) {
        categoryMap.put(role.getRoleName(), role);
        ROLES_BY_NAME.put(role.getRoleName(), role);
    }

    public static Map<String, Map<String, RoleDefinition>> getJobRoles() {
        return JOB_ROLES;
    }

    public static RoleDefinition getRoleByName(String roleName) {
        return ROLES_BY_NAME.get(roleName);
    }

    public static List<String> getAllRoleNames() {
        return new ArrayList<>(ROLES_BY_NAME.keySet());
    }

    public static Set<String> getCategories() {
        return JOB_ROLES.keySet();
    }

    public static List<String> getRolesForCategory(String category) {
        Map<String, RoleDefinition> roles = JOB_ROLES.get(category);
        if (roles != null) {
            return new ArrayList<>(roles.keySet());
        }
        return Collections.emptyList();
    }
}
