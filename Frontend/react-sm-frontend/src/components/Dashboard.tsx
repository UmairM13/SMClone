import React, { useEffect, useState } from "react";
import { getUser } from "../services/UserApi";
import "../styles/dashboard.css";
import { addPost } from "../services/PostApi";
import { Post } from "../interfaces/Post";
import PostItem from "../components/PostItem";
import { likePost, unlikePost, deletePost } from "../services/PostApi";

const Dashboard: React.FC = () => {
  const [followers, setFollowers] = useState<string[]>([]);
  const [following, setFollowing] = useState<string[]>([]);
  const [posts, setPosts] = useState<Post[]>([]);
  const [newPost, setNewPost] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(true);

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

      // Refetch the user data to get the updated posts
      await fetchUserData(); // Call fetchUserData after adding the post
    } catch (error: any) {
      console.error("Failed to add post:", error);
    }
  };

  const handleLike = async (postId: number) => {
    try {
      console.log("Like post with ID:", postId);
      console.log("Token:", token);
      await likePost(postId, token);
      console.log("Post liked successfully");
      await fetchUserData();
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleDislike = async (postId: number) => {
    try {
      console.log("Dislike post with ID:", postId);
      await unlikePost(postId, token);
      console.log("Post unliked successfully");
      await fetchUserData();
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
      await fetchUserData();
    } catch (error) {
      console.error("Error deleting post:", error);
    }
  };

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
            {posts.length > 0 ? (
              // Reverse the posts array to display new posts at the top
              [...posts]
                .reverse()
                .map((post) => (
                  <PostItem
                    key={post.id}
                    post={post}
                    currentUserId={currentUserId}
                    onLike={handleLike}
                    onDislike={handleDislike}
                    onEdit={handleEdit}
                    onDelete={handleDelete}
                  />
                )) // Use post.id as key for better performance
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
