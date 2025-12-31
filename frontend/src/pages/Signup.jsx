import { useState } from "react";
import { useUserContext } from "../context/UserContext";
import styles from './Page.module.css';


export default function Signup({ setIsLogin }) {
  const { signup } = useUserContext();

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
    <h1>uyy</h1>
  );
}
