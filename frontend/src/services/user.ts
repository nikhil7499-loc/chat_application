import { api } from "./base";
export interface SignupRequest {
  username: string;
  email: string;
  password: string;
  name?: string;
  gender?: "male" | "female" | "other";
  dateOfBirth?: string;
}

export interface LoginRequest {
  emailOrUsername: string;
  password: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  email: string;
  otp: string;
  newPassword: string;
}

export interface UserResponse {
  id: string;
  username: string;
  email: string;
  name: string;
  gender: string | null;
  profilePicture: string | null;
  createdAt?: string | null;
}

export const AuthAPI = {
  signup: async (data: SignupRequest): Promise<UserResponse> => {
    const res = await api.post("/auth/signup", data);
    return res.data;
  },

  login: async (data: LoginRequest): Promise<{ message: string; token: string }> => {
    const res = await api.post("/auth/login", data);
    if (res.data?.token) localStorage.setItem("token", res.data.token);
    return res.data;
  },

  logout: async (): Promise<string> => {
    const res = await api.post("/auth/logout");
    localStorage.removeItem("token");
    return res.data;
  },

  getAuthenticatedUser: async (): Promise<UserResponse> => {
    const res = await api.get("/auth/me");
    return res.data;
  },

  forgotPassword: async (data: ForgotPasswordRequest): Promise<string> => {
    const res = await api.post("/auth/forgot-password", data);
    return res.data;
  },

  resetPassword: async (data: ResetPasswordRequest): Promise<string> => {
    const res = await api.post("/auth/reset-password", data);
    return res.data;
  },
};

export const UserAPI = {
  updateProfile: async (data: {
    username?: string;
    name?: string;
    file?: File | null;
  }): Promise<UserResponse> => {
    const formData = new FormData();
    if (data.username) formData.append("username", data.username);
    if (data.name) formData.append("name", data.name);
    if (data.file) formData.append("file", data.file);

    const res = await api.post("/user/update-profile", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return res.data;
  },

  getProfilePictureUrl: (fileName: string): string => {
    if (!fileName) return "";
    return `${api.defaults.baseURL}/user/profile-picture/${fileName}`;
  },
};
