import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const useInactivityLogout = () => {
  const navigate = useNavigate();

  useEffect(() => {
    let timer: NodeJS.Timeout;

    const handleActivity = () => {
      clearTimeout(timer);
      timer = setTimeout(() => {
        // Logout logic here
        // For example, clear user data and navigate to the login page
        navigate("/login"); // Redirect to login after inactivity
      }, 300000); // 5 minutes of inactivity
    };

    window.addEventListener("mousemove", handleActivity);
    window.addEventListener("keypress", handleActivity);

    return () => {
      clearTimeout(timer);
      window.removeEventListener("mousemove", handleActivity);
      window.removeEventListener("keypress", handleActivity);
    };
  }, [navigate]);
};

export default useInactivityLogout;
