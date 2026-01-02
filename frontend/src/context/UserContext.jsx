import { createContext, useContext, useState } from "react";
import { AuthApi } from "../services/user";

const UserContext = createContext();

export const UserProvider = ({children})=>{

  const [user, setUser]=useState(null);
  const [loading, setLoading]=useState(false);
  const [error, setError] = useState(null);

  const signup = async (username, email, gender, date_of_birth, password) =>{
    try{
      setError(null);
      // make the loader here -- it is in common context
      setLoading(true);
      let res = await AuthApi.signup(username, email, gender, password, date_of_birth);
      console.log("this is res from signup: ", res);
    
    }catch(err){
      setError(err.message);
    }finally{
      setLoading(false);
    }
  }

  const value={
    user,
    loading,
    error,

    signup,
  }

  return(
    <UserContext.Provider value={value}>{children}</UserContext.Provider>
  )
}


export const useUserContext = () =>{
  const ctx = useContext(UserContext);
  return ctx;
}

