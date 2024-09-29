import "./App.css";
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Feed from "./components/Feed";
import Navbar from "./components/Navbar";
import Search from "./components/Search";

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" Component={Feed} />
        <Route path="/search" element={<Search />} />
      </Routes>
    </Router>
  );
}

export default App;
