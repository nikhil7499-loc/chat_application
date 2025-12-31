import { useState } from "react";
import { useUserContext } from "../context/UserContext";

export default function Signup({ setIsLogin }) {

    const {signup} = useUserContext();

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
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e)=>{
    e.preventDefault();
    await signup(formData.username, formData.email, formData.gender, formData.date_of_birth, formData.password);
  }


  return (
    <>
      <form onSubmit={handleSubmit}>
        <h2>Signup Form</h2>

        <input
          type="text"
          name="username"
          placeholder="Username"
          value={formData.username}
          onChange={handleChange}
          required
        />
        <br />
        <br />

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <br />
        <br />

        <select
          name="gender"
          value={formData.gender}
          onChange={handleChange}
          required
        >
          <option value="">Select Gender</option>
          <option value="male">Male</option>
          <option value="female">Female</option>
          <option value="other">Other</option>
        </select>
        <br />
        <br />

        <input
          type="date"
          name="date_of_birth"
          value={formData.date_of_birth}
          onChange={handleChange}
          required
        />
        <br />
        <br />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <br />
        <br />

        <button type="submit">Sign Up</button>
      </form>
      <button onClick={() => setIsLogin(true)}>
        Already have an account? Click here to login
      </button>
    </>
  );
}
