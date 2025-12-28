import { createContext,useContext,useEffect,useState } from "react";
import type { ReactNode } from "react";
import{AuthAPI,UserAPI} from "../common/LoaderContext";

interface UserContextType{
    user,
    loading,
   signup: (data) => Promise<void>;
  login: (data) => Promise<void>;
  logout: () => Promise<void>;
  refreshUser: () => Promise<void>;
  forgotPassword: (email) => Promise<void>;
  resetPassword: ( data) => Promise<void>;
  updateProfile: (data) => Promise<void>;
  getProfilePictureUrl: (fileName) => string;
}

const UserContext= createContext<UserContextType|undefined>(undefined);

export const UserProvider=({children})=>{
    const [user,setUser]=useState<UserResponse|null>(null);
    const[loading,setLoading]=useState(true);
    const[error,setError]=useState<String|null>(null);
    const loader=useLoader();


  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
    refreshUser();
    } else {
    setLoading(false);
    }
  }, []);

  const signup = async (data) => {
    try {
      setError(null);
      loader(true);
      const newUser = await AuthAPI.signup(data);
      setUser(newUser);
    } catch (err) {
      setError(err.response?.data || "Signup failed");
    } finally {
      loader(false);
    }
  };

  const forgotPassword = async (email) => {
    try {
      setError(null);
      loader(true);
      await AuthAPI.forgotPassword({ email});
    } catch (err) {
      setError(err.response?.data || "Failed to send reset email/OTP");
    } finally {
      loader(false);
    }
  };

  const resetPassword = async (data) => {
    try {
      setError(null);
      loader(true);
      await AuthAPI.resetPassword(data);
    } catch (err) {
      setError(err.response?.data || "Password reset failed");
    } finally {
      loader(false);
    }
  };

  const login = async (data) => {
      try {
        setError(null);
        loader(true);
        await AuthAPI.login(data);
        await refreshUser();
      } catch (err) {
        setError(err.response?.data || "Login failed");
      } finally {
        loader(false);
      }
    };
  
    const logout = async () => {
      try {
        loader(true);
        await AuthAPI.logout();
      } finally {
        localStorage.removeItem("token");
        setUser(null);
        loader(false);
      }
    };
  
}