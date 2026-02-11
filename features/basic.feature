Feature: Upcoming Bikes Extraction

  Scenario Outline: Verify upcoming bikes for different manufacturers
    Given User navigates to Home Page
    When user goes to New Bikes and Clicks Upcoming Bikes
    And user selects the manufacturer "<manufacturer>"
    Then user sees the "<manufacturer>" upcoming bikes page
    And user exports filtered bikes to excel for "<manufacturer>"

    Examples: 
      | manufacturer  |
      | Royal Enfield |
      | Yamaha		  |
      | TVS			  |
      | Bajaj		  |
      | Hero Moto Corp|
      