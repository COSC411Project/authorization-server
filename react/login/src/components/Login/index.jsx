import github from '../../assets/github-mark.svg'
import google from '../../assets/google-icon.svg'
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
            <h1>Guava</h1>
            
            <div className={style.options}>
                <div className={style.oauthClients}>
                    <p>Sign in with:</p>
                    <a href="/oauth2/authorization/google">
                        <img src={google} className={style.google} />
                        <span>Google</span>
                    </a>
                    <a href="/oauth2/authorization/github">
                        <img src={github} className={style.github} />
                        <span>GitHub</span>
                    </a>
                </div>                

                <div className={style.line}></div>
                <p className={style.or}>or</p>

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

                    <input type="submit" value="Continue" />
                </form>
            </div>
        </section>
    )
}

export default Login