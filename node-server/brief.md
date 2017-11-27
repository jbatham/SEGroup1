Server plan:

1. receive a json from app, containing lat, long, distance
2. controller takes request params, computes values for great circle calc
   to get set of house locations
3. use the db model to perform the mysql db request
4. package the set of house data as a json
