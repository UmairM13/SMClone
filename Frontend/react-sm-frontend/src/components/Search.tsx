import React, { useState, useEffect } from "react";
import { searchUsers } from "../services/PostApi";

const Search: React.FC = () => {
  const [query, setQuery] = useState<string>("");
  const [results, setResults] = useState<any[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  // Function to fetch users based on the query
  const fetchUsers = async (searchQuery: string) => {
    setLoading(true);
    setError(null);
    try {
      const trimmedQuery = searchQuery.trim();
      console.log(`Trimmed Query: "${trimmedQuery}"`); // Log the trimmed query

      // Fetch users based on the query
      const users = await searchUsers(trimmedQuery);
      console.log(users); // Log the results from the API
      setResults(users);
    } catch (error: any) {
      setError(error.message || "Something went wrong");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // Only initiate search if query has at least 1 character
    if (query.length > 0) {
      const timeoutId = setTimeout(() => fetchUsers(query), 300); // 300 ms delay
      return () => clearTimeout(timeoutId); // Clean up the timeout
    } else {
      // Clear results if the query is empty
      setResults([]);
    }
  }, [query]); // Effect runs whenever the query changes

  const handleSearch = () => {
    // Call fetchUsers with an empty query to get all users
    fetchUsers(query.length > 0 ? query : "");
  };

  return (
    <div className="mt-5">
      <h1>Search Users</h1>
      <div className="input-group">
        <input
          type="text"
          className="form-control"
          placeholder="Search for users"
          value={query}
          onChange={(e) => setQuery(e.target.value)} // Update query as the user types
        />
        <button
          className="btn btn-primary"
          onClick={handleSearch}
          disabled={loading}
        >
          Search
        </button>
      </div>
      {loading && (
        <div className="mt-3">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      )}
      {error && <div className="text-danger">Error: {error}</div>}
      {results.length > 0 && !loading && (
        <ul className="list-group mt-3">
          {results.map((user) => (
            <li key={user.id} className="list-group-item">
              {user.username} - {user.firstName} {user.lastName}
            </li>
          ))}
        </ul>
      )}
      {/* Display "No results found" only if there are no results */}
      {results.length === 0 && !loading && query.length > 0 && (
        <p>No results found</p>
      )}
    </div>
  );
};

export default Search;
