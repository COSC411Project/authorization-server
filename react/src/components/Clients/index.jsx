import { Link } from "react-router-dom"
import style from './style.module.css'

const Clients = () => {

    return (
        <>
            <section className={style.container}>
                <div>
                    <h1>Clients</h1>
                    <Link to="register">Register</Link>
                </div>
                <div>
                    
                </div>
            </section>
        </>
    )
}

const Client = ({client}) => {

}

export default Clients