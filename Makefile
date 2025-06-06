.PHONY: upgrade-gradle-wrapper

upgrade-gradle-wrapper:
	@echo "Upgrading Gradle wrapper to 8.5..."
	@./gradlew wrapper --gradle-version=8.5
	@./gradlew --version
