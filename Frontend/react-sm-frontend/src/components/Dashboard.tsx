import React, { useEffect, useState } from "react";
import { getUser } from "../services/UserApi";
import "../styles/dashboard.css";
import { addPost } from "../services/PostApi";
import { Post } from "../interfaces/Post";
import PostItem from "../components/PostItem"; // Import the PostItem component

const Dashboard: React.FC = () => {
  const [followers, setFollowers] = useState<string[]>([]);
  const [following, setFollowing] = useState<string[]>([]);
  const [posts, setPosts] = useState<Post[]>([]);
  const [newPost, setNewPost] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(true);

  const userId = localStorage.getItem("id");
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        if (!userId) {
          throw new Error("User ID not found");
        }
        const userData = await getUser(userId);

        setFollowers(userData.followers);
        setFollowing(userData.following);
        setPosts(userData.posts);
        setLoading(false);
      } catch (error: any) {
        console.error(error);
      }
    };
    fetchUserData();
  }, [userId]);

  const handleAddPost = async () => {
    if (newPost.trim() === "") {
      return;
    }

    try {
      const postData = await addPost(userId!, token!, newPost);
      setPosts([postData, ...posts]);
      setNewPost("");
    } catch (error: any) {
      console.error(error);
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
              posts.map((post, index) => (
                <PostItem key={index} post={post} /> // Use PostItem here
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
