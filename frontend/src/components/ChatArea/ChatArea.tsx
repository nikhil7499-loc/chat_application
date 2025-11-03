import React, { useEffect, useRef, useState } from "react";
import styles from "./ChatArea.module.css";
import { useUserContext } from "../../context/UserContext";
import { useMessageTypesContext } from "../../context/MessageTypeContext";
import { BiArrowFromLeft, BiArrowFromTop } from "react-icons/bi";
import { FaEye } from "react-icons/fa";
import logoSvg from "../../assets/logo.svg";

export interface ChatUser {
  id: string;
  username: string;
  profilePicture?: string | null;
  isOnline?: boolean;
}

export interface ChatMessage {
  id: string;
  fromSelf?: boolean;
  text?: string; // includes caption or plain text
  time?: string;
  fileName?: string;
  status?: "sent" | "delivered" | "read";
  typeId?: number;
}

interface ChatAreaProps {
  activeUser?: ChatUser | null;
  initialMessages?: ChatMessage[];
  onSend?: (msg: { text?: string; file?: File | null; typeId: number }) => Promise<void> | void;
}

export default function ChatArea({
  activeUser = null,
  initialMessages = [],
  onSend,
}: ChatAreaProps) {
  const { getProfilePictureUrl } = useUserContext();
  const { messageTypes, loading: msgTypeLoading } = useMessageTypesContext();

  const [messages, setMessages] = useState<ChatMessage[]>(
    initialMessages.length
      ? initialMessages
      : [
          { id: "m1", fromSelf: false, text: "Hey! 👋", time: "10:01 AM" },
          { id: "m2", fromSelf: true, text: "Hi — how's it going?", time: "10:02 AM", status: "read" },
        ]
  );

  const [text, setText] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [caption, setCaption] = useState(""); // 👈 NEW: caption input
  const [sending, setSending] = useState(false);
  const [isTyping, setIsTyping] = useState(false);
  const [selectedTypeId, setSelectedTypeId] = useState<number>(1);

  const listRef = useRef<HTMLDivElement | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const profilePictureSrc = activeUser?.profilePicture
    ? getProfilePictureUrl(activeUser.profilePicture)
    : getProfilePictureUrl("default-avatar.png");

  useEffect(() => {
    const el = listRef.current;
    if (el) el.scrollTo({ top: el.scrollHeight, behavior: "smooth" });
  }, [messages]);

  // Simulated typing
  useEffect(() => {
    if (!activeUser) return;
    const typingInterval = setInterval(() => {
      setIsTyping(true);
      setTimeout(() => {
        setIsTyping(false);
        const reply: ChatMessage = {
          id: `auto-${Date.now()}`,
          fromSelf: false,
          text: ["That's cool 😎", "Really?", "Tell me more!", "Haha!", "😂"][
            Math.floor(Math.random() * 5)
          ],
          time: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
        };
        setMessages((m) => [...m, reply]);
      }, 3000);
    }, 15000);
    return () => clearInterval(typingInterval);
  }, [activeUser]);

  const selectedType = messageTypes.find((t) => t.id === selectedTypeId);

  const handleSend = async () => {
    const isText = selectedType?.name === "text";

    if (isText && !text.trim()) return;
    if (!isText && !file) return;

    const payload = {
      text: isText ? text.trim() : caption.trim() || undefined,
      file: !isText ? file : null,
      typeId: selectedTypeId,
    };

    const next: ChatMessage = {
      id: `m-${Date.now()}`,
      fromSelf: true,
      text: payload.text,
      fileName: payload.file?.name,
      time: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
      status: "sent",
      typeId: selectedTypeId,
    };

    setMessages((m) => [...m, next]);
    setText("");
    setCaption("");
    setFile(null);
    if (fileInputRef.current) fileInputRef.current.value = "";

    // Simulate delivery and read
    setTimeout(() => {
      setMessages((prev) =>
        prev.map((msg) => (msg.id === next.id ? { ...msg, status: "delivered" } : msg))
      );
    }, 2000);
    setTimeout(() => {
      setMessages((prev) =>
        prev.map((msg) => (msg.id === next.id ? { ...msg, status: "read" } : msg))
      );
    }, 5000);

    if (onSend) {
      try {
        setSending(true);
        await onSend(payload);
      } finally {
        setSending(false);
      }
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const f = e.target.files?.[0] ?? null;
    if (!f || !selectedType?.allowedExtensions) return;

    const allowed = selectedType.allowedExtensions.split(",").map((x) => x.trim().toLowerCase());
    const ext = f.name.split(".").pop()?.toLowerCase();
    if (!ext || !allowed.includes(ext)) {
      alert(`Invalid file type. Allowed: ${allowed.join(", ")}`);
      e.target.value = "";
      return;
    }
    setFile(f);
  };

  const renderStatus = (status?: string) => {
    if (!status) return null;
    if (status === "sent") return <span className={styles.singleTick}><BiArrowFromLeft /></span>;
    if (status === "delivered") return <span className={styles.doubleTick}><BiArrowFromTop /></span>;
    if (status === "read") return <span className={styles.doubleTickRead}><FaEye /></span>;
  };

  if (msgTypeLoading) {
    return (
      <div className={styles.emptyState}>
        <div className={styles.emptyInner}>
          <img src={logoSvg} alt="ChatApp" className={styles.emptyIcon} />
          <h3>Loading message types...</h3>
        </div>
      </div>
    );
  }

  return (
    <>
      {!activeUser ? (
        <div className={styles.emptyState}>
          <div className={styles.emptyInner}>
            <img src={logoSvg} alt="Chat illustration" className={styles.emptyIcon} />
            <h2>Welcome to ChatApp</h2>
            <p>Select a conversation from the sidebar to start chatting.</p>
          </div>
        </div>
      ) : (
        <div className={styles.container}>
          <header className={styles.header}>
            <div className={styles.userBadge}>
              <div className={styles.avatar}>
                <img
                  src={profilePictureSrc}
                  alt={activeUser.username}
                  onError={(ev) =>
                    (ev.currentTarget.src = getProfilePictureUrl("default-avatar.png"))
                  }
                />
                {activeUser.isOnline && <span className={styles.onlineDot}></span>}
              </div>
              <div className={styles.userInfo}>
                <div className={styles.username}>{activeUser.username}</div>
                <div className={styles.status}>
                  {isTyping ? "Typing..." : activeUser.isOnline ? "Online" : "Offline"}
                </div>
              </div>
            </div>
          </header>

          <div className={styles.messages} ref={listRef}>
            {messages.map((m) => (
              <div
                key={m.id}
                className={`${styles.messageRow} ${m.fromSelf ? styles.outgoing : styles.incoming}`}
              >
                <div className={styles.messageBubble}>
                  {m.fileName && <div className={styles.fileChip}>{m.fileName}</div>}
                  {m.text && <div className={styles.messageText}>{m.text}</div>}
                  <div className={styles.messageMeta}>
                    <span className={styles.messageTime}>{m.time ?? ""}</span>
                    {m.fromSelf && renderStatus(m.status)}
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className={styles.composer}>
            {file && (
              <div className={styles.filePreview}>
                <span className={styles.fileName}>{file.name}</span>
                <button
                  className={styles.removeFile}
                  onClick={() => {
                    setFile(null);
                    setCaption("");
                    if (fileInputRef.current) fileInputRef.current.value = "";
                  }}
                >
                  ✕
                </button>
                {/* 👇 Caption input for uploaded file */}
                <input
                  type="text"
                  className={styles.captionInput}
                  placeholder="Add a caption..."
                  value={caption}
                  onChange={(e) => setCaption(e.target.value)}
                />
              </div>
            )}

            <div className={styles.controls}>
              <select
                value={selectedTypeId}
                onChange={(e) => setSelectedTypeId(Number(e.target.value))}
                className={styles.typeSelect}
              >
                {messageTypes.map((mt) => (
                  <option key={mt.id} value={mt.id}>
                    {mt.name.charAt(0).toUpperCase() + mt.name.slice(1)}
                  </option>
                ))}
              </select>

              {selectedType?.name === "text" ? (
                <input
                  value={text}
                  onChange={(e) => setText(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && handleSend()}
                  className={styles.input}
                  placeholder={`Message ${activeUser.username}...`}
                  disabled={!activeUser}
                />
              ) : (
                <>
                  <input
                    ref={fileInputRef}
                    type="file"
                    className={styles.fileInput}
                    onChange={handleFileChange}
                    accept={
                      selectedType?.allowedExtensions
                        ?.split(",")
                        .map((x) => `.${x.trim()}`)
                        .join(",") || ""
                    }
                  />
                  <button
                    type="button"
                    className={styles.attachBtn}
                    onClick={() => fileInputRef.current?.click()}
                  >
                    📎
                  </button>
                </>
              )}

              <button
                type="button"
                className={styles.sendBtn}
                onClick={handleSend}
                disabled={!activeUser || sending}
              >
                {sending ? "..." : "Send"}
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
