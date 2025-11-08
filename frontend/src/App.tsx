import { useState } from "react";
import { useUserContext } from "./context/UserContext";

import Login from "./pages/Login";
import Signup from "./pages/Signup";
import ChatWindow from "./pages/ChatWindow";
import { MessageProvider } from "./context/MessageContext";

function App() {
  const { user } = useUserContext();
  const [showSignup, setShowSignup] = useState(false);

  if (!user)
    return (
      <div>
        {showSignup ? (
          <Signup onSwitch={() => setShowSignup(false)} />
        ) : (
          <Login onSwitch={() => setShowSignup(true)} />
        )}
      </div>
    );

  return <MessageProvider>
    <ChatWindow />
  </MessageProvider>;
}

export default App;
