import { Author } from "./Author";
import { Like } from "./Likes";

export interface Post {
  id: number;
  text: string;
  epochSecond: number;
  author?: Author;
  authorId?: number;
  likes: Like[];
}
