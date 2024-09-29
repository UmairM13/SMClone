import React from "react";
import { Link } from "react-router-dom";

const Navbar: React.FC = () => {
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
            <li className="nav-item">
              <Link className="nav-link" to="/">
                Feed
              </Link>
            </li>
          </ul>
          <Link to="/search" className="btn btn-outline-primary ms-2">
            Search Users
          </Link>
          <Link to="/login" className="btn btn-primary ms-2">
            Login
          </Link>
          <Link to="/signup" className="btn btn-secondary ms-2">
            Signup
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
