import { useState, useEffect, useRef } from "react";
import styles from "./UsersPanel.module.css";
import { useUserContext } from "../../context/UserContext";
import UpdateProfile from "./UpdateProfile";

interface UserItem {
  id: string;
  username: string;
  lastMessage?: string;
  hasNotification?: boolean;
  isGroup?: boolean;
  isFavourite?: boolean;
  isOnline?: boolean;
}

interface UsersPanelProps {
  onSelectUser?: (user: UserItem) => void;
  selectedUserId?: string | null;
}

export default function UsersPanel({ onSelectUser, selectedUserId }: UsersPanelProps) {
  const { logout, user, getProfilePictureUrl } = useUserContext();

  const dummyUsers: UserItem[] = [
    { id: "1", username: "Alice", lastMessage: "Hey! How are you?", hasNotification: true, isOnline: true },
    { id: "2", username: "Bob", lastMessage: "Let's meet tomorrow.", isFavourite: true },
    { id: "3", username: "Charlie", lastMessage: "Got it, thanks!", isOnline: true },
    { id: "4", username: "Team Dev", lastMessage: "New commit pushed!", isGroup: true },
    { id: "5", username: "Eve", lastMessage: "Check this out 👀", hasNotification: true },
  ];

  const [users] = useState<UserItem[]>(dummyUsers);
  const [filter, setFilter] = useState<"all" | "unread" | "read" | "group" | "favourite">("all");
  const [searchInput, setSearchInput] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);
  const [showProfilePopup, setShowProfilePopup] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);

  // ✅ Close menu on outside click or ESC
  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        setMenuOpen(false);
      }
    };

    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === "Escape") {
        setMenuOpen(false);
        setShowProfilePopup(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    document.addEventListener("keydown", handleEsc);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
      document.removeEventListener("keydown", handleEsc);
    };
  }, []);

  // ✅ Disable scroll when profile popup is open
  useEffect(() => {
    document.body.style.overflow = showProfilePopup ? "hidden" : "";
  }, [showProfilePopup]);

  const filteredUsers = users.filter((u) => {
    const matchesSearch = u.username.toLowerCase().includes(searchInput.toLowerCase());
    if (!matchesSearch) return false;

    switch (filter) {
      case "unread":
        return u.hasNotification;
      case "read":
        return !u.hasNotification;
      case "group":
        return u.isGroup;
      case "favourite":
        return u.isFavourite;
      default:
        return true;
    }
  });

  return (
    <>
      {/* 🔹 Profile Update Popup Overlay */}
      {showProfilePopup && (
        <div className={styles.overlay}>
          <UpdateProfile onClose={() => setShowProfilePopup(false)} />
        </div>
      )}

      <div className={styles.panelContainer}>
        {/* 🔹 Filter Buttons */}
        <div className={styles.filterBar}>
          {["all", "unread", "read", "group", "favourite"].map((type) => (
            <button
              key={type}
              className={`${styles.filterButton} ${filter === type ? styles.activeFilter : ""}`}
              onClick={() => setFilter(type as any)}
            >
              {type.charAt(0).toUpperCase() + type.slice(1)}
            </button>
          ))}
        </div>

        {/* 🔹 Users List */}
        <div className={styles.usersList}>
          {filteredUsers.length > 0 ? (
            filteredUsers.map((u) => (
              <div
                key={u.id}
                className={`${styles.userItem} ${selectedUserId === u.id ? styles.active : ""}`}
                onClick={() => onSelectUser?.(u)}
              >
                <div className={styles.avatar}>
                  {u.username.charAt(0).toUpperCase()}
                  {u.isOnline && <div className={styles.onlineDot}></div>}
                </div>

                <div className={styles.userInfo}>
                  <span className={`${styles.username} ${u.isOnline ? styles.onlineUser : ""}`}>
                    {u.username}
                  </span>
                  {u.lastMessage && <span className={styles.lastMessage}>{u.lastMessage}</span>}
                </div>

                {u.hasNotification && <div className={styles.notificationDot}></div>}
              </div>
            ))
          ) : (
            <p className={styles.emptyText}>No users found</p>
          )}
        </div>

        {/* 🔹 Search + Menu */}
        <div className={styles.searchBox}>
          <input
            type="text"
            placeholder="Search by username..."
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            className={styles.searchInput}
          />

          <div className={styles.menuContainer} ref={menuRef}>
            <button
              type="button"
              className={styles.menuButton}
              onClick={() => setMenuOpen((prev) => !prev)}
            >
              ≔
            </button>

            {menuOpen && (
              <div className={styles.menuPopup}>
                <div
                  className={styles.userInfoPopup}
                  onClick={() => {
                    setMenuOpen(false);
                    setShowProfilePopup(true);
                  }}
                >
                  <img
                    src={
                      getProfilePictureUrl(user?.profilePicture || "default-avatar.png") ||
                      "/default-avatar.png"
                    }
                    alt="Profile"
                    className={styles.profileImage}
                    onError={(e) =>
                      ((e.target as HTMLImageElement).src = "/default-avatar.png")
                    }
                  />
                  <div className={styles.popupName}>{user?.name || "User"}</div>
                </div>

                <button onClick={() => alert("Create Group clicked")}>➕ Create Group</button>
                <button onClick={logout}>🚪 Logout</button>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
}
