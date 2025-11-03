import { api } from "./base";

/** ---------- TypeScript Interfaces ---------- **/

export interface MessageTypes {
  id: number;
  name: string;
  description?: string | null;
  allowedExtensions?: string | null;
  created_at?: string | null;
}

/** ---------- API Layer ---------- **/

export const MessageTypesAPI = {
  /**
   * Fetch all available message types.
   * GET /message-types
   */
  getAll: async (): Promise<MessageTypes[]> => {
    const res = await api.get("/message-types");
    const allTypes = res.data as MessageTypes[];

    // 🔹 Filter out the "system" message type (case-insensitive)
    return allTypes.filter(
        (t) => t.name.toLowerCase() !== "system"
    );
  },

  /**
   * Fetch a message type by its numeric ID.
   * GET /message-types/{id}
   */
  getById: async (id: number): Promise<MessageTypes> => {
    const res = await api.get(`/message-types/${id}`);
    return res.data;
  },

  /**
   * Fetch a message type by its name (e.g. 'text', 'image').
   * GET /message-types/name/{name}
   */
  getByName: async (name: string): Promise<MessageTypes> => {
    const res = await api.get(`/message-types/name/${name}`);
    return res.data;
  },
};
