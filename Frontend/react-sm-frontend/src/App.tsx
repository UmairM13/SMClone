import "./App.css";
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Feed from "./components/Feed";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" Component={Feed} />
      </Routes>
    </Router>
  );
}

export default App;
