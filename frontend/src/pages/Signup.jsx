import { useState } from "react";
import { signup } from "../services/auth";
import styles from "./Page.module.css";
import { handle } from "express/lib/application";

export default function Signup({onSwitch}){
  const {signup}= useUserContext();
  const[form,setform]=useState<SignupRequest>({
    username:"",
    email:"",
    password:"",
    name:"",
    genser:undefined,
    dateOfBirth:"",
  });

  const [error,setError]=useState<string| null>(null);

  const handleChange=(e)=>{
    const {name,value}=e.target;
    setform((prev)=>({
      ...prev,
      [name]:
      name==="gender"?(value)
    }));
  };
  const handeleSignup=async(e)=>{
    e.preventDefault();
    try{
      setError(null);
      await signup(form);
    }
    catch{
      setError("signup failed . please check your details.");
    }
  };


  return (
    <div className={styles.pageContainer}>
      <form onSubmit={handeleSignup}className={styles.formBox}>
        <h2 className={styles.title}>Create an Account</h2>
        <input
          type="text"
          placeholder="username"
          value={form.username}
          onChange={handleChange}
          className={styles.input}
          required
        />

        <input
          type="email"
          placeholder="email"
          value={form.email}
          onChange={handleChange}
          className={styles.input}
          required
        />

        <input
          type="password"
          placeholder="password"
          value={form.password}
          onChange={styles.input}
          required
        />

         <select name="gender" value={form.gender||""} onChange={handleChange} className={styles.input}>

          <option value="">select gender</option>
          <option value="male">male</option>
          <option value="female">female</option>
          <option value="other">other</option>
          
         </select>
         <input type="date" name="dateOfBirth" onChange={handleChange} className={styles.input}/>
         {error&&<p className={styles.error}>{error}</p>}
         <button type="submit" className={styles.button }>signup</button>

         <p className={Style.SwitchText}>ALREADY HAVE AN ACCOUNt?{""}
          <button type="button" onclick={onSwitch} className={styles.switchButton}>
          login
          </button>
          </p>
          </form>
          </div>
      );
};
