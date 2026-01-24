import { createContext, useContext, useEffect, useState } from "react";
import { MessageType } from "../services/message_types";

const MessageTypeContext = createContext();

export const MessageTypeProvider = ({ children }) => {
  const [messageTypes, setMessageTypes] = useState(null);
  const [loading, setLoading] = useState(false);

  const value = {
    messageTypes,
    loading,
  };
  const getall = () => {
    setLoading(true);
    MessageType.getAllType().then((dt) => {
      setMessageTypes(dt);
    }).catch((err)=>{
        console.log(err);
    }).finally(()=>{
        setLoading(false);
    });
  };

  useEffect(() => {
    getall();
  }, []);

  return (
    <MessageTypeContext.Provider value={value}>
      {children}
    </MessageTypeContext.Provider>
  );
};

export const useMessageTypeContext = () => {
  const ctx = useContext(MessageTypeContext);
  return ctx;
};
