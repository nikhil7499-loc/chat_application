import { useState } from "react";
import ChatArea from "../components/ChatArea/ChatArea";
import UsersPanel from "../components/UsersPanel/UsersPanel";
import { MessageTypesProvider } from "../context/MessageTypeContext";
import styles from "./PageStyle.module.css";

export default function ChatWindow() {
  const [selectedUserId, setSelectedUserId] = useState<string | null>(null);
  const [selectedUsername, setSelectedUsername] = useState<string>("");

  /** 🔹 When a user is clicked in UsersPanel */
  const handleSelectUser = (userId: string, username?: string) => {
    setSelectedUserId(userId);
    if (username) setSelectedUsername(username);
  };

  return (
    <div className={styles.chatCont}>
      {/* Left panel: user list */}
      <UsersPanel
        onSelectUser={(userId) => {
          // Find the user object (optional if you want to show name)
          // You could modify UsersPanel’s onSelectUser to also pass username
          // But for now we’ll just pass id
          handleSelectUser(userId);
        }}
        selectedUserId={selectedUserId}
      />

      {/* Right panel: chat area */}
      <MessageTypesProvider>
        <ChatArea
          selectedUserId={selectedUserId}
          selectedUsername={selectedUsername}
        />
      </MessageTypesProvider>
    </div>
  );
}
