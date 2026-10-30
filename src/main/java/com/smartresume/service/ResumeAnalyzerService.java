package com.smartresume.service;

import com.smartresume.config.CourseConfig;
import com.smartresume.config.JobRolesConfig;
import com.smartresume.dto.ResumeAnalysisResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResumeAnalyzerService {

    private final Map<String, List<String>> documentTypes = Map.of(
            "resume", List.of("experience", "education", "skills", "work", "project", "objective",
                    "summary", "employment", "qualification", "achievements"),
            "marksheet", List.of("grade", "marks", "score", "semester", "cgpa", "sgpa", "examination",
                    "result", "academic year", "percentage"),
            "certificate", List.of("certificate", "certification", "awarded", "completed", "achievement",
                    "training", "course completion", "qualified"),
            "id_card", List.of("id card", "identity", "student id", "employee id", "valid until",
                    "date of issue", "identification"),
            "invoice", List.of("invoice", "receipt", "total amount", "tax", "subtotal", "bill to",
                    "due date", "payment terms", "invoice number", "unit price", "remit to")
    );

    public ResumeAnalysisResult analyze(String text, String targetRole) {
        ResumeAnalysisResult result = new ResumeAnalysisResult();
        result.setRawText(text);

        if (text == null || text.trim().length() < 20) {
            result.setError("Uploaded document contains no readable text or is empty.");
            result.setDocumentType("unknown");
            result.setAtsScore(0);
            return result;
        }

        // 1. Detect Document Type
        String docType = detectDocumentType(text);
        result.setDocumentType(docType);

        if (!"resume".equalsIgnoreCase(docType)) {
            result.setAtsScore(0);
            result.setSuggestions(List.of("This appears to be a " + docType + " document. Please upload a resume for ATS analysis."));
            return result;
        }

        // 2. Personal Info Extraction
        extractPersonalInfo(text, result);

        // 3. Keyword Matching against Target Role
        List<String> requiredSkills = new ArrayList<>();
        JobRolesConfig.RoleDefinition roleDef = null;
        if (targetRole != null && !targetRole.isBlank()) {
            roleDef = JobRolesConfig.getRoleByName(targetRole);
            if (roleDef != null) {
                requiredSkills.addAll(roleDef.getRequiredSkills());
            }
        }
        if (requiredSkills.isEmpty()) {
            // Default core skills if no role specified
            requiredSkills = List.of("Communication", "Problem-solving", "Team collaboration", "Git", "SQL");
        }

        calculateKeywordMatch(text, requiredSkills, result);

        // 4. Section Extractions
        extractSections(text, result);

        // 5. Formatting Check
        List<String> formatDeductions = new ArrayList<>();
        double formatScore = checkFormatting(text, formatDeductions);
        result.setFormatScore(formatScore);
        result.setFormatSuggestions(formatDeductions);

        // 6. Section Specific Suggestions & Scores
        List<String> contactSuggs = new ArrayList<>();
        if (result.getEmail() == null || result.getEmail().isBlank()) contactSuggs.add("Add your email address");
        if (result.getPhone() == null || result.getPhone().isBlank()) contactSuggs.add("Add your phone number");
        if (result.getLinkedin() == null || result.getLinkedin().isBlank()) contactSuggs.add("Add your LinkedIn profile URL");
        result.setContactSuggestions(contactSuggs);
        double contactScore = Math.max(0, 100 - (contactSuggs.size() * 25));
        result.setContactScore(contactScore);

        List<String> summarySuggs = new ArrayList<>();
        if (result.getSummary() == null || result.getSummary().isBlank()) {
            summarySuggs.add("Add a professional summary to highlight your key qualifications");
        } else {
            int words = result.getSummary().split("\\s+").length;
            if (words < 20) summarySuggs.add("Expand your professional summary to better highlight your experience and goals");
            else if (words > 100) summarySuggs.add("Consider making your summary more concise (aim for 50-75 words)");
        }
        result.setSummarySuggestions(summarySuggs);
        double summaryScore = Math.max(0, 100 - (summarySuggs.size() * 33));
        result.setSummaryScore(summaryScore);

        List<String> skillsSuggs = new ArrayList<>();
        if (result.getSkills().isEmpty()) skillsSuggs.add("Add a dedicated skills section");
        if (result.getSkills().size() < 5) skillsSuggs.add("List more relevant technical and soft skills");
        if (result.getKeywordMatchScore() < 70) skillsSuggs.add("Add more skills that match the target job requirements");
        result.setSkillsSuggestions(skillsSuggs);
        result.setSkillsScore(result.getKeywordMatchScore());

        List<String> expSuggs = new ArrayList<>();
        if (result.getExperience().isEmpty()) {
            expSuggs.add("Add your work experience or project history section");
        } else {
            boolean hasDates = result.getExperience().stream().anyMatch(e -> Pattern.compile("\\b(19|20)\\d{2}\\b").matcher(e).find());
            boolean hasBullets = result.getExperience().stream().anyMatch(e -> e.contains("•") || e.contains("-") || e.contains("*"));
            boolean hasActionVerbs = result.getExperience().stream().anyMatch(e ->
                    Pattern.compile("\\b(developed|managed|created|implemented|designed|led|improved|built|engineered)\\b", Pattern.CASE_INSENSITIVE).matcher(e).find());

            if (!hasDates) expSuggs.add("Include dates for each work experience");
            if (!hasBullets) expSuggs.add("Use bullet points to list your achievements and responsibilities");
            if (!hasActionVerbs) expSuggs.add("Start bullet points with strong action verbs");
        }
        result.setExperienceSuggestions(expSuggs);
        double expScore = Math.max(0, 100 - (expSuggs.size() * 25));
        result.setExperienceScore(expScore);

        List<String> eduSuggs = new ArrayList<>();
        if (result.getEducation().isEmpty()) {
            eduSuggs.add("Add your educational background");
        } else {
            boolean hasDates = result.getEducation().stream().anyMatch(e -> Pattern.compile("\\b(19|20)\\d{2}\\b").matcher(e).find());
            boolean hasDegree = result.getEducation().stream().anyMatch(e ->
                    Pattern.compile("\\b(bachelor|master|phd|b\\.|m\\.|b\\.tech|m\\.tech|bca|mca|degree|diploma)\\b", Pattern.CASE_INSENSITIVE).matcher(e).find());

            if (!hasDates) eduSuggs.add("Include graduation dates");
            if (!hasDegree) eduSuggs.add("Specify your degree type");
        }
        result.setEducationSuggestions(eduSuggs);
        double eduScore = Math.max(0, 100 - (eduSuggs.size() * 25));
        result.setEducationScore(eduScore);

        // 7. Overall Weighted ATS Score calculation (matching reference formula)
        double atsScore = Math.round(
                (contactScore * 0.1) +
                (summaryScore * 0.1) +
                (result.getKeywordMatchScore() * 0.3) +
                (expScore * 0.2) +
                (eduScore * 0.1) +
                (formatScore * 0.2)
        );
        result.setAtsScore(Math.min(100.0, Math.max(0.0, atsScore)));

        // Combine all suggestions
        List<String> allSuggs = new ArrayList<>();
        allSuggs.addAll(contactSuggs);
        allSuggs.addAll(summarySuggs);
        allSuggs.addAll(skillsSuggs);
        allSuggs.addAll(expSuggs);
        allSuggs.addAll(eduSuggs);
        allSuggs.addAll(formatDeductions);
        if (allSuggs.isEmpty()) {
            allSuggs.add("Your resume is well-optimized for ATS systems!");
        }
        result.setSuggestions(allSuggs);

        // 8. Attach Courses and Tutorials
        if (targetRole != null && !targetRole.isBlank()) {
            List<String[]> courses = CourseConfig.getCoursesForRole(targetRole);
            if (!courses.isEmpty()) {
                result.getRecommendedCourses().put(targetRole, courses);
            }
        }

        // Bridge candidate missing skills directly with targeted courses and learning videos
        if (result.getMissingSkills() != null) {
            int gapCount = 0;
            for (String missingSkill : result.getMissingSkills()) {
                if (gapCount >= 4) break;
                List<String[]> skillCourses = CourseConfig.getCoursesForSkill(missingSkill);
                if (!skillCourses.isEmpty()) {
                    result.getRecommendedCourses().put(missingSkill + " (Skill Gap)", skillCourses);
                }
                String[] videoRec = CourseConfig.getVideoRecommendation(missingSkill);
                if (videoRec != null) {
                    result.getInterviewVideos().computeIfAbsent("Targeted Skill Gap: " + missingSkill, k -> new ArrayList<>())
                            .add(videoRec);
                }
                gapCount++;
            }
        }

        result.setResumeVideos(CourseConfig.getResumeVideos());
        CourseConfig.getInterviewVideos().forEach((k, v) ->
                result.getInterviewVideos().computeIfAbsent(k, key -> new ArrayList<>()).addAll(v));

        return result;
    }

    private String detectDocumentType(String text) {
        String lower = text.toLowerCase();
        Map<String, Double> scores = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : documentTypes.entrySet()) {
            long matches = entry.getValue().stream().filter(lower::contains).count();
            double density = (double) matches / entry.getValue().size();
            double frequency = (double) matches / (text.split("\\s+").length + 1);
            scores.put(entry.getKey(), (density * 0.7) + (frequency * 0.3));
        }

        Map.Entry<String, Double> best = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (best != null && best.getValue() > 0.12) {
            return best.getKey();
        }
        return "resume"; // Default to resume if ambiguous
    }

    private void calculateKeywordMatch(String text, List<String> requiredSkills, ResumeAnalysisResult result) {
        String lower = text.toLowerCase();
        List<String> found = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String skill : requiredSkills) {
            String skillLower = skill.toLowerCase();
            boolean matched;
            if (skillLower.matches("^[a-z0-9_]+$")) {
                // Word-boundary check for single-token keywords (e.g. "java" won't falsely match "javascript")
                Pattern p = Pattern.compile("\\b" + Pattern.quote(skillLower) + "\\b", Pattern.CASE_INSENSITIVE);
                matched = p.matcher(text).find();
            } else {
                // Multi-word or symbol keywords (e.g. "Spring Boot", "C++", "REST APIs")
                matched = lower.contains(skillLower);
            }

            if (matched) {
                found.add(skill);
            } else {
                missing.add(skill);
            }
        }

        double score = requiredSkills.isEmpty() ? 0 : ((double) found.size() / requiredSkills.size()) * 100.0;
        result.setKeywordMatchScore(Math.round(score * 10.0) / 10.0);
        result.setFoundSkills(found);
        result.setMissingSkills(missing);
    }

    private double checkFormatting(String text, List<String> deductions) {
        String[] lines = text.split("\n");
        double score = 100.0;

        if (text.length() < 300) {
            score -= 30;
            deductions.add("Resume is too short (under 300 characters)");
        }

        boolean hasUpperHeader = Arrays.stream(lines)
                .map(String::trim)
                .filter(l -> l.length() > 3 && l.length() < 30)
                .anyMatch(l -> l.equals(l.toUpperCase()) && !Pattern.compile("\\d").matcher(l).find());
        if (!hasUpperHeader) {
            score -= 20;
            deductions.add("No clear uppercase section headers found");
        }

        boolean hasBullets = Arrays.stream(lines)
                .map(String::trim)
                .anyMatch(l -> l.startsWith("•") || l.startsWith("-") || l.startsWith("*") || l.startsWith("→"));
        if (!hasBullets) {
            score -= 20;
            deductions.add("No bullet points found for listing achievements/responsibilities");
        }

        Pattern emailPattern = Pattern.compile("[\\w\\.-]+@[\\w\\.-]+\\.\\w+");
        Pattern phonePattern = Pattern.compile("(?:\\+?\\d{1,4}[-.\\s]?)?(?:\\(?\\d{2,5}\\)?[-.\\s]?)?\\d{3,5}[-.\\s]?\\d{3,5}\\b");
        boolean hasContact = emailPattern.matcher(text).find() || phonePattern.matcher(text).find();
        if (!hasContact) {
            score -= 15;
            deductions.add("Missing or improperly formatted contact information");
        }

        return Math.max(0.0, score);
    }

    private void extractPersonalInfo(String text, ResumeAnalysisResult result) {
        Matcher emailMatcher = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}").matcher(text);
        if (emailMatcher.find()) result.setEmail(emailMatcher.group());

        Pattern phonePattern = Pattern.compile("(?:\\+?\\d{1,4}[-.\\s]?)?(?:\\(?\\d{2,5}\\)?[-.\\s]?)?\\d{3,5}[-.\\s]?\\d{3,5}\\b");
        Matcher phoneMatcher = phonePattern.matcher(text);
        while (phoneMatcher.find()) {
            String candidate = phoneMatcher.group().trim();
            long digitCount = candidate.chars().filter(Character::isDigit).count();
            if (digitCount >= 7 && digitCount <= 15) {
                result.setPhone(candidate);
                break;
            }
        }

        Matcher linkedinMatcher = Pattern.compile("(https?://)?(www\\.)?linkedin\\.com/in/[a-zA-Z0-9_-]+").matcher(text);
        if (linkedinMatcher.find()) result.setLinkedin(linkedinMatcher.group());

        Matcher githubMatcher = Pattern.compile("(https?://)?(www\\.)?github\\.com/[a-zA-Z0-9_-]+").matcher(text);
        if (githubMatcher.find()) result.setGithub(githubMatcher.group());

        String[] lines = text.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isBlank() && trimmed.length() < 40 && !trimmed.contains("@") && !trimmed.contains("http")) {
                result.setName(trimmed);
                break;
            }
        }
        if (result.getName() == null || result.getName().isBlank()) {
            result.setName("Candidate");
        }
    }

    private void extractSections(String text, ResumeAnalysisResult result) {
        String[] lines = text.split("\n");
        List<String> skills = new ArrayList<>();
        List<String> education = new ArrayList<>();
        List<String> experience = new ArrayList<>();
        List<String> projects = new ArrayList<>();
        StringBuilder summary = new StringBuilder();

        String currentSection = null;

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isBlank()) continue;

            String lower = trimmed.toLowerCase();
            if (lower.contains("summary") || lower.contains("objective") || lower.contains("profile")) {
                currentSection = "summary";
                continue;
            } else if (lower.contains("skills") || lower.contains("technologies") || lower.contains("technical competencies")) {
                currentSection = "skills";
                continue;
            } else if (lower.contains("experience") || lower.contains("employment") || lower.contains("work history")) {
                currentSection = "experience";
                continue;
            } else if (lower.contains("education") || lower.contains("academic") || lower.contains("qualifications")) {
                currentSection = "education";
                continue;
            } else if (lower.contains("projects") || lower.contains("academic projects")) {
                currentSection = "projects";
                continue;
            }

            if ("summary".equals(currentSection)) {
                if (summary.length() < 300) summary.append(trimmed).append(" ");
            } else if ("skills".equals(currentSection)) {
                String[] parts = trimmed.split("[,•|/]");
                for (String p : parts) {
                    String s = p.trim();
                    if (!s.isBlank() && s.length() < 30) skills.add(s);
                }
            } else if ("experience".equals(currentSection)) {
                experience.add(trimmed);
            } else if ("education".equals(currentSection)) {
                education.add(trimmed);
            } else if ("projects".equals(currentSection)) {
                projects.add(trimmed);
            }
        }

        result.setSummary(summary.toString().trim());
        result.setSkills(skills);
        result.setEducation(education);
        result.setExperience(experience);
        result.setProjects(projects);
    }
}
