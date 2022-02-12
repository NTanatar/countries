Meta:

Narrative:
As a user
I want to add a city
So that I can ...

Scenario: add a city to existing country
Given there is a country with id 777
When I add a city with name Cheese to country with id 777
Then the city is added

!-- Scenario: non existing country id -> an exception

