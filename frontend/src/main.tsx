import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { LoaderProvider } from './common/LoaderContext.tsx'
import {UserProvider} from "./context/UserContext.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <LoaderProvider>
      <UserProvider>
        <App />
      </UserProvider>
    </LoaderProvider>
  </StrictMode>,
)
