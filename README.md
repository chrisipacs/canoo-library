# canoo-library

## API Endpoints

```GET /api/books```

Gets all the books (taking the page size limit into consideration, which is currently 50)

```GET /api/books?author=Tolkien```

Lists all the books from Tolkien

```GET /api/books?author=Tolkien&publicationDateFrom=1900-01-01&publicationDateTo=2000-01-01```

All of Tolkien’s books from the 20th century

```/api/books?orderByDesc=publicationDate&genre=DRAMA&pageNumber=0&pageSize=50```

The first 50 books in the drama category sorted by publication date

```GET /api/books/{id}```

The book with the id {id}

```POST /login```

An existing user can obtain a JWT token at this endpoint by authenticating with basic authenication. ("Basic "+base64 encoded username:password)

```PUT /register```

A new user can register by sending the user object in JSON format. Example:

``` 
{
  "username" : "testUser",
  "email" : “christianipacs@gmail.com",
  "password" : "password123"
}
```

## Persistence
In early versions, the objects were kept in memory. In the final version, the InMemoryBookRepository and InMemoryUserRepository classes are still present, but they are not used anymore.

The persistence layer is based on JPA/Hibernate + Spring data JPA. Beside the Spring Data interfaces, I also created a custom repository class using the criteria API.

There is a maximum page size of 100 items to prevent abuse.

## Security
The authentication is based on JWT tokens, so it’s completely stateless (no server side session). The JWT token should be sent in the Authorization header with the Bearer prefix.

The token can be obtained at the /login and /register endpoints (the latter of which does not require any kind of credentials).

For the JWT implementation, I implemented a custom AuthenticationProvider and AuthenticationFilter.
The roles and method level security are also handled by Spring Security.

## Other
The connection details for the database and the jwt secret are stored in the application.properties file. Of course, in a real project they would be stored as environment variables, here I made an exception for simplicity.

Test coverage is 89% according to IntelliJ

The app requires Java 10 to build (can modify it to use Java 8 with relatively low effort).
