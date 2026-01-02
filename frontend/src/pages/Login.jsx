import { useState } from "react";
import { useUserContext } from "../context/UserContext";
import styles from "./Page.module.css";


export default function Login({ setIsLogin }) {
  const { login, forgotPassword, resetPassword } = useUserContext();

  const [mode, setMode] = useState("login");
  const [emailOrUsername, setEmailOrUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
  };

  const handleForgotPassword = async (e) => {
    e.preventDefault();
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
  };

  const renderLoginForm = () => (
    <form onSubmit={handleLogin} className={styles.formBox}>
      <h2 className={styles.title}>Login</h2>

      <input
        type="text"
        placeholder="Email or Username"
        value={emailOrUsername}
        onChange={(e) => setEmailOrUsername(e.target.value)}
        className={styles.input}
        required
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className={styles.input}
        required
      />

      {error && <p className={styles.error}>{error}</p>}
      {message && <p className={styles.success}>{message}</p>}

      <button type="submit" className={styles.button} onClick={handleLogin}>
        Login
      </button>

      <p className={styles.switchText}>
        <button
          type="button"
          className={styles.linkButton}
          onClick={() => setMode("forgot")}
        >
          Forgot password?
        </button>
      </p>

      <p className={styles.switchText}>
        Don’t have an account? 
        <button
          type="button"
          onClick={()=>setIsLogin(false)}
          className={styles.switchButton}
        >
          Sign up
        </button>
      </p>
    </form>
  );

  /** FORGOT PASSWORD FORM **/
  const renderForgotForm = () => (
    <form onSubmit={handleForgotPassword} className={styles.formBox}>
      <h2 className={styles.title}>Forgot Password</h2>

      <input
        type="email"
        placeholder="Enter your email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className={styles.input}
        required
      />

      {error && <p className={styles.error}>{error}</p>}
      {message && <p className={styles.success}>{message}</p>}

      <button type="submit" className={styles.button}>
        Send OTP
      </button>

      <p className={styles.switchText}>
        <button
          type="button"
          className={styles.linkButton}
          onClick={() => setMode("login")}
        >
          Back to login
        </button>
      </p>
    </form>
  );

  /** RESET PASSWORD FORM **/
  const renderResetForm = () => (
    <form onSubmit={handleResetPassword} className={styles.formBox}>
      <h2 className={styles.title}>Reset Password</h2>

      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className={styles.input}
        required
      />

      <input
        type="text"
        placeholder="OTP"
        value={otp}
        onChange={(e) => setOtp(e.target.value)}
        className={styles.input}
        required
      />

      <input
        type="password"
        placeholder="New Password"
        value={newPassword}
        onChange={(e) => setNewPassword(e.target.value)}
        className={styles.input}
        required
      />

      {error && <p className={styles.error}>{error}</p>}
      {message && <p className={styles.success}>{message}</p>}

      <button type="submit" className={styles.button}>
        Reset Password
      </button>

      <p className={styles.switchText}>
        <button
          type="button"
          className={styles.linkButton}
          onClick={() => setMode("login")}
        >
          Back to login
        </button>
      </p>
    </form>
  );

  return (
    <div className={styles.pageContainer}>
      {mode === "login"
        ? renderLoginForm()
        : mode === "forgot"
        ? renderForgotForm()
        : renderResetForm()}
    </div>
  );
}
