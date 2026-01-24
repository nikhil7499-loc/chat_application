import { api } from "./base"

export const MessageType={
    async getAllType(){
    const res=  await api.get("/message-types/all");
    return res.data;
    }
}