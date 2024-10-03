import React from "react";
import { Post } from "../interfaces/Post";

interface PostItemProps {
  post: Post;
}

const PostItem: React.FC<PostItemProps> = ({ post }) => {
  console.log(post); // Add this line to see the post object in the console

  if (!post) {
    return <div>No content available</div>;
  }

  const authorName = post.author?.username ?? `User ID: ${post.authorId}`;
  const text = post.text || "No text available";
  const likesCount = post.likes?.length || 0;
  const publishedDate = post.epochSecond
    ? new Date(post.epochSecond * 1000).toLocaleString()
    : "Date not available";

  return (
    <div className="list-group-item">
      <p className="mb-1">{authorName}</p>
      <h5 className="mb-1">{text}</h5>
      <small className="text-muted">
        Likes: {likesCount} | Published on {publishedDate}
      </small>
    </div>
  );
};

export default PostItem;
