INSERT INTO articles (title, text, author, published_at) VALUES
('How to Master Java in 30 Days', 'Java remains one of the most in-demand programming languages. In this article, we will break down a step-by-step plan for learning Java from scratch to a confident level. You will learn about key OOP concepts, working with collections, multithreading, and modern frameworks.', 'Ivan Petrov', '2025-01-15 10:00:00'),
('Spring Boot Basics for Beginners', 'Spring Boot significantly simplifies enterprise application development in Java. We will cover auto-configuration, the embedded Tomcat server, Starters, and other key features that make Spring Boot the number one choice for Java development.', 'Maria Sidorova', '2025-02-20 14:30:00');

INSERT INTO videos (title, url, duration) VALUES
('Introduction to Java 21', 'https://example.com/videos/java-21-intro', '00:15:30'),
('Spring Boot Microservices', 'https://example.com/videos/spring-boot-microservices', '00:45:12');

INSERT INTO podcasts (title) VALUES
('Technologies of the Future'),
('Programmers of the Future');

INSERT INTO podcast_episodes (podcast_id, episode_number, title, audio_url, duration) VALUES
(1, 1, 'Artificial Intelligence in 2025', 'https://example.com/podcasts/ai-2025.mp3', '00:32:15'),
(1, 2, 'Quantum Computing for Everyone', 'https://example.com/podcasts/quantum-computing.mp3', '00:28:45'),
(1, 3, 'The Future of Web Development', 'https://example.com/podcasts/web-dev-future.mp3', '00:41:20'),
(2, 1, 'Docker and Kubernetes from Scratch', 'https://example.com/podcasts/docker-k8s.mp3', '00:38:10'),
(2, 2, 'CI/CD Pipelines in Detail', 'https://example.com/podcasts/cicd-pipelines.mp3', '00:25:55'),
(2, 3, 'Cloud Technologies: AWS vs GCP vs Azure', 'https://example.com/podcasts/cloud-comparing.mp3', '00:44:30');
