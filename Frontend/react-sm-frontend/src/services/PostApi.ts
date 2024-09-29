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
