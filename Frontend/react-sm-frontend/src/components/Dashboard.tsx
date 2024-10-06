import React, { useEffect, useState } from "react";
import { getUser } from "../services/UserApi";
import "../styles/dashboard.css";
import {
  addPost,
  updatePost,
  likePost,
  unlikePost,
  deletePost,
} from "../services/PostApi";
import { Post } from "../interfaces/Post";
import PostItem from "../components/PostItem";

const Dashboard: React.FC = () => {
  const [followers, setFollowers] = useState<string[]>([]);
  const [following, setFollowing] = useState<string[]>([]);
  const [posts, setPosts] = useState<Post[]>([]);
  const [newPost, setNewPost] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(true);
  const [editingPostId] = useState<number | null>(null);

  const userId = localStorage.getItem("id");
  const token = localStorage.getItem("token");
  const currentUserId = userId ? parseInt(userId) : null;

  // Function to fetch user data
  const fetchUserData = async () => {
    if (!userId) {
      console.error("User ID not found");
      setLoading(false);
      return; // Exit if userId is not found
    }

    try {
      const userData = await getUser(userId);
      // Fetch followers, following, and posts from userData
      setFollowers(userData.followers);
      setFollowing(userData.following);
      setPosts(userData.posts);
    } catch (error: any) {
      console.error("Error fetching user data:", error);
    } finally {
      setLoading(false); // Ensure loading state is set to false in all cases
    }
  };

  useEffect(() => {
    fetchUserData(); // Call the fetchUserData function on mount
  }, [userId]);

  const handleAddPost = async () => {
    if (newPost.trim() === "") {
      return; // Don't allow empty posts
    }

    try {
      if (!userId || !token) {
        console.error("User ID or token not found");
        return; // Exit if userId or token is not available
      }

      const postData = await addPost(userId, token, newPost);
      console.log("New post added:", postData);
      setNewPost(""); // Clear the input field

      await fetchUserData(); // Refresh the post list
    } catch (error: any) {
      console.error("Failed to add post:", error);
    }
  };

  const handleLike = async (postId: number) => {
    try {
      await likePost(postId, token);
      await fetchUserData(); // Refresh the post list
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleDislike = async (postId: number) => {
    try {
      await unlikePost(postId, token);
      await fetchUserData(); // Refresh the post list
    } catch (error) {
      console.error("Error disliking post:", error);
    }
  };

  const handleUpdatePost = async (postId: number, text: string) => {
    try {
      await updatePost(postId.toString(), userId!, token!, text);
      setPosts((prevPosts) =>
        prevPosts.map((post) => (post.id === postId ? { ...post, text } : post))
      );
      await fetchUserData();
    } catch (error) {
      console.error("Error updating post:", error);
    }
  };

  const handleDelete = async (postId: number) => {
    try {
      await deletePost(postId, token);
      await fetchUserData();
    } catch (error) {
      console.error("Error deleting post:", error);
    }
  };

  // Sort posts by published date (newer first)
  const sortedPosts = [...posts].sort((a, b) => {
    return (b.epochSecond || 0) - (a.epochSecond || 0);
  });

  return (
    <div className="dashboard">
      <h2 className="dashboard-title">User Dashboard</h2>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <>
          <div className="dashboard-info">
            <div className="info-box">
              <h3>Followers</h3>
              <p>{followers.length}</p> {/* Show number of followers */}
            </div>
            <div className="info-box">
              <h3>Following</h3>
              <p>{following.length}</p> {/* Show number of following */}
            </div>
          </div>

          <div className="new-post-section">
            <textarea
              placeholder="What's on your mind?"
              value={newPost}
              onChange={(e) => setNewPost(e.target.value)}
              className="post-input"
            />
            <button onClick={handleAddPost} className="add-post-button">
              Add Post
            </button>
          </div>

          <div className="user-posts">
            <h3>Your Posts</h3>
            {sortedPosts.length > 0 ? (
              sortedPosts.map((post) => (
                <div key={post.id}>
                  {editingPostId === post.id ? (
                    <div></div>
                  ) : (
                    <PostItem
                      post={post}
                      currentUserId={currentUserId}
                      onLike={handleLike}
                      onDislike={handleDislike}
                      onEdit={handleUpdatePost}
                      onDelete={handleDelete}
                    />
                  )}
                </div>
              ))
            ) : (
              <p>You haven't created any posts yet.</p>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default Dashboard;
