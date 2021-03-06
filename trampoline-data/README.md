# trampoline-data

This module extends functionality that is found in Spring Data JPA by using a base entity and enabling features like JPA auditing.
Pagination is also a common issue regarding project so there's a bunch of Pagination related classes.

## Architecture

At the core of trampoline-data we have `TrampolineEntity` which acts as a base entity for JPA.
To manage `TrampolineEntity` it's recommended to implement the `TrampolineRepository` interface(which extends `JpaRepository`).

Another common exception I was missing is one when a resource(entity) could not be found.
This is why there's a `ResourceNotFoundException`. This is a `RuntimeException` which will throw a 404 to the client.

To remap a `Page` object we have created the `MappedPage#of` method. This accepts a `Page` object and a mapping lambda and will return a `MappedPage`.