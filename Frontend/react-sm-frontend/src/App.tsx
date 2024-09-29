import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Feed from "./components/Feed";
import Navbar from "./components/Navbar";
import Search from "./components/Search";
import SignUp from "./components/SignUp";

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" Component={Feed} />
        <Route path="/search" element={<Search />} />
        <Route path="/signup" element={<SignUp />} />
      </Routes>
    </Router>
  );
}

export default App;
