import { createContext, useContext, useEffect, useState } from "react";
import { AuthApi } from "../services/user";

const UserContext = createContext();

export const UserProvider = ({children})=>{

  const [user, setUser]=useState(null);
  const [loading, setLoading]=useState(false);
  const [error, setError] = useState(null);

  const signup = async (username, email, gender, date_of_birth, password) =>{
    try{
      setError(null);
      setLoading(true);
      let res = await AuthApi.signup(username, email, gender, password, date_of_birth);
      login(email, password);
    }catch(err){
      setError(err.response.data.message);
    }finally{
      setLoading(false);
    }
  }

  const login = async (userOrEmail, password) =>{
    try{
      setError(null);
      setLoading(true);
      let res = await AuthApi.login(userOrEmail, password);
      verify_user();
      console.log(res);

    }catch(err){
      setError(err.response.data.message);
    }finally{
      setLoading(false);
    }
  }

  const verify_user = ()=>{
    AuthApi.getAuthenticatedUser().then((res)=>{
          setUser(res.data);
        }).catch((err)=>{
      setUser(null);
    })
  }

  useEffect(()=>{
    verify_user();
  }, [])

  const logout = ()=>{
    AuthApi.logout().then((data)=>{
      verify_user();
    }).catch((err)=>{
      verify_user();
    })
  }

  const value={
    user,
    loading,
    error,

    signup,
    login,
    logout
  }

  return(
    <UserContext.Provider value={value}>{children}</UserContext.Provider>
  )
}


export const useUserContext = () =>{
  const ctx = useContext(UserContext);
  return ctx;
}

