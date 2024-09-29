import axios from "axios";

const API_URL = "http://localhost:3333";

export const signup = async (userData: {
  firstName: string;
  lastName: string;
  username: string;
  password: string;
}) => {
  try {
    const response = await axios.post(`${API_URL}/users`, userData);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response?.status === 400) {
      throw new Error("User already exists");
    }
    throw new Error("Error signing up");
  }
};
