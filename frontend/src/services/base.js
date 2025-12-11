import axios from 'axios';

export const base_url = import.meta.env.VITE_BACKEND_URL=="" ? window.location.origin : import.meta.env.VITE_BACKEND_URL;

export const api = axios.create(
    {
        baseURL: `${base_url}/api`
    }
)