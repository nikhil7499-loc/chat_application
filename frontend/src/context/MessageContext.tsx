import {
  createContext,
  useContext,
  useEffect,
  useState,
  useCallback,
} from "react";
import type { ReactNode } from "react";
import { MessageAPI } from "../services/message";
import type {
  MessageResponse,
  KnownConnectionResponse,
  SendMessageRequest,
  ReplyMessageRequest,
} from "../services/message";
import { useLoader } from "../common/LoaderContext";

// Context Type
interface MessageContextType {
  messages: MessageResponse[];
  connections: KnownConnectionResponse[];
  loading: boolean;
  error: string | null;

  // === Message Actions ===
  sendMessage: (
    data: SendMessageRequest,
    file?: File | null
  ) => Promise<MessageResponse>;
  replyToMessage: (
    messageId: string,
    data: ReplyMessageRequest
  ) => Promise<void>;
  deleteMessage: (messageId: string) => Promise<void>;

  // === Conversation Actions ===
  getDirectMessages: (otherUserId: string) => Promise<MessageResponse[]>;
  getConnections: () => Promise<void>;

  // === User Actions ===
  blockUser: (contactId: string) => Promise<void>;
  unblockUser: (contactId: string) => Promise<void>;
  searchUser: (username: string) => Promise<any[]>;

  // === Status Updates ===
  markAsDelivered: (messageId: string) => void;
  markAsRead: (messageId: string) => void;

  // ✅ NEW – server-side updates
  markDeliveredServer: (otherUserId: string) => Promise<void>;
  markReadServer: (otherUserId: string) => Promise<void>;

  refreshAll: () => Promise<void>;
}

const MessageContext = createContext<MessageContextType | undefined>(
  undefined
);

export const MessageProvider = ({ children }: { children: ReactNode }) => {
  const [messages, setMessages] = useState<MessageResponse[]>([]);
  const [connections, setConnections] = useState<KnownConnectionResponse[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const loader = useLoader();

  /** Load connections on mount */
  useEffect(() => {
    void getConnections();
  }, []);

  /** ✅ Send Message (text or media) */
  const sendMessage = async (
    data: SendMessageRequest,
    file?: File | null
  ): Promise<MessageResponse> => {
    try {
      setError(null);
      loader(true);

      const newMsg = await MessageAPI.sendMessage(data, file);

      setMessages((prev) => [...prev, newMsg]);
      await getConnections();

      return newMsg;
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to send message");
      throw err;
    } finally {
      loader(false);
    }
  };

  /** ✅ Reply Message */
  const replyToMessage = async (
    messageId: string,
    data: ReplyMessageRequest
  ) => {
    try {
      setError(null);
      loader(true);

      const reply = await MessageAPI.replyToMessage(messageId, data);

      setMessages((prev) => [...prev, reply]);
      await getConnections();
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to send reply");
    } finally {
      loader(false);
    }
  };

  /** ✅ Get messages with another user */
  const getDirectMessages = async (
    otherUserId: string
  ): Promise<MessageResponse[]> => {
    try {
      setError(null);
      setLoading(true);
      loader(true);

      const msgs = await MessageAPI.getDirectMessages(otherUserId);
      setMessages(msgs);

      return msgs;
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to load messages");
      return [];
    } finally {
      setLoading(false);
      loader(false);
    }
  };

  /** ✅ Get known connections */
  const getConnections = useCallback(async () => {
    try {
      setError(null);
      setLoading(true);

      const cons = await MessageAPI.getConnections();
      setConnections(cons);
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to fetch connections");
    } finally {
      setLoading(false);
    }
  }, []);

  /** ✅ Block user */
  const blockUser = async (contactId: string) => {
    try {
      loader(true);
      await MessageAPI.blockUser(contactId);
      await getConnections();
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to block user");
    } finally {
      loader(false);
    }
  };

  /** ✅ Unblock user */
  const unblockUser = async (contactId: string) => {
    try {
      loader(true);
      await MessageAPI.unblockUser(contactId);
      await getConnections();
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to unblock user");
    } finally {
      loader(false);
    }
  };

  /** ✅ Search for user */
  const searchUser = async (username: string) => {
    try {
      setError(null);
      return await MessageAPI.searchUser(username);
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Search failed");
      return [];
    }
  };

  /** ✅ Delete a message */
  const deleteMessage = async (messageId: string) => {
    try {
      loader(true);
      await MessageAPI.deleteMessage(messageId);

      setMessages((prev) => prev.filter((m) => m.id !== messageId));
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to delete message");
    } finally {
      loader(false);
    }
  };

  /** ✅ Local delivered state */
  const markAsDelivered = (messageId: string) => {
    setMessages((prev) =>
      prev.map((msg) =>
        msg.id === messageId
          ? { ...msg, deliveredAt: new Date().toISOString() }
          : msg
      )
    );
  };

  /** ✅ Local read state */
  const markAsRead = (messageId: string) => {
    setMessages((prev) =>
      prev.map((msg) =>
        msg.id === messageId
          ? { ...msg, readAt: new Date().toISOString() }
          : msg
      )
    );
  };

  // ✅ NEW — server-side delivered update
  const markDeliveredServer = async (otherUserId: string) => {
    try {
      await MessageAPI.markDelivered(otherUserId);

      // Local optimistic update
      setMessages((prev) =>
        prev.map((msg) =>
          msg.sender.id === otherUserId && !msg.deliveredAt
            ? { ...msg, deliveredAt: new Date().toISOString() }
            : msg
        )
      );
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to mark as delivered");
    }
  };

  // ✅ NEW — server-side READ update
  const markReadServer = async (otherUserId: string) => {
    try {
      await MessageAPI.markRead(otherUserId);

      setMessages((prev) =>
        prev.map((msg) =>
          msg.sender.id === otherUserId && !msg.readAt
            ? { ...msg, readAt: new Date().toISOString() }
            : msg
        )
      );
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data || "Failed to mark as read");
    }
  };

  /** ✅ Refresh everything */
  const refreshAll = async () => {
    await getConnections();
    if (messages.length > 0 && messages[0].receiver) {
      await getDirectMessages(messages[0].receiver.id);
    }
  };

  const value: MessageContextType = {
    messages,
    connections,
    loading,
    error,

    sendMessage,
    replyToMessage,
    deleteMessage,

    getDirectMessages,
    getConnections,

    blockUser,
    unblockUser,
    searchUser,

    markAsDelivered,
    markAsRead,

    // ✅ NEW
    markDeliveredServer,
    markReadServer,

    refreshAll,
  };

  return (
    <MessageContext.Provider value={value}>
      {children}
    </MessageContext.Provider>
  );
};

// Hook
export const useMessageContext = (): MessageContextType => {
  const ctx = useContext(MessageContext);
  if (!ctx)
    throw new Error("useMessageContext must be used within a MessageProvider");
  return ctx;
};
