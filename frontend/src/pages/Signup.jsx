import { useState } from "react";
import { useUserContext } from "../context/UserContext";
import styles from './Page.module.css';


export default function Signup({ setIsLogin }) {
  const { signup } = useUserContext();
  const{error}=useUserContext();

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
            <input type="text"
            name="username"
            placeholder="Enter a username"
            value={formData.username}
            className={styles.input}
            required />

            <input type="email" name="email"
            placeholder="Enter a email"
            value={formData.email}
            className={styles.input}
            required            
            />
            <input type="password" name="password" placeholder="Password"
            value={formData.password}
            className={styles.password} 
            required/>

            <select name="gender" value={formData.gender} className={styles.input}>
                <option value="">select gender</option>
                <option value="male">male</option>
                <option value="femal">female</option>
                <option value="other">other</option>
            </select>
            <input type="date" 
            name="dateOfBirth"
            className={styles.input}
            value={formData.date_of_birth}/>

            {error&& <p className={styles.error}>{error}</p>}
            <button type="submit" className={styles.button}>sign up</button>
            <p className={styles.switchText}>Already have a account?
            <button type="button" className={styles.switchButton}>login</button>
            </p>
        </form>
    </div>
);
}
