import React, { useState, useEffect } from "react";
import { fetchFeed } from "../services/PostApi";
import { Post } from "../interfaces/Post";
import PostItem from "./PostItem";
import { likePost, unlikePost, deletePost } from "../services/PostApi";

const Feed: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const id = localStorage.getItem("id");
  const token = localStorage.getItem("token");
  const currentUserId = id ? parseInt(id) : null;

  useEffect(() => {
    loadFeed(); // Call loadFeed on component mount

    // Optional: If you want to refresh the feed when the token changes
  }, [token]);

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

  const handleLike = async (postId: number) => {
    try {
      console.log("Like post with ID:", postId);
      console.log("Token:", token);
      await likePost(postId, token);
      console.log("Post liked successfully");
      await loadFeed(); // Refresh the feed after liking
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleDislike = async (postId: number) => {
    try {
      console.log("Dislike post with ID:", postId);
      await unlikePost(postId, token);
      console.log("Post unliked successfully");
      await loadFeed(); // Refresh the feed after unliking
    } catch (error) {
      console.error("Error disliking post:", error);
    }
  };

  const handleEdit = (postId: number) => {
    console.log("Edit post with ID:", postId);
  };

  const handleDelete = async (postId: number) => {
    try {
      console.log("Delete post with ID:", postId);
      await deletePost(postId, token);
      console.log("Post deleted successfully");
      await loadFeed();
    } catch (error) {
      console.error("Error deleting post:", error);
    }
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
