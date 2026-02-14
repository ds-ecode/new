Feature: Going to Used Cars Page

  Scenario Outline: Checking Used cars page
    Given User navigates to HomePage
    When User navigates to Used Cars page
    And user selects the city "<cityName>"
    Then user exports filtered used cars to excel for "<cityName>"
    Then checks if thats Used cars page or not "<cityName>"
    

    Examples:
	  | cityName	|
	  | Chennai		|
	  | Bangalore	|
	  | Mumbai		|