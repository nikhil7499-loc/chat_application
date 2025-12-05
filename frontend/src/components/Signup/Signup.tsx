export default function Signup() {
  return (
    <>
    <form action="">
      <input type="text" placeholder="username" />
      <input type="email" placeholder="email" />
      <input type="password" placeholder="password" />
      <div>
        <label htmlFor="male">Male</label>
        <input type="radio" name="gender" value={"male"} />
        <label htmlFor="male">Female</label>
        <input type="radio" name="gender" value={"female"} />
        <label htmlFor="male">Other</label>
        <input type="radio" name="gender" value={"other"} />
      </div>
      <input type="date" placeholder="enter date of birth" />
      <button>Signup</button>
    </form>
    </>
  );
}
