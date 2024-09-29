import React from "react";
import { Post } from "../interfaces/Post";

interface PostItemProps {
  post: Post;
}

const PostItem: React.FC<PostItemProps> = ({ post }) => {
  return (
    <div className="list-group-item">
      <h5 className="mb-1">{post.author.username}</h5>
      <p className="mb-1">{post.text}</p>
      <small className="text-muted">
        Likes: {post.likes.length} | Published on{" "}
        {post.epochSecond
          ? new Date(post.epochSecond * 1000).toLocaleString()
          : " Unknown"}
      </small>
    </div>
  );
};

export default PostItem;
