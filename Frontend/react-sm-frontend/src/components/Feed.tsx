import React, { useState, useEffect } from "react";
import { fetchFeed } from "../services/PostApi";
import { Post } from "../interfaces/Post";

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
            <div key={post.id} className="list-group-item">
              <h5 className="mb-1">{post.author.username}</h5>
              <p className="mb-1">{post.text}</p>
              <small className="text-muted">
                Likes: {post.likes.length} | Published on{" "}
                {post.epochSecond
                  ? new Date(post.epochSecond * 1000).toLocaleString()
                  : " Unknown"}
              </small>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Feed;
