import { useState } from "react";
import styles from "./UpdateProfile.module.css";
import { useUserContext } from "../../context/UserContext";

interface UpdateProfileProps {
  onClose: () => void; // 🔹 parent passes this to close the popup
}

export default function UpdateProfile({ onClose }: UpdateProfileProps) {
  const { user, updateProfile, getProfilePictureUrl } = useUserContext();

  const [username, setUsername] = useState(user?.username || "");
  const [name, setName] = useState(user?.name || "");
  const [file, setFile] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(
    user?.profilePicture
      ? getProfilePictureUrl(user.profilePicture)
      : getProfilePictureUrl("default-avatar.png")
  );
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{ text: string; type: "success" | "error" } | null>(
    null
  );

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selected = e.target.files?.[0];
    if (selected) {
      setFile(selected);
      setPreview(URL.createObjectURL(selected));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    setLoading(true);

    try {
      await updateProfile({ username, name, file });
      setMessage({ text: "Profile updated successfully!", type: "success" });
    } catch (err: any) {
      setMessage({ text: err?.message || "Failed to update profile.", type: "error" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.formWrapper}>
        {/* 🔹 Close Button */}
        <button className={styles.closeButton} onClick={onClose} aria-label="Close popup">
          ×
        </button>

        <h2 className={styles.title}>Update Profile</h2>

        <form onSubmit={handleSubmit} className={styles.form}>
          {/* Profile Picture Section */}
          <div className={styles.profileSection}>
            <div className={styles.avatarWrapper}>
              <img
                src={preview || "/default-avatar.png"}
                alt="Profile Preview"
                className={styles.avatar}
                onError={(e) => ((e.target as HTMLImageElement).src = "/default-avatar.png")}
              />
              <label htmlFor="fileInput" className={styles.uploadLabel}>
                Change Photo
              </label>
              <input
                type="file"
                id="fileInput"
                accept="image/*"
                onChange={handleFileChange}
                className={styles.fileInput}
              />
            </div>
          </div>

          {/* Username Field */}
          <div className={styles.inputGroup}>
            <label>Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter new username"
            />
          </div>

          {/* Full Name Field */}
          <div className={styles.inputGroup}>
            <label>Full Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter your name"
            />
          </div>

          {/* Submit Button */}
          <button type="submit" className={styles.updateButton} disabled={loading}>
            {loading ? "Updating..." : "Update Profile"}
          </button>

          {/* Message */}
          {message && (
            <p
              className={`${styles.message} ${
                message.type === "success" ? styles.success : styles.error
              }`}
            >
              {message.text}
            </p>
          )}
        </form>
      </div>
    </div>
  );
}
