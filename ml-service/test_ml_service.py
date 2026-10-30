"""
Unit tests for Python ML/NLP Microservice
Tests skill extraction, semantic scoring, role classification, and embeddings.
"""

from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_health_check():
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "UP"
    assert data["service"] == "Smart AI Resume ML Service"

def test_extract_entities():
    payload = {
        "resumeText": "Experienced Software Engineer with 4 years in Java, Spring Boot, MySQL, and Docker (2020 - 2024)."
    }
    response = client.post("/api/v1/ml/extract-entities", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "Java" in data["skills"]
    assert "Spring Boot" in data["skills"]
    assert data["experienceYears"] >= 4.0

def test_semantic_score():
    payload = {
        "candidateSkills": ["Java", "Spring Boot", "MySQL", "Git"],
        "jobRequirements": ["Java", "Spring Boot", "Docker", "Kubernetes"]
    }
    response = client.post("/api/v1/ml/semantic-score", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["semanticScore"] == 50.0
    assert "Docker" in data["missingConcepts"]

def test_classify_role():
    payload = {
        "resumeText": "Built microservices using Spring Boot, Java, and REST APIs with JUnit tests."
    }
    response = client.post("/api/v1/ml/classify-role", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["predictedRole"] == "Software Engineer"
    assert data["confidence"] > 0.5

def test_embeddings():
    payload = {
        "texts": ["Proficient in distributed cloud architecture."]
    }
    response = client.post("/api/v1/ml/embed", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["dimensions"] == 384
    assert len(data["embeddings"]) == 1
    assert len(data["embeddings"][0]) == 384
