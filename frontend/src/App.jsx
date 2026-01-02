import {useUserContext} from './context/UserContext';
import { useState } from "react";

import Login from "./pages/Login";
import Signup from "./pages/Signup";
import ChatWindow from './pages/ChatWindow'

export default function App() {
  const {user} = useUserContext();
  const [isLogin, setIsLogin] = useState(false);
  console.log(user)
  if(!user){
    return(
      <>
        {isLogin ? <Login setIsLogin={setIsLogin}/> : <Signup setIsLogin={setIsLogin}/>}
      </>
    )
  }

  return(
    <ChatWindow/>
  )
}
