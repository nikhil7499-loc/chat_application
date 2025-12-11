import { api } from "./base"

export const signup = async (username, email, password, date_of_birth, gender) =>{
    const user_obj={
        username: username,
        email: email,
        password: password,
        dateOfBirth: date_of_birth,
        gender: gender
    }

    const res=await api.post("/auth/signup", user_obj);

    return res
}