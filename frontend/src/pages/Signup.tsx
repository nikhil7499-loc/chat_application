import { useState } from "react";
import { useUserContext } from "../context/UserContext";
import type { SignupRequest } from "../services/user";
import styles from "./PageStyle.module.css";

interface SignupProps {
  onSwitch: () => void;
}

export default function Signup({ onSwitch }: SignupProps) {
  const { signup } = useUserContext();

  const [form, setForm] = useState<SignupRequest>({
    username: "",
    email: "",
    password: "",
    name: "",
    gender: undefined,
    dateOfBirth: "",
  });

  const [error, setError] = useState<string | null>(null);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]:
        name === "gender"
          ? (value as "male" | "female" | "other" | undefined)
          : value,
    }));
  };

  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setError(null);
      await signup(form);
    } catch {
      setError("Signup failed. Please check your details.");
    }
  };

  return (
    <div className={styles.pageContainer}>
      <form onSubmit={handleSignup} className={styles.formBox}>
        <h2 className={styles.title}>Create an Account</h2>

        <input
          type="text"
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
          className={styles.input}
          required
        />

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          className={styles.input}
          required
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className={styles.input}
          required
        />

        <input
          type="text"
          name="name"
          placeholder="Full Name"
          value={form.name}
          onChange={handleChange}
          className={styles.input}
          required
        />

        <select
          name="gender"
          value={form.gender || ""}
          onChange={handleChange}
          className={styles.input}
        >
          <option value="">Select Gender</option>
          <option value="male">Male</option>
          <option value="female">Female</option>
          <option value="other">Other</option>
        </select>

        <input
          type="date"
          name="dateOfBirth"
          value={form.dateOfBirth}
          onChange={handleChange}
          className={styles.input}
        />

        {error && <p className={styles.error}>{error}</p>}

        <button type="submit" className={styles.button}>
          Sign Up
        </button>

        <p className={styles.switchText}>
          Already have an account?{" "}
          <button
            type="button"
            onClick={onSwitch}
            className={styles.switchButton}
          >
            Login
          </button>
        </p>
      </form>
    </div>
  );
}
