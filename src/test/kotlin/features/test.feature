Feature: Test

  @apiTest
  Scenario: Test
    Given I send "GET" request to "/some/lovely/endpoint" endpoint
    Then I get status code "200"