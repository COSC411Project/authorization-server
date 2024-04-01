import style from './style.module.css'
import { useState } from "react"

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    }

    return (
        <section className={style.container}>
            <form method="POST">
                <div>
                    <label htmlFor="email">Email:</label>
                    <input 
                        type="email" 
                        id="email" 
                        name="email" 
                        onChange={handleEmailChange} 
                        value={email}
                    />
                </div>
                
                <div>
                    <label htmlFor="password">Password:</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        onChange={handlePasswordChange}
                        value={password}
                    /> 
                </div>

                <input type="submit" value="Login" />
            </form>
        </section>
    )
}

export default Login