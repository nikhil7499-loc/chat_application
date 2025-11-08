import { useEffect, useRef, useState } from "react";
import { useMessageContext } from "../../context/MessageContext";
import { useUserContext } from "../../context/UserContext";
import { useMessageTypesContext } from "../../context/MessageTypeContext";
import styles from "./ChatArea.module.css";
import { API_BASE_URL } from "../../services/base";

interface ChatAreaProps {
  selectedUserId: string | null;
  selectedUsername?: string;
}

export default function ChatArea({ selectedUserId, selectedUsername }: ChatAreaProps) {
  const { user } = useUserContext();
  const {
    messages,
    getDirectMessages,
    sendMessage,
    markDeliveredServer,
    markReadServer,   // ✅ NEW
    loading,
    error,
  } = useMessageContext();

  const { messageTypes, loading: typesLoading, error: typesError } =
    useMessageTypesContext();

  const [chatMessages, setChatMessages] = useState(messages);
  const [input, setInput] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [caption, setCaption] = useState("");
  const [messageType, setMessageType] = useState("text");
  const fileInputRef = useRef<HTMLInputElement>(null);
  const bottomRef = useRef<HTMLDivElement>(null);

  /** ✅ Load messages AND mark them delivered */
  useEffect(() => {
    if (!selectedUserId) return;

    void (async () => {
      const msgs = await getDirectMessages(selectedUserId);
      setChatMessages(msgs);

      // ✅ When you open a chat => mark all messages from THEM as delivered
      await markDeliveredServer(selectedUserId);
    })();
  }, [selectedUserId]);

  /** ✅ Mark messages as READ when new incoming messages appear */
  useEffect(() => {
    if (!selectedUserId) return;

    const hasUnread = chatMessages.some(
      (m) => m.sender.id === selectedUserId && !m.readAt
    );

    if (hasUnread) {
      // ✅ Tell server messages are read
      void markReadServer(selectedUserId);
    }
  }, [chatMessages, selectedUserId]);

  /** 🔹 Auto scroll */
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [chatMessages]);

  /** ✅ Allowed extensions */
  const allowedExtensions = messageTypes.find(
    (t) => t.name.toLowerCase() === messageType
  )?.allowedExtensions;

  const isValidFile = (filename: string) => {
    if (!allowedExtensions) return true;
    const ext = filename.split(".").pop()?.toLowerCase();
    return allowedExtensions.split(",").map((x) => x.trim()).includes(ext || "");
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selected = e.target.files?.[0];
    if (!selected) return;

    if (!isValidFile(selected.name)) {
      alert("This file type is not allowed for selected message type.");
      return;
    }

    setFile(selected);
  };

  /** ✅ Send message */
  const handleSend = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedUserId) return;
    if (messageType === "text" && !input.trim()) return;
    if (messageType !== "text" && !file) return;

    try {
      const requestPayload = {
        receiverId: selectedUserId,
        groupId: null,
        replyToMessageId: null,
        caption: caption || "",
        type: messageType,
        content: messageType === "text" ? input.trim() : caption,
      };

      const sent = await sendMessage(requestPayload, file);

      setChatMessages((prev) => [...prev, sent]);

      setInput("");
      setCaption("");
      setFile(null);
      if (fileInputRef.current) fileInputRef.current.value = "";
    } catch (err) {
      console.error("Send failed", err);
    }
  };

  if (!selectedUserId) {
    return (
      <div className={styles.emptyState}>
        <div className={styles.emptyInner}>
          <img src="/chat-empty.svg" alt="No chat" className={styles.emptyIcon} />
          <h2>No Chat Selected</h2>
          <p>Select a user from the left panel to start messaging.</p>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.userBadge}>
          <div className={styles.avatar}>
            {selectedUsername?.charAt(0).toUpperCase()}
            <div className={styles.onlineDot}></div>
          </div>
          <div className={styles.userInfo}>
            <div className={styles.username}>{selectedUsername}</div>
            <div className={styles.status}>Online</div>
          </div>
        </div>
      </div>

      {/* Messages */}
      <div className={styles.messages}>
        {loading ? (
          <p>Loading messages...</p>
        ) : error ? (
          <p style={{ color: "red" }}>{error}</p>
        ) : chatMessages.length === 0 ? (
          <p>No messages yet. Say hello 👋</p>
        ) : (
          chatMessages.map((msg) => (
            <div
              key={msg.id}
              className={`${styles.messageRow} ${
                msg.sender.id === user?.id ? styles.outgoing : styles.incoming
              }`}
            >
              <div className={styles.messageBubble}>
                {msg.type !== "text" && (
                  <a
                    href={`${API_BASE_URL}/api/messages/file/${msg.id}`}
                    target="_blank"
                    rel="noreferrer"
                    className={styles.mediaLink}
                  >
                    📎 Download file
                  </a>
                )}

                {msg.type === "text" && (
                  <div className={styles.messageText}>{msg.content}</div>
                )}

                {msg.caption && (
                  <div className={styles.captionText}>{msg.caption}</div>
                )}

                {/* ✅ Tick system */}
                <div className={styles.messageMeta}>
                  {/* ✅ Sent only */}
                  {msg.sender.id === user?.id &&
                    !msg.deliveredAt &&
                    !msg.readAt && <span className={styles.singleTick}>✓</span>}

                  {/* ✅ Delivered but not read */}
                  {msg.sender.id === user?.id &&
                    msg.deliveredAt &&
                    !msg.readAt && (
                      <span className={styles.deliveredTick}>✓✓</span>
                    )}

                  {/* ✅ Read */}
                  {msg.sender.id === user?.id && msg.readAt && (
                    <span
                      className={`${styles.doubleTick} ${styles.doubleTickRead}`}
                    >
                      ✓✓
                    </span>
                  )}
                </div>
              </div>
            </div>
          ))
        )}

        <div ref={bottomRef}></div>
      </div>

      {/* Composer */}
      <form onSubmit={handleSend} className={styles.composer}>
        {file && (
          <div className={styles.filePreview}>
            <span>{file.name}</span>
            <button type="button" onClick={() => setFile(null)}>
              ✕
            </button>
          </div>
        )}

        {file && (
          <input
            type="text"
            className={styles.captionInput}
            placeholder="Add a caption..."
            value={caption}
            onChange={(e) => setCaption(e.target.value)}
          />
        )}

        <div className={styles.controls}>
          {!["text", "system"].includes(messageType) && (
            <>
              <button
                type="button"
                className={styles.attachBtn}
                onClick={() => fileInputRef.current?.click()}
              >
                📎
              </button>
              <input
                ref={fileInputRef}
                type="file"
                className={styles.fileInput}
                onChange={handleFileChange}
              />
            </>
          )}

          <select
            value={messageType}
            onChange={(e) => setMessageType(e.target.value)}
            className={styles.typeSelect}
            disabled={typesLoading}
          >
            {typesError && <option>Error loading types</option>}
            {!typesError &&
              messageTypes.map((type) => (
                <option key={type.id} value={type.name.toLowerCase()}>
                  {type.name}
                </option>
              ))}
          </select>

          <input
            type="text"
            className={styles.input}
            placeholder="Type a message..."
            value={input}
            disabled={messageType !== "text"}
            onChange={(e) => setInput(e.target.value)}
          />

          <button type="submit" className={styles.sendBtn}>
            Send
          </button>
        </div>
      </form>
    </div>
  );
}
