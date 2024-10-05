import React, { useState } from "react";
import { Post } from "../interfaces/Post"; // Assuming you have defined the Post interface
import "../styles/PostItem.css"; // Ensure your CSS styles are defined here

interface PostItemProps {
  post: Post;
  currentUserId: number | null;
  onLike: (postId: number) => void;
  onDislike: (postId: number) => void;
  onEdit: (postId: number, newText: string) => void; // Function to handle edit
  onDelete: (postId: number) => void; // Function to handle delete
}

const PostItem: React.FC<PostItemProps> = ({
  post,
  currentUserId,
  onLike,
  onDislike,
  onEdit,
  onDelete,
}) => {
  const [isEditing, setIsEditing] = useState(false); // State for editing mode
  const [newText, setNewText] = useState(post.text || ""); // State for new text
  const [showActions, setShowActions] = useState(false); // State to show edit/delete buttons

  // Function to handle saving the edited post
  const handleSave = () => {
    if (newText.trim()) {
      onEdit(post.id, newText); // Call the edit function
      setIsEditing(false); // Exit editing mode
    }
  };

  // Function to handle deleting the post
  const handleDelete = () => {
    onDelete(post.id); // Call the delete function without confirmation
  };

  // If post is not available
  if (!post) {
    return <div>No content available</div>;
  }

  // Get author name and published date
  const authorName = post.author?.username ?? `User ID: ${post.authorId}`;
  const likesCount = post.likes?.length || 0;
  const publishedDate = post.epochSecond
    ? new Date(post.epochSecond * 1000).toLocaleString()
    : "Date not available";

  return (
    <div className="list-group-item post-item" style={{ position: "relative" }}>
      <div className="post-header">
        <div className="author-name">{authorName}</div>
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
        <>
          <textarea
            value={newText}
            onChange={(e) => setNewText(e.target.value)} // Update new text
            rows={3}
            className="post-textarea"
          />
          <div className="post-edit-actions">
            <button onClick={handleSave} className="save-button">
              Save
            </button>
            <button
              onClick={() => {
                setIsEditing(false); // Exit editing mode
              }}
              className="cancel-button"
            >
              Cancel
            </button>
          </div>
        </>
      ) : (
        <>
          <h5 className="mb-1">{post.text || "No text available"}</h5>
          <small className="text-muted">
            Likes: {likesCount} | Published on {publishedDate}
          </small>
        </>
      )}

      <div className="post-actions">
        <button onClick={() => onLike(post.id)} className="like-button">
          Like
        </button>
        <button onClick={() => onDislike(post.id)} className="dislike-button">
          Dislike
        </button>
      </div>
    </div>
  );
};

export default PostItem;
