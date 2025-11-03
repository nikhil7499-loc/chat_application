import { useState } from "react";
import { useUserContext } from "./context/UserContext";

import Login from "./pages/Login";
import Signup from "./pages/Signup";
import ChatWindow from "./pages/ChatWindow";

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

  return <ChatWindow />;
}

export default App;
