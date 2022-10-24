##CategoryManagement Application

CategoryManagement using nested set.

Pre-requisite:

<li>docker and docker-compose should be available and docker daemon running
<li>Please make sure no other app is running on port 8080 and 5432.</li>
<li> Active internet connection required to download dependencies like JDK and Postgres. </li>

To start the application do the following:

1. Open a terminal and cd to the root directory for this project
2. `mvn package` to create a jar
3. run `docker-compose up` (To run as daemon `docker-compose up -d`)

The application will be available to at `http://localhost:8080/`

API details can be found using swagger UI at `http://localhost:8080/swagger-ui.html#`

API doc is generated using swagger which can be found at `http://localhost:8080/v2/api-docs` 