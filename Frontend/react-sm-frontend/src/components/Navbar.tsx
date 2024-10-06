import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { logout } from "../services/UserApi"; // Import the logout function from UserApi

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const handleLogout = async () => {
    try {
      if (token) {
        console.log("Attempting to log out with token:", token); // Log the token
        const response = await logout(token);
        console.log("Logout response:", response); // Log the response

        // Clear local storage
        localStorage.removeItem("id");
        localStorage.removeItem("token");

        // Redirect to login
        navigate("/login");
      } else {
        console.warn("No token found, user may already be logged out.");
      }
    } catch (error) {
      console.error("An error occurred while logging out:", error); // Log the error
    }
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">
          MyApp
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item"></li>
          </ul>
          {token ? (
            <>
              <Link to="/search" className="btn btn-outline-primary ms-2">
                Search Users
              </Link>
              <Link to="/dashboard" className="btn btn-outline-primary ms-2">
                Dashboard
              </Link>
              <button className="btn btn-danger ms-2" onClick={handleLogout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/search" className="btn btn-outline-primary ms-2">
                Search Users
              </Link>
              <Link to="/login" className="btn btn-primary ms-2">
                Login
              </Link>
              <Link to="/signup" className="btn btn-secondary ms-2">
                Signup
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
