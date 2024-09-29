import React, { useState, useEffect } from "react";
import { fetchFeed } from "../services/PostApi";
import { Post } from "../interfaces/Post";
import PostItem from "./PostItem";

const Feed: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
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

    loadFeed();
  }, []);

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
            <PostItem key={post.id} post={post} />
          ))}
        </div>
      )}
    </div>
  );
};

export default Feed;
