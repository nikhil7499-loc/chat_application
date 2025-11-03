import { createContext, useContext, useEffect, useState } from "react";
import type { ReactNode } from "react";
import { MessageTypesAPI } from "../services/messageTypes";
import type { MessageTypes } from "../services/messageTypes";
import { useLoader } from "../common/LoaderContext";

interface MessageTypesContextType {
  messageTypes: MessageTypes[];
  loading: boolean;
  error: string | null;
  refreshMessageTypes: () => Promise<void>;
  getMessageTypeById: (id: number) => MessageTypes | undefined;
  getMessageTypeByName: (name: string) => MessageTypes | undefined;
}

const MessageTypesContext = createContext<MessageTypesContextType | undefined>(undefined);

export const MessageTypesProvider = ({ children }: { children: ReactNode }) => {
  const [messageTypes, setMessageTypes] = useState<MessageTypes[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const loader = useLoader();

  useEffect(() => {
    const cached = localStorage.getItem("messageTypes");
    if (cached) {
      try {
        const parsed: MessageTypes[] = JSON.parse(cached);
        setMessageTypes(parsed);
        setLoading(false);
        // Try background refresh silently
        refreshMessageTypes(parsed);
      } catch {
        refreshMessageTypes();
      }
    } else {
      refreshMessageTypes();
    }
  }, []);

  const refreshMessageTypes = async (currentCache?: MessageTypes[]) => {
    try {
      setError(null);
      setLoading(true);
      loader(true); // ✅ show global spinner

      const fetched = await MessageTypesAPI.getAll();

      // Only update if data has changed
      if (JSON.stringify(fetched) !== JSON.stringify(currentCache || messageTypes)) {
        setMessageTypes(fetched);
        localStorage.setItem("messageTypes", JSON.stringify(fetched));
      }
    } catch (err: any) {
      console.error("Failed to load message types:", err);
      setError(err.response?.data || "Failed to load message types");
    } finally {
      setLoading(false);
      loader(false); // ✅ hide global spinner
    }
  };

  const getMessageTypeById = (id: number) =>
    messageTypes.find((t) => t.id === id);

  const getMessageTypeByName = (name: string) =>
    messageTypes.find((t) => t.name.toLowerCase() === name.toLowerCase());

  const value: MessageTypesContextType = {
    messageTypes,
    loading,
    error,
    refreshMessageTypes,
    getMessageTypeById,
    getMessageTypeByName,
  };

  return (
    <MessageTypesContext.Provider value={value}>
      {children}
    </MessageTypesContext.Provider>
  );
};

export const useMessageTypesContext = (): MessageTypesContextType => {
  const ctx = useContext(MessageTypesContext);
  if (!ctx) throw new Error("useMessageTypesContext must be used within a MessageTypesProvider");
  return ctx;
};
