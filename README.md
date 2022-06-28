### Commands for testing application

Get all circuits:
> curl 'http://localhost:8080/f1-stats-war/rest/v1/stats/circuit/all' | python -m json.tool

Get all circuit names:
> curl 'http://localhost:8080/f1-stats-war/rest/v1/stats/circuit/names' | python -m json.tool

Get specific circuit:
> curl 'http://localhost:8080/f1-stats-war/rest/v1/stats/circuit?name=Jarama' | python -m json.tool

Get all drivers:
> curl 'http://localhost:8080/f1-stats-war/rest/v1/stats/driver/all' | python -m json.tool

Get specific driver:
> curl 'http://localhost:8080/f1-stats-war/rest/v1/stats/driver?firstName=Fernando&lastName=Alonso' | python -m json.tool

Get all teams:
> curl 'http://localhost:8080/f1-stats-war/rest/v1/stats/team/all' | python -m json.tool

Get all team names:
> curl 'http://localhost:8080/f1-stats-war/rest/v1/stats/team/names' | python -m json.tool

Get specific team:
> curl 'http://localhost:8080/f1-stats-war/rest/v1/stats/team?name=McLaren' | python -m json.tool
