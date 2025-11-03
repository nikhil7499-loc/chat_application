import axios from "axios";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";


const API_BASE_URL = import.meta.env.VITE_API_BASE_URL?.trim() || window.location.origin;
const WS_ENDPOINT = `${API_BASE_URL}/ws`; 

export const api = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  withCredentials: true,   
});

export const createSocketClient = () => {
  const stompClient = new Client({
    webSocketFactory: () => new SockJS(WS_ENDPOINT),
    reconnectDelay: 5000, 
    debug: (msg) => console.log(msg),
    connectHeaders: {
      Authorization: `Bearer ${localStorage.getItem("token") || ""}`,
    },
  });

  return stompClient;
};
