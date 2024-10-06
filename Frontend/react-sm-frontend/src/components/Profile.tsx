import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUser, followUser, unfollowUser } from "../services/UserApi";
import "../styles/profile.css";
import { Post } from "../interfaces/Post";
import PostItem from "../components/PostItem";
import { likePost, unlikePost } from "../services/PostApi";

const Profile: React.FC = () => {
  const { userId } = useParams<{ userId: string }>();
  const [userData, setUserData] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [isFollowing, setIsFollowing] = useState<boolean>(false);

  if (!userId) {
    return <p>User ID is missing</p>;
  }

  const id = localStorage.getItem("id");
  const currentUserId = id ? parseInt(id) : null;
  const token = localStorage.getItem("token");

  const fetchUserProfile = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getUser(userId);
      console.log("Fetched user data:", data); // Log fetched user data
      setUserData(data);
      setIsFollowing(
        data.followers.some((follower: any) => follower.id === currentUserId)
      );
    } catch (err: any) {
      setError("Error fetching user profile");
    } finally {
      setLoading(false);
    }
  };

  const handleLike = async (postId: number) => {
    try {
      await likePost(postId, token);
      await fetchUserProfile();
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleDislike = async (postId: number) => {
    try {
      await unlikePost(postId, token);
      await fetchUserProfile();
    } catch (error) {
      console.error("Error disliking post:", error);
    }
  };

  const handleFollow = async () => {
    try {
      await followUser(userId, token);
      setIsFollowing(true);
      await fetchUserProfile();
    } catch (error) {
      console.error("Error following user:", error);
    }
  };

  const handleUnfollow = async () => {
    try {
      await unfollowUser(userId, token);
      setIsFollowing(false);
      await fetchUserProfile();
    } catch (error) {
      console.error("Error unfollowing user:", error);
    }
  };

  useEffect(() => {
    fetchUserProfile(); // Fetch profile data on mount
  }, [userId]);

  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <p className="text-danger">{error}</p>;
  }

  if (!userData) {
    return <p>User not found</p>;
  }

  return (
    <div className="profile">
      <h2 className="profile-username">{userData.username}</h2>

      {/* Follow and Unfollow Buttons */}
      <div className="follow-button-container">
        <button
          className={`follow-btn ${isFollowing ? "hidden" : ""}`}
          onClick={handleFollow}
          disabled={isFollowing} // Disable if already following
        >
          Follow
        </button>
        <button
          className={`unfollow-btn ${!isFollowing ? "hidden" : ""}`}
          onClick={handleUnfollow}
        >
          Unfollow
        </button>
      </div>

      <div className="profile-info">
        <div className="info-box">
          <h3>Followers</h3>
          <p>{userData.followers.length}</p>
        </div>
        <div className="info-box">
          <h3>Following</h3>
          <p>{userData.following.length}</p>
        </div>
      </div>

      <div className="profile-posts">
        <h3>{userData.username}'s Posts</h3>
        {userData.posts && userData.posts.length > 0 ? (
          userData.posts.map((post: Post) => (
            <PostItem
              key={post.id}
              post={post}
              currentUserId={currentUserId}
              onLike={handleLike}
              onDislike={handleDislike}
              onEdit={() => {}}
              onDelete={() => {}}
            />
          ))
        ) : (
          <p>This user hasn't posted anything yet.</p>
        )}
      </div>
    </div>
  );
};

export default Profile;
