import { api } from "./base"


export const AuthApi  ={
    signup: async (username, email, gender, password, date_of_birth)=>{
        let user_obj = {
            username: username,
            email: email,
            gender: gender,
            password: password,
            dateOfBirth: date_of_birth
        };

        let res = await api.post("/auth/signup", user_obj);
        return res.data;
    },

    login: async (emailOrUsername, password) =>{
        let user_obj={
            emailOrUsername: emailOrUsername,
            password: password
        };

        let res = await api.post("/auth/login", user_obj);

        return res.data;
    },

    logout: async () =>{
        let res = await api.post("/auth/logout");

        return res.data;
    },

    getAuthenticatedUser: async () => {
        let res = await api.post("/auth/me");

        return res.data;
    },


    forgotPassword: async (email)=> {
        let data = {
            email: email
        }
        const res = await api.post("/auth/forgot-password", data);
        return res.data;
    },

    resetPassword: async (email, otp, new_password) => {
        let data ={
            email: email,
            otp: otp,
            newPassword: new_password
        }
        const res = await api.post("/auth/reset-password", data);
        return res.data;
    },
}