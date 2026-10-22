INSERT INTO articles (title, text, author, published_at) VALUES
('Как освоить Java за 30 дней', 'Java остается одним из самых востребованных языков программирования. В этой статье мы разберем пошаговый план изучения Java с нуля до уверенного уровня. Вы узнаете о ключевых концепциях ООП, работе с коллекциями, многопоточности и современных фреймворках.', 'Иван Петров', '2025-01-15 10:00:00'),
('Основы Spring Boot для начинающих', 'Spring Boot значительно упрощает разработку корпоративных приложений на Java. Мы рассмотрим автоконфигурацию, встроенный сервер Tomcat, Starters и другие ключевые возможности, которые делают Spring Boot выбором номер один для Java-разработки.', 'Мария Сидорова', '2025-02-20 14:30:00');

INSERT INTO videos (title, url, duration) VALUES
('Введение в Java 21', 'https://example.com/videos/java-21-intro', '00:15:30'),
('Spring Boot микросервисы', 'https://example.com/videos/spring-boot-microservices', '00:45:12');

INSERT INTO podcasts (title) VALUES
('Технологии будущего'),
('Программисты будущего');

INSERT INTO podcast_episodes (podcast_id, episode_number, title, audio_url, duration) VALUES
(1, 1, 'Искусственный интеллект в 2025 году', 'https://example.com/podcasts/ai-2025.mp3', '00:32:15'),
(1, 2, 'Квантовые вычисления для всех', 'https://example.com/podcasts/quantum-computing.mp3', '00:28:45'),
(1, 3, 'Будущее веб-разработки', 'https://example.com/podcasts/web-dev-future.mp3', '00:41:20'),
(2, 1, 'Docker и Kubernetes с нуля', 'https://example.com/podcasts/docker-k8s.mp3', '00:38:10'),
(2, 2, 'CI/CD пайплайны в деталях', 'https://example.com/podcasts/cicd-pipelines.mp3', '00:25:55'),
(2, 3, 'Облачные технологии: AWS vs GCP vs Azure', 'https://example.com/podcasts/cloud-comparing.mp3', '00:44:30');
