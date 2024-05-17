# kotlin-wiremock

# restassured-kotlin-template
![Java version](https://img.shields.io/badge/Java-1.11-%23b07219)

# Installation

1. Open repo in your favourite IDE and set Project SDK to "java version 1.11.0_*"
2. Right click on pom.xml and set MAVEN project, you might need to re-import maven dependencies
3. For better work experience don't forget to install Gherkin plugin and Cucumber for Kotlin plugin in Intellij
4. Install Docker Desktop


# Features

1. Set environment (dev, test or local) in environment.yml file
2. Wiremock
- User can mock endpoints by providing "wiremock-host" and "mocked-endpoints"
- Wiremock starts before test-suite and ends after test-suite. To make usage of it add mocked endpoint to properties.yml file
  and define mocked endpoint in Stubs class
- When mocked endpoint is not defined but app endpoint exists then test will run on app endpoint
- When mocked endpoint is defined and the same app endpoint exists then test will run on mocked endpoint
3. Allure report
4. Tests execution in docker container

# Run

1. Before run
- Verify if environment.yml "env" field is set with one of three available values (dev, test, local).
2. Run Tests
- Run "ApiRunner.kt" class
- Run from terminal. This requires maven to be installed and MAVEN_HOME set
  as environmental variable

       brew install maven
       mvn clean test 

- Tests can be run also in docker container

      docker-compose up 
      docker run -v 'pwd':/usr/src/app api-tests mvn clean test


# Generate Allure Report

After tests are executed result.json file should generate in /allure-results folder. To generate index.thml report
in target/site folder you need to execute in terminal

      mvn allure:report -Dallure.results.directory=../allure-results

# Usage

Example Test Feature file

      @apiTest
      Scenario: Example Api test
        Given I send "GET" request to "" endpoint
        Then I get status code "200"