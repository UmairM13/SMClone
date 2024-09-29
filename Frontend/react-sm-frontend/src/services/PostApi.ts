import axios from "axios";
import { Post } from "../interfaces/Post";

const API_URL = "http://localhost:3333";

export const fetchFeed = async (): Promise<Post[]> => {
  try {
    const response = await axios.get(`${API_URL}/feed`);
    return response.data;
  } catch (error) {
    throw new Error("Error fetching feed");
  }
};

export const searchUsers = async (q: string): Promise<any[]> => {
  try {
    console.log(`Searching for users with query: "${q}"`);
    const response = await axios.get(`${API_URL}/search?q=${q}`);
    return response.data;
  } catch (error) {
    throw new Error("Error searching users");
  }
};
