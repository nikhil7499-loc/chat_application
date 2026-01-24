import { MessageTypeProvider, useMessageTypeContext } from "../context/MessageTypeContext";
import { useUserContext } from "../context/UserContext";


const Sample=()=>{
    const {messageTypes} =useMessageTypeContext();
    console.log("data goes here", messageTypes)
    return(
        <>
            <select>
                {
                    messageTypes.map((mt)=>(
                        <option key={mt.id}>{mt.name}</option>
                    ))
                }
            </select>
        </>
    )
}

export default function ChatWindow() {
  const { logout } = useUserContext();
  return (
    <>
      <h1>this is chat window</h1>
      <button onClick={logout}>Logout</button>

      <MessageTypeProvider>
        <div>
            <Sample></Sample>
        </div>
      </MessageTypeProvider>
    </>
  );
}
