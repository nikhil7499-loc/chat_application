import { useUserContext } from "../context/UserContext"

export default function ChatWindow(){
    const {logout} = useUserContext();
    return(
        <>
            <h1>this is chat window</h1>
            <button onClick={logout}>Logout</button>
        </>
    )
}