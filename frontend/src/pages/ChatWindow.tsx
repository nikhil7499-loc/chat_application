import { useState } from "react";
import UsersPanel from "../components/UsersPanel/UsersPanel";
import ChatArea from "../components/ChatArea/ChatArea";
import styles from "./PageStyle.module.css";
import type { ChatUser } from "../components/ChatArea/ChatArea";
import { MessageTypesProvider } from "../context/MessageTypeContext";

export default function ChatWindow() {
  const [activeUser, setActiveUser] = useState<ChatUser | null>(null);

  return (
    <div className={styles.chatCont}>
      {/* 🔹 Left side – User list */}
      <UsersPanel onSelectUser={setActiveUser} selectedUserId={activeUser?.id} />

      {/* 🔹 Right side – Chat area */}
      <MessageTypesProvider>
        <ChatArea
          activeUser={activeUser}
          onSend={async ({ text, file }) => {
            console.log("📨 Sent message to", activeUser?.username, { text, file });
          }}
        />
      </MessageTypesProvider>
    </div>
  );
}
