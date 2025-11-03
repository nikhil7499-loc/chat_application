import { createContext, useContext, useEffect, useState } from "react";
import type { ReactNode } from "react";
import { AuthAPI, UserAPI } from "../services/user";
import type { UserResponse, LoginRequest, SignupRequest, ResetPasswordRequest, ForgotPasswordRequest } from "../services/user";
import { useLoader } from "../common/LoaderContext";

interface UserContextType {
  user: UserResponse | null;
  loading: boolean;
  error: string | null;
  signup: (data: SignupRequest) => Promise<void>;
  login: (data: LoginRequest) => Promise<void>;
  logout: () => Promise<void>;
  refreshUser: () => Promise<void>;
  forgotPassword: (email: string) => Promise<void>;
  resetPassword: ( data: ResetPasswordRequest) => Promise<void>;
  updateProfile: (data: { username?: string; name?: string; file?: File | null }) => Promise<void>;
  getProfilePictureUrl: (fileName: string) => string;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<UserResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const loader = useLoader();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      refreshUser();
    } else {
      setLoading(false);
    }
  }, []);

  const signup = async (data: SignupRequest) => {
    try {
      setError(null);
      loader(true);
      const newUser = await AuthAPI.signup(data);
      setUser(newUser);
    } catch (err: any) {
      setError(err.response?.data || "Signup failed");
    } finally {
      loader(false);
    }
  };

  const forgotPassword = async (email: string) => {
    try {
      setError(null);
      loader(true);
      await AuthAPI.forgotPassword({ email});
    } catch (err: any) {
      setError(err.response?.data || "Failed to send reset email/OTP");
    } finally {
      loader(false);
    }
  };

  const resetPassword = async (data: ResetPasswordRequest) => {
    try {
      setError(null);
      loader(true);
      await AuthAPI.resetPassword(data);
    } catch (err: any) {
      setError(err.response?.data || "Password reset failed");
    } finally {
      loader(false);
    }
  };


  const login = async (data: LoginRequest) => {
    try {
      setError(null);
      loader(true);
      await AuthAPI.login(data);
      await refreshUser();
    } catch (err: any) {
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

  const refreshUser = async () => {
    try {
      setLoading(true);
      loader(true);
      const u = await AuthAPI.getAuthenticatedUser();
      setUser(u);
    } catch (err) {
      console.error("Failed to fetch user:", err);
      setUser(null);
      localStorage.removeItem("token");
    } finally {
      setLoading(false);
      loader(false);
    }
  };

  const updateProfile = async (data: { username?: string; name?: string; file?: File | null }) => {
    try {
      setError(null);
      loader(true);
      const updatedUser = await UserAPI.updateProfile(data);
      setUser(updatedUser);
    } catch (err: any) {
      setError(err.response?.data || "Profile update failed");
    } finally {
      loader(false);
    }
  };

  const getProfilePictureUrl = (fileName: string) => UserAPI.getProfilePictureUrl(fileName);

  const value: UserContextType = {
    user,
    loading,
    error,
    signup,
    login,
    logout,
    refreshUser,
    forgotPassword,
    resetPassword,
    updateProfile,
    getProfilePictureUrl,
  };

  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};

export const useUserContext = (): UserContextType => {
  const ctx = useContext(UserContext);
  if (!ctx) throw new Error("useUserContext must be used within a UserProvider");
  return ctx;
};
