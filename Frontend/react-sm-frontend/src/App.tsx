import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Feed from "./components/Feed";
import Navbar from "./components/Navbar";
import Search from "./components/Search";
import SignUp from "./components/SignUp";
import Login from "./components/Login";
import Dashboard from "./components/Dashboard";

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" Component={Feed} />
        <Route path="/search" element={<Search />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
