import { api } from "./base";

export interface SendMessageRequest {
  content?: string | null; 
  caption?: string | null;
  type: string;                 // must match message_types.name (db)
  receiverId?: string | null;
  groupId?: string | null;
  replyToMessageId?: string | null;
}

export interface ReplyMessageRequest {
  content: string;
  caption?: string | null;
}


export interface KnownConnectionResponse {
  id: string;

  contact: {
    id: string;
    username: string;
    name: string | null;
    profilePicture: string | null;
  };

  isBlocked: boolean;
  blockedByUserId?: string | null;
  blockedAt?: string | null;

  isFavorite: boolean;
  unreadCount: number;
  lastMessageAt?: string | null;
}


export interface MessageResponse {
  id: string;
  content: string;
  caption?: string | null;
  type: string;

  sender: {
    id: string;
    username: string;
    name?: string | null;
    profilePicture?: string | null;
  };

  receiver?: {
    id: string;
    username: string;
    name?: string | null;
    profilePicture?: string | null;
  } | null;

  group?: {
    id: string;
    name: string;
    description?: string | null;
  } | null;

  sentAt: string;
  deliveredAt?: string | null;
  readAt?: string | null;
}


export const MessageAPI = {

  /** ✅ Send a message with optional file (matches backend multipart/form-data) */
  sendMessage: async (
    request: SendMessageRequest,
    file?: File | null
  ): Promise<MessageResponse> => {

    const formData = new FormData();

    // ✅ Send request JSON as Blob (backend expects @RequestPart("request"))
    formData.append(
      "request",
      new Blob([JSON.stringify(request)], { type: "application/json" })
    );

    // ✅ Add file only for non-text messages
    if (file) {
      formData.append("file", file);
    }

    const res = await api.post("/messages/send", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    return res.data;
  },


  /** ✅ Reply only uses JSON (no file allowed by backend) */
  replyToMessage: async (
    messageId: string,
    data: ReplyMessageRequest
  ): Promise<MessageResponse> => {
    const res = await api.post(`/messages/reply/${messageId}`, data);
    return res.data;
  },


  /** ✅ Direct chat messages */
  getDirectMessages: async (otherUserId: string): Promise<MessageResponse[]> => {
    const res = await api.get(`/messages/direct/${otherUserId}`);
    return res.data;
  },


  /** ✅ Connections list */
  getConnections: async () => {
    const res = await api.get("/messages/connections");
    return res.data;
  },


  /** ✅ Search user */
  searchUser: async (username: string) => {
    const res = await api.get(`/messages/search`, { params: { username } });
    return res.data;
  },


  /** ✅ Download protected file (backend returns binary) */
  getFile: async (messageId: string): Promise<Blob> => {
    const res = await api.get(`/messages/file/${messageId}`, {
      responseType: "blob",
    });
    return res.data;
  },


  /** ✅ Block user */
  blockUser: async (contactId: string): Promise<string> => {
    const res = await api.post(`/messages/block/${contactId}`);
    return res.data;
  },

  /** ✅ Unblock user */
  unblockUser: async (contactId: string): Promise<string> => {
    const res = await api.post(`/messages/unblock/${contactId}`);
    return res.data;
  },

  /** ✅ Delete message */
  deleteMessage: async (messageId: string): Promise<string> => {
    const res = await api.delete(`/messages/${messageId}`);
    return res.data;
  },

  async markDelivered(data: { currentUserId: string; otherUserId: string }) {
    return api.post("/messages/mark-delivered", data);
  },

  async markRead(data: { currentUserId: string; otherUserId: string }) {
    return api.post("/messages/mark-read", data);
  }
};
