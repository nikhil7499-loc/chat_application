import { useState, useEffect, useRef, useMemo } from "react";
import styles from "./UsersPanel.module.css";
import { useUserContext } from "../../context/UserContext";
import { useMessageContext } from "../../context/MessageContext";
import UpdateProfile from "./UpdateProfile";

interface UsersPanelProps {
  onSelectUser?: (userId: string) => void;
  selectedUserId?: string | null;
}

export default function UsersPanel({ onSelectUser, selectedUserId }: UsersPanelProps) {
  const { logout, user, getProfilePictureUrl } = useUserContext();
  const {
    connections,
    searchUser,
    getConnections,
    loading,
    error,
  } = useMessageContext();

  const [searchInput, setSearchInput] = useState("");
  const [filter, setFilter] = useState<"all" | "unread" | "read" | "group" | "favourite">("all");
  const [menuOpen, setMenuOpen] = useState(false);
  const [showProfilePopup, setShowProfilePopup] = useState(false);
  const [searchResults, setSearchResults] = useState<any[]>([]);
  const menuRef = useRef<HTMLDivElement>(null);

  /** 🔹 Load known connections on mount */
  useEffect(() => {
    void getConnections();
  }, [getConnections]);

  /** 🔹 Handle outside click & ESC for menu */
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

  /** 🔹 Disable background scroll when profile popup is open */
  useEffect(() => {
    document.body.style.overflow = showProfilePopup ? "hidden" : "";
  }, [showProfilePopup]);

  /** 🔍 Search users dynamically */
  useEffect(() => {
    const delayDebounce = setTimeout(async () => {
      if (searchInput.trim().length > 1) {
        const results = await searchUser(searchInput.trim());
        setSearchResults(results);
      } else {
        setSearchResults([]);
      }
    }, 400);
    return () => clearTimeout(delayDebounce);
  }, [searchInput, searchUser]);

  /** 🧭 Normalize backend connections for UI */
  const mappedConnections = useMemo(() => {
    return connections
      .map((conn) => ({
        id: conn.contact.id,
        username: conn.contact.username,
        name: conn.contact.name,
        profilePicture: conn.contact.profilePicture,
        lastMessageAt: conn.lastMessageAt,
        isFavorite: conn.isFavorite,
        isBlocked: conn.isBlocked,
        unreadCount: conn.unreadCount ?? 0,
        blockedByUserId: conn.blockedByUserId,
        blockedAt: conn.blockedAt,
      }))
      // 🕒 Sort by last message (most recent first)
      .sort(
        (a, b) =>
          new Date(b.lastMessageAt || 0).getTime() -
          new Date(a.lastMessageAt || 0).getTime()
      );
  }, [connections]);

  /** 🔹 Apply filters on mapped connections or search results */
  const getFilteredUsers = () => {
    const list = searchInput ? searchResults : mappedConnections;
    if (!list) return [];

    return list.filter((u: any) => {
      const username = u.username || "";
      const matchesSearch = username.toLowerCase().includes(searchInput.toLowerCase());
      if (!matchesSearch) return false;

      switch (filter) {
        case "unread":
          return u.unreadCount > 0;
        case "favourite":
          return u.isFavorite;
        default:
          return true;
      }
    });
  };

  const filteredUsers = getFilteredUsers();

  return (
    <>
      {/* 🔹 Profile Update Popup */}
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
              className={`${styles.filterButton} ${
                filter === type ? styles.activeFilter : ""
              }`}
              onClick={() => setFilter(type as any)}
            >
              {type.charAt(0).toUpperCase() + type.slice(1)}
            </button>
          ))}
        </div>

        {/* 🔹 Users List */}
        <div className={styles.usersList}>
          {loading && <p className={styles.loadingText}>Loading users...</p>}
          {error && <p className={styles.errorText}>{error}</p>}

          {!loading && filteredUsers.length > 0 ? (
            filteredUsers.map((u: any) => (
              <div
                key={u.id}
                className={`${styles.userItem} ${
                  selectedUserId === u.id ? styles.active : ""
                }`}
                onClick={() => onSelectUser?.(u.id)}
              >
                <div className={styles.avatar}>
                  <img
                    src={
                      getProfilePictureUrl(u.profilePicture || "default-avatar.png") ||
                      "/default-avatar.png"
                    }
                    alt={u.username}
                    onError={(e) =>
                      ((e.target as HTMLImageElement).src = "/default-avatar.png")
                    }
                  />
                </div>

                <div className={styles.userInfo}>
                  <span className={styles.username}>{u.name || u.username}</span>
                  {u.lastMessageAt && (
                    <span className={styles.lastMessage}>
                      Last message:{" "}
                      {new Date(u.lastMessageAt).toLocaleString([], {
                        hour: "2-digit",
                        minute: "2-digit",
                        month: "short",
                        day: "numeric",
                      })}
                    </span>
                  )}
                </div>

                {u.unreadCount > 0 && (
                  <div className={styles.notificationDot}></div>
                )}
              </div>
            ))
          ) : (
            !loading && (
              <p className={styles.emptyText}>
                {searchInput ? "No users found." : "No conversations yet."}
              </p>
            )
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
