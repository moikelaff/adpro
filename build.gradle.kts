val seleniumJavaVersion = "4.14.1"
val seleniumJupiterVersion = "5.0.1"
val webdriverManagerVersion = "5.6.3"
val junitJupiterVersion = "5.10.2"

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")

	// Using JUnit BOM to ensure consistent versions
	testImplementation(platform("org.junit:junit-bom:$junitJupiterVersion"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.mockito:mockito-core:4.8.0")
	testImplementation("org.mockito:mockito-junit-jupiter:4.8.0")
	// Security fixes
	implementation("commons-io:commons-io:2.15.1")
	implementation("org.apache.commons:commons-compress:1.26.0")
	implementation("org.bouncycastle:bcprov-jdk18on:1.77")

	// Selenium dependencies
	testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumJavaVersion")
	testImplementation("io.github.bonigarcia:selenium-jupiter:$seleniumJupiterVersion")
	testImplementation("io.github.bonigarcia:webdrivermanager:$webdriverManagerVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.register<Test>("unitTest") {
	description = "Runs unit tests."
	group = "verification"
	filter {
		excludeTestsMatching("*FunctionalTest")
	}
}

tasks.register<Test>("functionalTest") {
	description = "Runs functional tests."
	group = "verification"
	filter {
		includeTestsMatching("*FunctionalTest")
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

tasks.test {
	filter {
		includeTestsMatching("*FunctionalTest")
	}
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
}