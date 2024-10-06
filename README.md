# Social Media clone

This is a simple Twitter clone built with a Spring Boot backend, React frontend, and MySQL database. The web application allows users to log in using Bearer token authentication, create, edit, and delete posts, follow and unfollow other users, and like or unlike posts.

## Table of contents

- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Frontend](#frontend)

## Technologies Used

- **Backend**:

  - [Spring Boot](https://spring.io/projects/spring-boot) - A framework for building Java-based web applications.
  - [MySQL](https://www.mysql.com/) - Database used for storing data.

- **Frontend**:
  - [React](https://reactjs.org/) - A JavaScript library for building user interfaces.
  - [TypeScript](https://www.typescriptlang.org/) - A superset of JavaScript that adds static types.

## Installation

## Backend

1. Clone the repository:

`git clone https://github.com/UmairM13/SMClone.git`

`cd SpringFSD`

`./mvnw install`

`./mvnw spring-boot:run`

If you go into `MySQLConfig` file you can change the code to connect your own database

## Frontend

`cd Frontend`

`cd react-sm-frontend`

`npm install`

`npm run dev`

## Usage

The backend server runs on `localhost:3333` which can be changed in the application properties.
The frontend server runs on `localhost:5173`.

After you run the server, in a browser paster the url `http://localhost:5173/`
which should take you to the feed page.

Once on, there should be an option to log in, search for users and sign up.

## API Documentation

For detailed information about the API endpoints, please refer to the API documentation here:
[Chirrup API Documentation](https://app.swaggerhub.com/apis/MMU-SE/Chirrup/1.0.0/#/Post%20Management/getPost).
