// Updated PostItem component
import React, { useState } from "react";
import { Post } from "../interfaces/Post";
import { Link } from "react-router-dom"; // Import Link for navigation
import "../styles/PostItem.css";

interface PostItemProps {
  post: Post;
  currentUserId: number | null;
  onLike: (postId: number) => void;
  onDislike: (postId: number) => void;
  onEdit: (postId: number, newText: string) => void;
  onDelete: (postId: number) => void;
}

const PostItem: React.FC<PostItemProps> = ({
  post,
  currentUserId,
  onLike,
  onDislike,
  onEdit,
  onDelete,
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const [newText, setNewText] = useState(post.text || "");
  const [showActions, setShowActions] = useState(false);

  const handleSave = () => {
    if (newText.trim()) {
      onEdit(post.id, newText);
      setIsEditing(false);
    }
  };

  const handleDelete = () => {
    onDelete(post.id);
  };

  if (!post) {
    return <div>No content available</div>;
  }

  const authorName = post.author?.username ?? `User ID: ${post.authorId}`;
  const likesCount = post.likes?.length || 0;
  const publishedDate = post.epochSecond
    ? new Date(post.epochSecond * 1000).toLocaleString()
    : "Date not available";

  return (
    <div className="list-group-item post-item" style={{ position: "relative" }}>
      <div className="post-header">
        <Link
          to={
            currentUserId === post.author?.userId
              ? "/dashboard"
              : `/profile/${post.author?.userId}`
          }
          className="author-name"
        >
          {authorName}
        </Link>
        {currentUserId === post.author?.userId && (
          <div
            className="post-actions"
            style={{ position: "absolute", right: "10px", top: "10px" }}
          >
            <button
              onClick={() => setShowActions(!showActions)}
              className="options-button"
            >
              Options
            </button>
            {showActions && (
              <div className="action-buttons">
                <button
                  onClick={() => setIsEditing(true)}
                  className="edit-button"
                >
                  Edit
                </button>
                <button onClick={handleDelete} className="delete-button">
                  Delete
                </button>
              </div>
            )}
          </div>
        )}
      </div>

      {isEditing ? (
        <textarea
          value={newText}
          onChange={(e) => setNewText(e.target.value)}
          rows={3}
          className="post-textarea"
        />
      ) : (
        <>
          <h5 className="mb-1">{post.text || "No text available"}</h5>
          <small className="text-muted">
            Likes: {likesCount} | Published on {publishedDate}
          </small>
        </>
      )}

      <div className="post-edit-actions">
        {isEditing ? (
          <>
            <button onClick={handleSave} className="save-button">
              Save
            </button>
            <button
              onClick={() => {
                setIsEditing(false);
              }}
              className="cancel-button"
            >
              Cancel
            </button>
          </>
        ) : (
          <div className="post-actions">
            <button onClick={() => onLike(post.id)} className="like-button">
              Like
            </button>
            <button
              onClick={() => onDislike(post.id)}
              className="dislike-button"
            >
              Dislike
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default PostItem;
