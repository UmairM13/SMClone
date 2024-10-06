import React, { useState, useEffect } from "react";
import {
  fetchFeed,
  likePost,
  unlikePost,
  deletePost,
  updatePost,
} from "../services/PostApi";
import { Post } from "../interfaces/Post";
import PostItem from "./PostItem";

const Feed: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const id = localStorage.getItem("id");
  const token = localStorage.getItem("token");
  const currentUserId = id ? parseInt(id) : null;

  useEffect(() => {
    loadFeed();
  }, [token]);

  const loadFeed = async () => {
    try {
      const feed = await fetchFeed();
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
      await likePost(postId, token);
      await loadFeed();
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleDislike = async (postId: number) => {
    try {
      await unlikePost(postId, token);
      await loadFeed();
    } catch (error) {
      console.error("Error disliking post:", error);
    }
  };

  const handleUpdatePost = async (postId: number, text: string) => {
    try {
      await updatePost(postId.toString(), id!, token!, text);
      setPosts((prevPosts) =>
        prevPosts.map((post) => (post.id === postId ? { ...post, text } : post))
      );
      await loadFeed();
    } catch (error) {
      console.error("Error updating post:", error);
    }
  };

  const handleDelete = async (postId: number) => {
    try {
      await deletePost(postId, token);
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
              onEdit={handleUpdatePost} // Pass the onEdit prop here
              onDelete={handleDelete}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default Feed;
