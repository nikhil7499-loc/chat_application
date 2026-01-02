import { useState } from "react";
import { useUserContext } from "../context/UserContext";
import styles from "./Page.module.css";

export default function Signup({ setIsLogin }) {
  const { signup } = useUserContext();
  const { error } = useUserContext();

  const [formData, setFormData] = useState({
    username: "",
    email: "",
    gender: "",
    date_of_birth: "",
    password: "",
  });
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await signup(
      formData.username,
      formData.email,
      formData.gender,
      formData.date_of_birth,
      formData.password
    );
  };

  return (
    <div className={styles.pageContainer}>
      <form className={styles.formBox}>
        <h2 className={styles.title}>Create an account</h2>
        <input
          type="text"
          name="username"
          placeholder="Enter a username"
          value={formData.username}
          className={styles.input}
          onChange={handleChange}
          required
        />

        <input
          type="email"
          name="email"
          placeholder="Enter a email"
          value={formData.email}
          onChange={handleChange}
          className={styles.input}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          className={styles.input}
          required
        />

        <select
          name="gender"
          value={formData.gender}
          className={styles.input}
          onChange={handleChange}
        >
          <option value="">select gender</option>
          <option value="male">male</option>
          <option value="femal">female</option>
          <option value="other">other</option>
        </select>
        <input
          type="date"
          name="date_of_birth"
          className={styles.input}
          value={formData.date_of_birth}
          onChange={handleChange}
        />

        {error && <p className={styles.error}>{error}</p>}
        <button type="submit" className={styles.button}>
          sign up
        </button>
        <p className={styles.switchText}>
          Already have a account?
          <button type="button" className={styles.switchButton} onClick={()=>setIsLogin(true)}>
            login
          </button>
        </p>
      </form>
    </div>
  );
}
