export default function Login({setIsLogin}){
    return(
        <>
            this is login page

            <button onClick={()=>setIsLogin(false)}>don't have an account? click here to signup</button>
        </>
    )
}