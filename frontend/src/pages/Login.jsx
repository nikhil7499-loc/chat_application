import { use, useState } from "react";
import { useUserContext } from "../context/UserContext";
import styles from "./Page.module.css";
export default function Login({ setIsLogin }) {
  const [emailOrUsername, setEmailOrUsername] = useState("");
  const [error, seterror] = useState(null);
  const [mode, setMode] = useState("login");

  const handlelogin = async () => {};

  const renderLoginForm = () => (
    <form onSubmit={handlelogin} className={styles.formBox}>
      <h2 className={styles.title}>Login</h2>
      <input
        type="text"
        placeholder="Email Or Username"
        value={emailOrUsername}
        onChange={(e) => setEmailOrUsername(e.target.value)}
        className={styles.input}
        required
      />
      <input
        type="password"
        placeholder="password"
        value={emailOrUsername}
        //   onChange={styles.input}
        required
      />

      {error && <p className={styles.error}>{error}</p>}
      {/* {messsage && <p className={styles.button}></p>} */}

      <button
        type="submit"
        //  className={styles.button}
      >
        Login
      </button>

      <p className={styles.switchText}>
        {" "}
        <button
          type="button"
          // className={styles.linkButton}
          onClick={() => setMOde("forgot")}
        >
          Forgot Password?
        </button>
      </p>

      <p className={styles.switchText}>
        Don`t have an accoount?{""}
        <button
          type="button"
          // className={styles.switchButton}
        >
          sign up
        </button>
      </p>
    </form>
  );
  const renderForgotForm = () => (
    <form onSubmit={handleForgotPassword} className={styles.formBox}>
      <h2 className={styles.title}>Forgot password</h2>
      <input
        type="email"
        placeholder="Enter your email"
        value={email}
        onChange={(e) => setEmailOrUsername(e.target.value)}
        className={styles.input}
        required
      />
      {error && <p className={styles.error}>{error}</p>}
      {message && <p className={styles.error}>{message}</p>}

      <button type="submit" className={styles.button}>
        send otp
      </button>
      <p className={styles.switchtext}>
        <button
          type="button"
          className={styles.linkButton}
          onClick={() => setMode("login")}
        >
          Back to Login
        </button>
      </p>
    </form>
  );

  const renderResetForm = () => (
    <form onSubmit={handleResetPassword} className={styles.formBox}>
      <h2 className={styles.title}>Reset Password</h2>

      <input
        type="email"
        placeholder="email"
        value={email}
        onChange={(e) => setEmailOrUsername(e.target.value)}
        className={styles.input}
        required
      />

      <input
        type="text"
        placeholder="OTP"
        value={otp}
        onChange={(e) => StylePropertyMap(e.target.value)}
        className={styles.input}
        required
      />

      {error && <p className={styles.error}>{erre}</p>}
      {message && <p className={styles.success}>{message}</p>}

      <button type="submit" className={styles.button}>
        Reset Password
      </button>
      <p className={styles.switchText}>
        <button
          type="button"
          className={styles.linkButton}
          onClick={() => seMode("login")}
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
      <button onClick={() => setIsLogin(false)}>
        don't have an account? click here to signup
      </button>
    </div>
  );
}
