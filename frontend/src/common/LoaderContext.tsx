import React, { createContext, useContext, useState } from "react";
import type { ReactNode } from "react";
import { createPortal } from "react-dom";

const LoaderContext = createContext<((show: boolean) => void) | undefined>(undefined);

export const LoaderProvider = ({ children }: { children: ReactNode }) => {
  const [visible, setVisible] = useState(false);

  const toggleLoader = (show: boolean) => setVisible(show);

  return (
    <LoaderContext.Provider value={toggleLoader}>
      {children}
      {visible && createPortal(<FloatingLoader />, document.body)}
    </LoaderContext.Provider>
  );
};

// ==============================
// 🔹 Floating Loader (non-blocking)
// ==============================
const FloatingLoader: React.FC = () => {
  const containerStyle: React.CSSProperties = {
    position: "fixed",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    zIndex: 9999,
    pointerEvents: "none", // 👈 allows clicking through
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  };

  const spinnerStyle: React.CSSProperties = {
    width: "50px",
    height: "50px",
    border: "6px solid #e0e0e0",
    borderTop: "6px solid #007bff",
    borderRadius: "50%",
    animation: "spin 1s linear infinite",
  };

  const textStyle: React.CSSProperties = {
    marginTop: "10px",
    color: "#333",
    fontSize: "14px",
    fontFamily: "Arial, sans-serif",
  };

  return (
    <>
      <style>
        {`
          @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
          }
        `}
      </style>
      <div style={containerStyle}>
        <div style={spinnerStyle}></div>
        <p style={textStyle}>Loading...</p>
      </div>
    </>
  );
};

// ==============================
// 🔹 Hook to use loader anywhere
// ==============================
export const useLoader = () => {
  const ctx = useContext(LoaderContext);
  if (!ctx) throw new Error("useLoader must be used within a LoaderProvider");
  return ctx;
};
