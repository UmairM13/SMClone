import React, { useState, useEffect } from "react";
import { fetchFeed } from "../services/PostApi";
import { Post } from "../interfaces/Post";
import PostItem from "./PostItem";

const Feed: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const id = localStorage.getItem("id");
  const currentUserId = id ? parseInt(id) : null;

  useEffect(() => {
    const loadFeed = async () => {
      try {
        const feed = await fetchFeed();
        console.log("Fetched Feed:", feed);
        const sortedFeed = feed.sort(
          (a: Post, b: Post) => b.epochSecond - a.epochSecond
        );
        setPosts(sortedFeed);
        setLoading(false);
      } catch (error: any) {
        setError(error.message);
        setLoading(false);
      }
    };

    loadFeed();
  }, []);

  const handleLike = (postId: number) => {
    console.log("Like post with ID:", postId);
  };

  const handleDislike = (postId: number) => {
    console.log("Dislike post with ID:", postId);
  };

  const handleEdit = (postId: number) => {
    console.log("Edit post with ID:", postId);
  };

  const handleDelete = (postId: number) => {
    console.log("Delete post with ID:", postId);
  };

  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  console.log("Current User ID:", currentUserId); // Log current user ID

  return (
    <div className="mt-5">
      <h1> Feed </h1>
      {posts.length === 0 ? (
        <p>No posts available</p>
      ) : (
        <div className="list-group">
          {posts.map((post) => (
            <PostItem
              key={post.id}
              post={post}
              currentUserId={currentUserId}
              onLike={handleLike}
              onDislike={handleDislike}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default Feed;
