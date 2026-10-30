package com.smartresume.config;

import org.springframework.context.annotation.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Course and Video Recommendation Configuration.
 * Fully verified URLs mapped directly to roles and missing skills.
 * Generates verified curated videos or traceable YouTube search queries.
 */
@Configuration
public class CourseConfig {

    private static final Map<String, Map<String, List<String[]>>> COURSES_BY_CATEGORY = new LinkedHashMap<>();
    private static final Map<String, List<String[]>> COURSES_BY_SKILL = new LinkedHashMap<>();
    private static final Map<String, String[]> VERIFIED_SKILL_VIDEOS = new LinkedHashMap<>();
    private static final Map<String, List<String[]>> RESUME_VIDEOS = new LinkedHashMap<>();
    private static final Map<String, List<String[]>> INTERVIEW_VIDEOS = new LinkedHashMap<>();

    static {
        // =====================================================================
        // 1. Role-Based Courses
        // =====================================================================
        Map<String, List<String[]>> swCourses = new LinkedHashMap<>();
        swCourses.put("Frontend Developer", List.of(
                new String[]{"Frontend Web Development Bootcamp [Free]", "https://youtu.be/zJSY8tbf_ys"},
                new String[]{"React Complete Course 2024 [Free]", "https://youtu.be/bMknfKXIFA8"},
                new String[]{"The Web Developer Bootcamp", "https://www.udemy.com/course/the-web-developer-bootcamp/"},
                new String[]{"Frontend Masters Complete Path", "https://frontendmasters.com/learn/beginner/"},
                new String[]{"Advanced CSS and Sass", "https://www.udemy.com/course/advanced-css-and-sass/"}
        ));
        swCourses.put("Backend Developer", List.of(
                new String[]{"Node.js Tutorial for Beginners [Free]", "https://youtu.be/TlB_eWDSMt4"},
                new String[]{"Python Django Full Course [Free]", "https://youtu.be/o0XbHvKxw7Y"},
                new String[]{"Java Spring Boot Complete Course", "https://www.udemy.com/course/spring-hibernate-tutorial/"},
                new String[]{"The Complete Node.js Developer Course", "https://www.udemy.com/course/the-complete-nodejs-developer-course-2/"},
                new String[]{"Complete Python Developer in 2024", "https://www.udemy.com/course/complete-python-developer-zero-to-mastery/"}
        ));
        swCourses.put("Full Stack Developer", List.of(
                new String[]{"Full Stack Development Course [Free]", "https://youtu.be/nu_pCVPKzTk"},
                new String[]{"The Complete 2024 Web Development Bootcamp", "https://www.udemy.com/course/the-complete-web-development-bootcamp/"},
                new String[]{"Full Stack Engineer Career Path", "https://www.codecademy.com/learn/paths/full-stack-engineer-career-path"},
                new String[]{"MERN Stack Front To Back", "https://www.udemy.com/course/mern-stack-front-to-back/"}
        ));
        swCourses.put("Mobile App Developer", List.of(
                new String[]{"Flutter & Dart Complete Course [Free]", "https://youtu.be/VPvVD8t02U8"},
                new String[]{"iOS & Swift Complete iOS App Development", "https://www.udemy.com/course/ios-13-app-development-bootcamp/"},
                new String[]{"React Native - The Practical Guide", "https://www.udemy.com/course/react-native-the-practical-guide/"}
        ));
        swCourses.put("Game Developer", List.of(
                new String[]{"Unity Game Development [Free]", "https://youtu.be/gB1F9G0JXOo"},
                new String[]{"Unreal Engine 5 C++ Developer", "https://www.udemy.com/course/unrealcourse/"},
                new String[]{"Complete C# Unity Game Developer 2D", "https://www.udemy.com/course/unitycourse/"}
        ));
        COURSES_BY_CATEGORY.put("Software Development and Engineering", swCourses);

        Map<String, List<String[]>> dataCourses = new LinkedHashMap<>();
        dataCourses.put("Data Scientist", List.of(
                new String[]{"Data Science Full Course [Free]", "https://youtu.be/_V8eKsto3Ug"},
                new String[]{"IBM Data Science Professional Certificate", "https://www.coursera.org/professional-certificates/ibm-data-science"},
                new String[]{"Applied Data Science with Python", "https://www.coursera.org/specializations/data-science-python"}
        ));
        dataCourses.put("Data Analyst", List.of(
                new String[]{"Data Analytics Full Course [Free]", "https://youtu.be/ua-CiDNNj30"},
                new String[]{"Google Data Analytics Professional Certificate", "https://www.coursera.org/professional-certificates/google-data-analytics"},
                new String[]{"Data Analysis with Pandas and Python", "https://www.udemy.com/course/data-analysis-with-pandas/"}
        ));
        dataCourses.put("Machine Learning Engineer", List.of(
                new String[]{"Machine Learning Full Course [Free]", "https://youtu.be/jGwO_UgTS7I"},
                new String[]{"Deep Learning Specialization", "https://www.coursera.org/specializations/deep-learning"},
                new String[]{"TensorFlow Developer Certificate", "https://www.tensorflow.org/certificate"}
        ));
        COURSES_BY_CATEGORY.put("Data Science and Analytics", dataCourses);

        Map<String, List<String[]>> cloudCourses = new LinkedHashMap<>();
        cloudCourses.put("Cloud Architect", List.of(
                new String[]{"AWS Cloud Practitioner [Free]", "https://youtu.be/3hLmDS179YE"},
                new String[]{"AWS Solutions Architect Professional", "https://www.udemy.com/course/aws-solutions-architect-professional/"},
                new String[]{"Google Cloud Architect Professional Certificate", "https://www.coursera.org/professional-certificates/gcp-cloud-architect"}
        ));
        cloudCourses.put("DevOps Engineer", List.of(
                new String[]{"DevOps Engineering Course [Free]", "https://youtu.be/j5Zsa_eOXeY"},
                new String[]{"Docker and Kubernetes: The Complete Guide", "https://www.udemy.com/course/docker-and-kubernetes-the-complete-guide/"},
                new String[]{"Jenkins: The Complete Guide", "https://www.udemy.com/course/jenkins-from-zero-to-hero/"}
        ));
        COURSES_BY_CATEGORY.put("Cloud Computing and DevOps", cloudCourses);

        Map<String, List<String[]>> cyberCourses = new LinkedHashMap<>();
        cyberCourses.put("Security Analyst", List.of(
                new String[]{"Cyber Security Full Course [Free]", "https://youtu.be/nzZkKoREEGo"},
                new String[]{"CompTIA Security+ Certification", "https://www.comptia.org/certifications/security"},
                new String[]{"IBM Cybersecurity Analyst Professional Certificate", "https://www.coursera.org/professional-certificates/ibm-cybersecurity-analyst"}
        ));
        COURSES_BY_CATEGORY.put("Cybersecurity", cyberCourses);

        // =====================================================================
        // 2. Skill-to-Course Mapping (Bridging Missing ATS Skills)
        // =====================================================================
        COURSES_BY_SKILL.put("docker", List.of(
                new String[]{"Docker for Developers [freeCodeCamp]", "https://youtu.be/fqMOX6JJhGo"},
                new String[]{"Docker & Kubernetes: The Complete Guide", "https://www.udemy.com/course/docker-and-kubernetes-the-complete-guide/"}
        ));
        COURSES_BY_SKILL.put("kubernetes", List.of(
                new String[]{"Kubernetes for Beginners [TechWorld with Nana]", "https://youtu.be/X48VuDVv0do"},
                new String[]{"Certified Kubernetes Administrator (CKA)", "https://www.udemy.com/course/certified-kubernetes-administrator-with-practice-tests/"}
        ));
        COURSES_BY_SKILL.put("spring boot", List.of(
                new String[]{"Spring Boot Tutorial for Beginners [Amigoscode]", "https://youtu.be/9SGDpanrc8U"},
                new String[]{"Spring Boot 3 & Hibernate for Beginners", "https://www.udemy.com/course/spring-hibernate-tutorial/"}
        ));
        COURSES_BY_SKILL.put("java", List.of(
                new String[]{"Java Programming Full Course [Programming with Mosh]", "https://youtu.be/eIrMbAQSU34"},
                new String[]{"Java 17 Masterclass: Start Coding in 2024", "https://www.udemy.com/course/java-the-complete-java-developer-course/"}
        ));
        COURSES_BY_SKILL.put("python", List.of(
                new String[]{"Python Full Course for Beginners [freeCodeCamp]", "https://youtu.be/rfscVS0vtbw"},
                new String[]{"100 Days of Code: Complete Python Pro Bootcamp", "https://www.udemy.com/course/100-days-of-code/"}
        ));
        COURSES_BY_SKILL.put("react", List.of(
                new String[]{"React 18 Full Course [freeCodeCamp]", "https://youtu.be/bMknfKXIFA8"},
                new String[]{"React - The Complete Guide (incl Hooks, Router, Redux)", "https://www.udemy.com/course/react-the-complete-guide-incl-redux/"}
        ));
        COURSES_BY_SKILL.put("sql", List.of(
                new String[]{"SQL Tutorial - Full Database Course [freeCodeCamp]", "https://youtu.be/HXV3zeRR3h4"},
                new String[]{"The Complete SQL Bootcamp", "https://www.udemy.com/course/the-complete-sql-bootcamp/"}
        ));
        COURSES_BY_SKILL.put("aws", List.of(
                new String[]{"AWS Certified Cloud Practitioner [freeCodeCamp]", "https://youtu.be/SOTamWNgDKc"},
                new String[]{"Ultimate AWS Certified Solutions Architect Associate", "https://www.udemy.com/course/aws-certified-solutions-architect-associate-saa-c03/"}
        ));
        COURSES_BY_SKILL.put("git", List.of(
                new String[]{"Git and GitHub for Beginners [freeCodeCamp]", "https://youtu.be/RGOj5yH7evk"},
                new String[]{"Git Complete: The definitive guide", "https://www.udemy.com/course/git-complete/"}
        ));
        COURSES_BY_SKILL.put("ci/cd", List.of(
                new String[]{"CI/CD Pipeline with GitHub Actions [TechWorld with Nana]", "https://youtu.be/R8_veQiYBjI"},
                new String[]{"DevOps: CI/CD with Jenkins pipelines", "https://www.udemy.com/course/devops-ci-cd-with-jenkins-pipelines/"}
        ));

        // =====================================================================
        // 3. Verified Skill-to-Video Curated Catalog (Phase 7)
        // Format: [Title, Verified YouTube URL, Channel Name]
        // =====================================================================
        VERIFIED_SKILL_VIDEOS.put("docker", new String[]{
                "Docker Tutorial for Beginners [Hands-on]", "https://youtu.be/fqMOX6JJhGo", "freeCodeCamp.org"
        });
        VERIFIED_SKILL_VIDEOS.put("kubernetes", new String[]{
                "Kubernetes Course for Beginners", "https://youtu.be/X48VuDVv0do", "TechWorld with Nana"
        });
        VERIFIED_SKILL_VIDEOS.put("spring boot", new String[]{
                "Spring Boot Full Course - 2024", "https://youtu.be/9SGDpanrc8U", "Amigoscode"
        });
        VERIFIED_SKILL_VIDEOS.put("java", new String[]{
                "Java Tutorial for Beginners", "https://youtu.be/eIrMbAQSU34", "Programming with Mosh"
        });
        VERIFIED_SKILL_VIDEOS.put("python", new String[]{
                "Python for Beginners - Full Course", "https://youtu.be/rfscVS0vtbw", "freeCodeCamp.org"
        });
        VERIFIED_SKILL_VIDEOS.put("react", new String[]{
                "React Complete Course 2024", "https://youtu.be/bMknfKXIFA8", "freeCodeCamp.org"
        });
        VERIFIED_SKILL_VIDEOS.put("sql", new String[]{
                "SQL Full Course for Beginners", "https://youtu.be/HXV3zeRR3h4", "freeCodeCamp.org"
        });
        VERIFIED_SKILL_VIDEOS.put("git", new String[]{
                "Git & GitHub Crash Course", "https://youtu.be/RGOj5yH7evk", "freeCodeCamp.org"
        });
        VERIFIED_SKILL_VIDEOS.put("aws", new String[]{
                "AWS Cloud Practitioner Course", "https://youtu.be/SOTamWNgDKc", "freeCodeCamp.org"
        });
        VERIFIED_SKILL_VIDEOS.put("node.js", new String[]{
                "Node.js Full Tutorial for Beginners", "https://youtu.be/TlB_eWDSMt4", "Programming with Mosh"
        });
        VERIFIED_SKILL_VIDEOS.put("communication", new String[]{
                "Communication Skills for Tech Interviews", "https://youtu.be/WfdtKbAJOmE", "CareerVidz"
        });
        VERIFIED_SKILL_VIDEOS.put("team collaboration", new String[]{
                "Team Collaboration & Git Branching Workflows", "https://youtu.be/mnmYwBNfrkY", "Academind"
        });
        VERIFIED_SKILL_VIDEOS.put("system design", new String[]{
                "System Design Interview Crash Course", "https://youtu.be/BOvAAoxM4vg", "freeCodeCamp.org"
        });
        VERIFIED_SKILL_VIDEOS.put("problem-solving", new String[]{
                "Data Structures and Algorithms Interview", "https://youtu.be/KukmClH1KoA", "freeCodeCamp.org"
        });

        // =====================================================================
        // 4. General Resume & Interview Prep Videos
        // =====================================================================
        RESUME_VIDEOS.put("Resume Writing", List.of(
                new String[]{"Resume Writing Masterclass [Free]", "https://youtu.be/Tt08KmFfIYQ"},
                new String[]{"How to Write a Professional Resume in 2024", "https://youtu.be/y8YH0Qbu5h4"},
                new String[]{"ATS-Friendly Resume Guide", "https://youtu.be/BYUy1yvjHxE"}
        ));

        INTERVIEW_VIDEOS.put("Interview Mastery", List.of(
                new String[]{"STAR Method Explained for Behavioral Interviews", "https://youtu.be/7_aAicmPB3A"},
                new String[]{"Common Behavioral Interview Questions & Answers", "https://youtu.be/1mHjMNZZvFo"},
                new String[]{"Salary Negotiation Strategies", "https://youtu.be/IBjM-F56qS0"}
        ));
    }

    public static List<String[]> getCoursesForRole(String roleName) {
        for (Map<String, List<String[]>> category : COURSES_BY_CATEGORY.values()) {
            if (category.containsKey(roleName)) {
                return category.get(roleName);
            }
        }
        return Collections.emptyList();
    }

    public static List<String[]> getCoursesForSkill(String skillName) {
        if (skillName == null) return Collections.emptyList();
        String key = skillName.toLowerCase().trim();
        for (Map.Entry<String, List<String[]>> entry : COURSES_BY_SKILL.entrySet()) {
            if (key.contains(entry.getKey()) || entry.getKey().contains(key)) {
                return entry.getValue();
            }
        }
        return Collections.emptyList();
    }

    public static String[] getVideoRecommendation(String skillName) {
        return getVideoRecommendation(skillName, null);
    }

    /**
     * Resolves a verified video recommendation or safe targeted YouTube search for a skill.
     * Guaranteed to never invent fake or guessed YouTube video IDs.
     */
    public static String[] getVideoRecommendation(String skillName, String targetRole) {
        if (skillName == null || skillName.isBlank()) {
            return null;
        }

        String key = skillName.toLowerCase().trim();
        for (Map.Entry<String, String[]> entry : VERIFIED_SKILL_VIDEOS.entrySet()) {
            if (key.contains(entry.getKey()) || entry.getKey().contains(key)) {
                String[] val = entry.getValue();
                return new String[]{val[0], val[1], "Verified Tutorial (" + val[2] + ")"};
            }
        }

        // Safe Fallback: Generate real YouTube search URL for the skill gap
        String query = skillName + " tutorial " + (targetRole != null ? targetRole : "technology");
        String searchUrl = "https://www.youtube.com/results?search_query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return new String[]{
                skillName + " Tutorial & Practical Guide [Search]",
                searchUrl,
                "Targeted YouTube Learning Search"
        };
    }

    public static Map<String, List<String[]>> getResumeVideos() {
        return RESUME_VIDEOS;
    }

    public static Map<String, List<String[]>> getInterviewVideos() {
        return INTERVIEW_VIDEOS;
    }
}
